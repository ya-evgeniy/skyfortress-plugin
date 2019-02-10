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
import org.spongepowered.api.text.translation.locale.Locales;
import ru.jekarus.jserializer.itemstack.ItemStackSerializer;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.random.RandomDistribution;
import ru.jekarus.skyfortress.v3.engine.CastleDeathEngine;
import ru.jekarus.skyfortress.v3.game.SfGameStageType;
import ru.jekarus.skyfortress.v3.gui.ShopGui;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.serializer.SfSerializers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.io.IOException;
import java.nio.file.Paths;

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
    public void onConnect(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player)
    {
        SfPlayer sfPlayer = players.getOrCreatePlayer(player);
        sfPlayer.setLastPlayed(-1);
//        SfLanguage language = this.plugin.getLanguages().get(player.getLocale()); // fixme not work
        SfLanguage language = this.plugin.getLanguages().get(Locales.RU_RU);
        if (language != null)
        {
//            player.sendMessage(Text.of("Твой язык: " + player.getLocale()));
            sfPlayer.setLocale(language.locale);
        }
        SfTeam team = sfPlayer.getTeam();

        if (team == null)
        {
            team = this.plugin.getTeamContainer().getNoneTeam();
            team.addPlayer(this.plugin, sfPlayer);
            SfLocation center = this.plugin.getLobby().getSettings().center;
            player.setLocationAndRotation(
                    center.getLocation(),
                    center.getRotation()
            );
            player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
            player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                effects.addElement(
                        PotionEffect.builder().potionType(PotionEffectTypes.SATURATION).duration(1_000_000).amplifier(255).particles(false).build()
                );
                player.offer(effects);
            });
        }
        else if (team.getType() == SfTeam.Type.GAME)
        {
            SfGameTeam gameTeam = (SfGameTeam) team;
            if (!gameTeam.getCastle().isAlive())
            {
                SfUtils.setPlayerSpectator(player);
            }
            else if (gameTeam.getCastle().isCaptured())
            {
                player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                    effects.addElement(
                            PotionEffect.builder().potionType(PotionEffectTypes.STRENGTH).duration(1_000_000).amplifier(0).particles(false).build()
                    );
                    player.offer(effects);
                });
            }
        }
        else if (team.getType() == SfTeam.Type.NONE)
        {
            SfLocation center = this.plugin.getLobby().getSettings().center;
            player.setLocationAndRotation(
                    center.getLocation(),
                    center.getRotation()
            );
        }

        this.plugin.getScoreboards().setFor(sfPlayer, player);
    }

    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player)
    {
        players.getPlayer(player).ifPresent(sfPlayer -> {
            sfPlayer.setLastPlayed(System.currentTimeMillis());
            SfTeam team = sfPlayer.getTeam();
            if (team.getType() == SfTeam.Type.GAME)
            {
                if (this.plugin.getGame().getStage() == SfGameStageType.IN_GAME)
                {
                    SfGameTeam gameTeam = (SfGameTeam) team;
                    CastleDeathEngine.checkCapturedCastle(this.plugin, gameTeam.getCastle());
                }
            }
        });
    }

    @Listener
    public void onMessage(MessageChannelEvent.Chat event, @First Player player)
    {
        if (false)
        {
            return;
        }
        String raw = event.getRawMessage().toPlain();

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

}
