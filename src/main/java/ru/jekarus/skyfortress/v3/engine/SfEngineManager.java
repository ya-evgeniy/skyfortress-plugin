package ru.jekarus.skyfortress.v3.engine;

import lombok.Getter;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class SfEngineManager {

    private final SkyFortressPlugin plugin;

    @Getter private final CaptureEngine captureEngine;
    @Getter private final CheckCaptureEngine checkCaptureEngine;
    @Getter private final CastleDeathEngine castleDeathEngine;

    @Getter private final ResourcesEngine resourcesEngine;

    public SfEngineManager(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;

//        CheckCaptureEngine checkCaptureEngine = CheckCaptureEngine.getInstance();
//        checkCaptureEngine.start();

        this.captureEngine = new CaptureEngine(this.plugin);
        this.checkCaptureEngine = new CheckCaptureEngine(this.plugin, this.captureEngine);
        this.castleDeathEngine = new CastleDeathEngine(this.plugin);

        this.resourcesEngine = new ResourcesEngine(plugin, plugin.getResourceContainer());
    }

}
