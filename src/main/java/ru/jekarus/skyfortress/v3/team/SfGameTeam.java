package ru.jekarus.skyfortress.v3.team;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.text.format.TextColor;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

public class SfGameTeam extends SfTeam {

    @Getter @Setter private SfCastle castle;
    @Getter @Setter private DyeColor blockColor;

    public SfGameTeam(@NonNull String uniqueId, @NonNull TextColor color) {
        super(Type.GAME, uniqueId, color);
        this.blockColor = SfUtils.getDyeColorFromTextColor(color);
    }

}
