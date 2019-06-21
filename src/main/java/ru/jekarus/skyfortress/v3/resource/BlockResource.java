package ru.jekarus.skyfortress.v3.resource;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Ignore;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.block.BlockState;

public class BlockResource extends SfResource {

    @Getter @Setter private Options options = new Options();

    @Getter @Setter private BlockState block;

    @OptionalValue @ConfigPath("while_wait_block")
    @Getter @Setter private BlockState whileWaitBlock;

    @OptionalValue @ConfigPath("bottom_block")
    @Getter @Setter private BlockState bottomBlock;

    @Ignore
    @Getter @Setter private boolean runned = true;

    public static class Options extends SfResource.Options {

        @OptionalValue @ConfigPath("use_bottom_block")
        @Getter private boolean useBottomBlock;

    }

}
