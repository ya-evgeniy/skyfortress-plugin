package ru.jekarus.skyfortress.v3.listener;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.jserializer.itemstack.ItemStackSerializer;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.random.RandomDistribution;
import ru.jekarus.skyfortress.v3.engine.CastleDeathEngine;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;
import ru.jekarus.skyfortress.v3.gui.ShopGui;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lobby.SfLobby;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeamSettings;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.serializer.SfSerializers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Locale;

public class ConnectionListener {

    private final SkyFortressPlugin plugin;
    private SfPlayers players;

    public ConnectionListener()
    {
        this.plugin = SkyFortressPlugin.getInstance();
        this.players = SfPlayers.getInstance();

        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    @Listener
    public void onConnect(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);
        sfPlayer.setLastPlayed(-1);

        if (sfPlayer.getLocale() == null) {
            SfLanguages languages = this.plugin.getLanguages();
            Locale locale = player.getLocale();

            if (languages.has(locale)) {
                sfPlayer.setLocale(locale);
            }
            else {
                sfPlayer.setLocale(languages.getDef());
            }
        }

        this.plugin.getScoreboards().setFor(sfPlayer, player);

        SfTeam playerTeam = sfPlayer.getTeam();
        PlayerZone playerZone = sfPlayer.getZone();

        SfGameStageType gameStage = this.plugin.getGame().getStage();

        if (playerZone == PlayerZone.LOBBY || playerZone == PlayerZone.GAME || playerZone == PlayerZone.TEAM_ROOM) {
            if (playerTeam == null) {
                playerTeam = this.plugin.getTeamContainer().getNoneTeam();
                playerTeam.addPlayer(this.plugin, sfPlayer);
            }

            if (playerTeam.getType() == SfTeam.Type.NONE) {
                player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
                player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                    effects.addElement(
                            PotionEffect.builder().potionType(PotionEffectTypes.SATURATION).duration(1_000_000).amplifier(255).particles(false).build()
                    );
                    player.offer(effects);
                });

                SfLocation center = this.plugin.getLobby().getSettings().center;
                player.setLocationAndRotation(
                        center.getLocation(),
                        center.getRotation()
                );
                return;
            }
        }

        if (playerZone == PlayerZone.LOBBY) {

        }
        else if (playerZone == PlayerZone.TEAM_ROOM) {

        }
        else if (playerZone == PlayerZone.GAME) {
            if (playerTeam.getType() == SfTeam.Type.GAME) {
                SfGameTeam gameTeam = (SfGameTeam) playerTeam;
                if (!gameTeam.getCastle().isAlive()) {
                    //fixme drop all items in inventory
                    SfUtils.setPlayerSpectator(player);
                }
                else if (gameTeam.getCastle().isCaptured()) {
                    player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                        effects.addElement(
                                PotionEffect.builder().potionType(PotionEffectTypes.STRENGTH).duration(1_000_000).amplifier(0).particles(false).build()
                        );
                        player.offer(effects);
                    });
                }
            }
        }
        else if (playerZone == PlayerZone.OTHER) {
        }
        else if (playerZone == PlayerZone.CAPTAIN_SYSTEM) {
            if (gameStage == SfGameStageType.PRE_GAME) {
                this.plugin.getDistributionController().onConnect(sfPlayer, player);
            }
            if (!plugin.getDistributionController().isEnabled()) {
                if (playerTeam.getType() == SfTeam.Type.GAME) {
                    Collection<SfLobbyTeam> teams = plugin.getLobby().getTeams();
                    for (SfLobbyTeam team : teams) {
                        SfLobbyTeamSettings settings = team.getSettings();
                        if (playerTeam == settings.team) {
                            sfPlayer.setZone(PlayerZone.TEAM_ROOM);
                            player.setLocationAndRotation(
                                    settings.accepted.getLocation(),
                                    settings.accepted.getRotation()
                            );
                            break;
                        }
                    }
                }
                else {
                    SfLobby lobby = plugin.getLobby();
                    SfLocation center = lobby.getSettings().center;

                    plugin.getTeamContainer().getNoneTeam().addPlayer(plugin, sfPlayer);
                    sfPlayer.setZone(PlayerZone.LOBBY);
                    player.setLocationAndRotation(
                            center.getLocation(),
                            center.getRotation()
                    );
                }
            }
        }

    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player)
    {
        SfPlayer sfPlayer = players.getOrCreatePlayer(player);
        sfPlayer.setLastPlayed(System.currentTimeMillis());

        SfGameStageType stage = this.plugin.getGame().getStage();
        SfTeam playerTeam = sfPlayer.getTeam();

        PlayerZone playerZone = sfPlayer.getZone();
        if (playerZone == PlayerZone.LOBBY) {
            if (playerTeam.getType() == SfTeam.Type.GAME)
            {
                if (stage == SfGameStageType.IN_GAME)
                {
                    SfGameTeam gameTeam = (SfGameTeam) playerTeam;
                    CastleDeathEngine.checkCapturedCastle(this.plugin, gameTeam.getCastle());
                }
            }
        }
        else if (playerZone == PlayerZone.TEAM_ROOM) {

        }
        else if (playerZone == PlayerZone.GAME) {

        }
        else if (playerZone == PlayerZone.OTHER) {

        }
        else if (playerZone == PlayerZone.CAPTAIN_SYSTEM) {
            if (stage == SfGameStageType.PRE_GAME) {
                this.plugin.getDistributionController().onDisconnect(sfPlayer, player);
            }
        }
    }

    @Listener
    public void onMessage(MessageChannelEvent.Chat event, @First Player player)
    {
        if (false)
        {
            return;
        }
        String raw = event.getRawMessage().toPlain();

//        if (raw.equalsIgnoreCase("captain")) {
//            Optional<SfPlayer> optionalSfPlayer = SfPlayers.getInstance().getPlayer(player);
//            optionalSfPlayer.ifPresent(sfPlayer -> {
//                this.plugin.getDistributionController().runCaptain(Collections.singletonList(sfPlayer));
//            });
//        }

//        if (raw.equalsIgnoreCase("captain")) {
//
//            Path directory = Sponge.getConfigManager().getPluginConfig(this.plugin).getDirectory();
//            Path captains = Paths.get(directory.toString(), "/captain_system.conf");
//
//            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
//                    .setPath(captains)
//                    .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.SERIALIZERS))
//                    .build();
//
//            try
//            {
//                CommentedConfigurationNode node = loader.load();
//                CaptainConfig config = node.getNode("captain_system").getValue(TypeToken.of(CaptainConfig.class));
//
//                for (CaptainConfigCaptain captain : config.captains) {
//                    captain.team = (SfGameTeam) plugin.getTeamContainer().fromUniqueId(captain.teamId).orElseThrow(NullPointerException::new);
//                }
//
//                List<Player> players = new ArrayList<>(Sponge.getServer().getOnlinePlayers());
//                players.remove(player);
//
//                new CaptainDistribution(this.plugin, config).startC(Arrays.asList(player), players);
//
//
////                if (players.size() > 4) {
////                    new CaptainDistribution(this.plugin, config).startC(players.subList(0, 3), players.subList(3, players.size()));
////                }
////                else {
////                    new CaptainDistribution(this.plugin, config).startC(players, Collections.emptyList());
////                }
//                System.out.println("started");
//            }
//            catch (IOException | ObjectMappingException e)
//            {
//                e.printStackTrace();
//            }
//
//        }

        if (raw.equalsIgnoreCase("dis-random"))
        {
            new RandomDistribution().start();
        }

        if (raw.equalsIgnoreCase("stack-s"))
        {
            ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                    .setPath(Paths.get("C:\\Users\\Jeka\\IdeaProjects\\sponge\\skyfortress\\run\\config\\sky_fortress\\test.conf"))
                    .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(ItemStackSerializer.SERIALIZERS))
                    .build();
            try
            {
                CommentedConfigurationNode emptyNode = loader.createEmptyNode();
                emptyNode.setValue(TypeToken.of(ItemStack.class), itemStack);
                loader.save(emptyNode);
            }
            catch (IOException | ObjectMappingException e)
            {
                e.printStackTrace();
            }
        }
        if (raw.equalsIgnoreCase("stack-l"))
        {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                    .setPath(Paths.get("C:\\Users\\Jeka\\IdeaProjects\\sponge\\skyfortress\\run\\config\\sky_fortress\\test.conf"))
                    .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(ItemStackSerializer.SERIALIZERS))
                    .build();
            try
            {
                CommentedConfigurationNode node = loader.load();
                ItemStack stack = node.getValue(TypeToken.of(ItemStack.class));
                player.setItemInHand(HandTypes.MAIN_HAND, stack);
            }
            catch (IOException | ObjectMappingException e)
            {
                e.printStackTrace();
            }
        }
        if (raw.equalsIgnoreCase("shop-s"))
        {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                    .setPath(Paths.get("C:\\Users\\Jeka\\IdeaProjects\\sponge\\skyfortress\\run\\config\\sky_fortress\\shop_test.conf"))
                    .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.SERIALIZERS))
                    .build();
            try
            {
                CommentedConfigurationNode emptyNode = loader.createEmptyNode();
                emptyNode.getNode("gui").setValue(TypeToken.of(ShopGui.class), ShopGui.INSTANCE);
                loader.save(emptyNode);
            }
            catch (IOException | ObjectMappingException e)
            {
                e.printStackTrace();
            }
        }
        if (raw.equalsIgnoreCase("shop-l"))
        {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                    .setPath(Paths.get("C:\\Users\\Jeka\\IdeaProjects\\sponge\\skyfortress\\run\\config\\sky_fortress\\shop_test.conf"))
                    .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.SERIALIZERS))
                    .build();
            try
            {
                CommentedConfigurationNode node = loader.load();
                ShopGui gui = node.getNode("shop").getValue(TypeToken.of(ShopGui.class));
                player.openInventory(gui.getInventory());
            }
            catch (IOException | ObjectMappingException e)
            {
                e.printStackTrace();
            }
        }
        if (raw.equalsIgnoreCase("shop"))
        {
            player.openInventory(ShopGui.INSTANCE.getInventory());
        }
//        if (raw.equalsIgnoreCase("load"))
//        {
//            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(Paths.get("C:\\Users\\Jeka\\IdeaProjects\\sponge\\skyfortress\\run\\config\\sky_fortress\\test.conf")).build();
//            try
//            {
//                CommentedConfigurationNode node = loader.load();
//                TextTemplate template = node.getValue(TypeToken.of(TextTemplate.class));
//                player.sendMessage(template);
//            }
//            catch (IOException | ObjectMappingException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        if (raw.equalsIgnoreCase("save"))
//        {
//            TextTemplate template = TextTemplate.of(
//                    TextTemplate.arg("team_0").optional().defaultValue(Text.of("team_0")).build(),
//                    Text.builder().append(Text.of(":")).color(TextColors.GRAY).build()
//            );
//
//            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(Paths.get("C:\\Users\\Jeka\\IdeaProjects\\sponge\\skyfortress\\run\\config\\sky_fortress\\test.conf")).build();
//            try
//            {
//                CommentedConfigurationNode emptyNode = loader.createEmptyNode();
//                emptyNode.setValue(TypeToken.of(TextTemplate.class), template);
//                loader.save(emptyNode);
//            }
//            catch (IOException | ObjectMappingException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        if (raw.equalsIgnoreCase("r"))
//        {
//            Vector3d rotation = player.getRotation();
//            player.sendMessage(Text.of(String.format("rotation = { x = %s, y = %s, z = %s }", rotation.getX(), rotation.getY(), rotation.getZ())));
//        }
    }
//
//    @Listener
//    public void onBlockPlace(ChangeBlockEvent.Place event) {
//        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
//            BlockSnapshot block = transaction.getFinal();
//            block.getLocation().ifPresent(worldLocation -> {
//                double x = worldLocation.getX();
//                double y = worldLocation.getY();
//                double z = worldLocation.getZ();
//
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append('\n').append("\t\t{");
//                stringBuilder.append('\n').append("\t\t\tchanged_blocks = [");
//                stringBuilder.append('\n').append(String.format("\t\t\t\t{ position = { x = %s, y = %s, z = %s } }", x, y-2, z));
//                stringBuilder.append('\n').append("\t\t\t]");
//                stringBuilder.append('\n').append("\t\t\tchanged_blocks = {");
//                stringBuilder.append('\n').append(String.format("\t\t\t\tposition = { x = %s, y = %s, z = %s } }", x+.5, y, z+.5));
//                stringBuilder.append('\n').append(String.format("\t\t\t\trotation = { x = 0.0, y = 0.0, z = 0.0} }", x+.5, y, z+.5));
//                stringBuilder.append('\n').append("\t\t\t}");
//                stringBuilder.append('\n').append("\t\t}");
//
//                Toolkit.getDefaultToolkit().getSystemClipboard()
//                        .setContents(new StringSelection(stringBuilder.toString()), null);
//
//                System.out.println(stringBuilder);
//            });
//        }
//    }

}
