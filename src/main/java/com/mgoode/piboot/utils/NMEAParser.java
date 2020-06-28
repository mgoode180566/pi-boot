package com.mgoode.piboot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NMEAParser {

    Logger log = LoggerFactory.getLogger(this.getClass());

    double speed = 0;
    double latitude = 0;
    double longitude = 0;

    public double getSpeed() {
        return speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean parse(String sentence) {
        String parts[];
        boolean result = true;
        if (sentence.startsWith("$GPVTG")) {
            parts = sentence.split("\\,", -1);
            if (parts[7].length() > 0) {
                speed = Double.parseDouble(parts[7]);
            } else result = false;
        }
        if (sentence.startsWith("$GPGGA")) {
            parts = sentence.split("\\,", -1);
            if ((parts[4].length()>0) && (parts[5].length()>0) && (parts[2].length()>0) && (parts[3].length()>0)) {
                latitude = latitudeToDecimal(parts[4], parts[5]);
                longitude = longitudeToDecimal(parts[2], parts[3]);
            } else result = false;
        }
        if (sentence.startsWith("$GPRMC")) {
            parts = sentence.split("\\,", -1);
            if ((parts[4].length()>0) && (parts[5].length()>0) && (parts[2].length()>0) && (parts[3].length()>0)) {
                latitude = latitudeToDecimal(parts[5], parts[6]);
                longitude = longitudeToDecimal(parts[3], parts[4]);
            } else result = false;
        }
        return result;
    }

    private double latitudeToDecimal(String lat, String NS) {

            int minutesPosition = lat.indexOf('.') - 2;
            double minutes = Double.parseDouble(lat.substring(minutesPosition));
            double decimalDegrees = Double.parseDouble(lat.substring(minutesPosition)) / 60.0f;

            double degree = Double.parseDouble(lat) - minutes;
            double wholeDegrees = (int) degree / 100;

            double latitudeDegrees = wholeDegrees + decimalDegrees;
            if (NS.startsWith("S")) {
                latitudeDegrees = -latitudeDegrees;
            }
            return latitudeDegrees;

    }

    private double longitudeToDecimal(String lon, String WE) {
        int minutesPosition = lon.indexOf('.') - 2;
        double minutes = Double.parseDouble(lon.substring(minutesPosition));
        double decimalDegrees = Double.parseDouble(lon.substring(minutesPosition)) / 60.0f;

        double degree = Double.parseDouble(lon) - minutes;
        double wholeDegrees = (int) degree / 100;

        double longitudeDegrees = wholeDegrees + decimalDegrees;
        if (WE.startsWith("W")) {
            longitudeDegrees = -longitudeDegrees;
        }
        return longitudeDegrees;
    }


}
