package com.noseplugapp.android.utils;

import com.google.android.gms.maps.model.LatLng;

/* Creating latlng implementation because:
        google's latlng has no default constructor
        Which causes an error with Firebase
        -John Blum, 3/25/2016
*/
public class NoseplugLatLng
{
    public Double latitude;
    public Double longitude;

    public NoseplugLatLng()
    {
        latitude = 0.0;
        longitude = 0.0;
    }

    public NoseplugLatLng(Double t, Double g)
    {
        latitude = t;
        longitude = g;
    }

    public NoseplugLatLng(LatLng google)
    {
        latitude = google.latitude;
        longitude = google.longitude;
    }

    public LatLng toGoogleLatLng()
    {
        return new LatLng(latitude, longitude);
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
}