package com.maryam.memorableplaces;

import java.io.Serializable;

public class LocationSerializable implements Serializable {
    protected double latitude;
    protected double longitude;
    public LocationSerializable(double lat, double lon){
        latitude=lat;
        longitude=lon;
    }
}
