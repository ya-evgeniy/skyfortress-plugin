package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import ru.jekarus.jserializer.itemstack.ItemStackSerializer;
import ru.jekarus.skyfortress.v3.SfSettings;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.gui.ShopGui;
import ru.jekarus.skyfortress.v3.lang.SfMapLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfGameMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfLobbyMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfInGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfPostGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfPreGameScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfScoreboardLanguage;
import ru.jekarus.skyfortress.v3.lobby.SfLobbyTeam;
import ru.jekarus.skyfortress.v3.resource.SfResource;
import ru.jekarus.skyfortress.v3.serializer.distribution.CaptainConfigSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.SfMapLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.messages.SfGameMessagesLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.messages.SfLobbyMessagesLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.messages.SfMessagesLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.scoreboard.SfInGameScoreboardLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.scoreboard.SfPostGameScoreboardLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.scoreboard.SfPreGameScoreboardLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.language.scoreboard.SfScoreboardLanguageSerializer;
import ru.jekarus.skyfortress.v3.serializer.shop.ShopGuiSerializer;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

public class SfSerializers {

    public static final TypeSerializerCollection SERIALIZERS = TypeSerializers.getDefaultSerializers().newChild();
    public static final TypeSerializerCollection DEF_SERIALIZERS = TypeSerializers.getDefaultSerializers();

    static {

        SERIALIZERS.registerType(TypeToken.of(Vector3d.class), new SfVector3dSerialize());
        SERIALIZERS.registerType(TypeToken.of(SfLocation.class), new SfLocationSerializer());

        SERIALIZERS.registerType(TypeToken.of(SfSettings.class), new SfSettingsSerializer());
        SERIALIZERS.registerType(TypeToken.of(SfTeam.class), new SfTeamSerializer());
        SERIALIZERS.registerType(TypeToken.of(SfCastle.class), new SfCastleSerializer());
        SERIALIZERS.registerType(TypeToken.of(SfCastlePositions.class), new SfCastlePositionsSerializer());
        SERIALIZERS.registerType(TypeToken.of(Location.class), new SfSpongeLocationSerializer());
        SERIALIZERS.registerType(TypeToken.of(SfResource.class), new SfResourceSerializer());
//        SERIALIZERS.registerType(TypeToken.of(ItemStack.class), new SfItemStackSerializer());
        SERIALIZERS.registerType(TypeToken.of(SfLobbyTeam.class), new SfLobbyTeamSerializer());

        SERIALIZERS.registerType(TypeToken.of(ItemStack.class), new ItemStackSerializer());
        SERIALIZERS.registerType(TypeToken.of(ShopGui.class), new ShopGuiSerializer());



        DEF_SERIALIZERS.registerType(TypeToken.of(SfMapLanguage.class), new SfMapLanguageSerializer());

        DEF_SERIALIZERS.registerType(TypeToken.of(SfScoreboardLanguage.class), new SfScoreboardLanguageSerializer());
        DEF_SERIALIZERS.registerType(TypeToken.of(SfPreGameScoreboardLanguage.class), new SfPreGameScoreboardLanguageSerializer());
        DEF_SERIALIZERS.registerType(TypeToken.of(SfInGameScoreboardLanguage.class), new SfInGameScoreboardLanguageSerializer());
        DEF_SERIALIZERS.registerType(TypeToken.of(SfPostGameScoreboardLanguage.class), new SfPostGameScoreboardLanguageSerializer());

        DEF_SERIALIZERS.registerType(TypeToken.of(SfMessagesLanguage.class), new SfMessagesLanguageSerializer());
        DEF_SERIALIZERS.registerType(TypeToken.of(SfLobbyMessagesLanguage.class), new SfLobbyMessagesLanguageSerializer());
        DEF_SERIALIZERS.registerType(TypeToken.of(SfGameMessagesLanguage.class), new SfGameMessagesLanguageSerializer());

        DEF_SERIALIZERS.registerType(TypeToken.of(CaptainConfig.class), new CaptainConfigSerializer());

    }

}
