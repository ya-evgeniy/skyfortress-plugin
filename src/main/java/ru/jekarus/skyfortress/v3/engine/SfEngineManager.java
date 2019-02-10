package ru.jekarus.skyfortress.v3.engine;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class SfEngineManager {

    private final SkyFortressPlugin plugin;

    private final CaptureEngine captureEngine;
    private final CheckCaptureEngine checkCaptureEngine;
    private final CastleDeathEngine castleDeathEngine;

    private final ResourcesEngine resourcesEngine;

    public SfEngineManager(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;

//        CheckCaptureEngine checkCaptureEngine = CheckCaptureEngine.getInstance();
//        checkCaptureEngine.start();

        this.captureEngine = new CaptureEngine(this.plugin);
        this.checkCaptureEngine = new CheckCaptureEngine(this.plugin, this.captureEngine);
        this.castleDeathEngine = new CastleDeathEngine(this.plugin);

        this.resourcesEngine = new ResourcesEngine(this.plugin);
    }

    public CaptureEngine getCaptureEngine()
    {
        return this.captureEngine;
    }

    public CheckCaptureEngine getCheckCaptureEngine()
    {
        return this.checkCaptureEngine;
    }

    public CastleDeathEngine getCastleDeathEngine()
    {
        return this.castleDeathEngine;
    }

    public ResourcesEngine getResourcesEngine()
    {
        return this.resourcesEngine;
    }

}
