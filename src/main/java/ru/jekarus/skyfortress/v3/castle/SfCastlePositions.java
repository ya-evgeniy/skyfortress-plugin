package ru.jekarus.skyfortress.v3.castle;

import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.Collection;

public class SfCastlePositions {

    private SfLocation respawn;
    private SfLocation capture;
    private Collection<SfLocation> shops;

    public SfCastlePositions()
    {

    }

    public SfCastlePositions(SfLocation respawn, SfLocation capture, Collection<SfLocation> shops)
    {
        this.respawn = respawn;
        this.capture = capture;
        this.shops = shops;
    }

    public SfLocation getRespawn()
    {
        return this.respawn;
    }

    public void setRespawn(SfLocation respawn)
    {
        this.respawn = respawn;
    }

    public SfLocation getCapture()
    {
        return this.capture;
    }

    public void setCapture(SfLocation capture)
    {
        this.capture = capture;
    }

    public Collection<SfLocation> getShops()
    {
        return this.shops;
    }

    public void setShops(Collection<SfLocation> shops)
    {
        this.shops = shops;
    }
}
