package com.haroonfazal.myapps.gpsnavigation;

import java.io.Serializable;

/**
 * Created by Haroon on 11/7/2017.
 */

public class SingleRow
{


    String name,vicinity,latitude,longitude;


    public SingleRow(String name, String vicinity, String latitude, String longitude) {
        this.name = name;
        this.vicinity = vicinity;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
