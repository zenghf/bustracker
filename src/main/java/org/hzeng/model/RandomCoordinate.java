package org.hzeng.model;

import org.hzeng.model.Coordinate;

import java.util.Random;

/**
 * Created by hzeng on 5/26/17.
 */
public class RandomCoordinate implements Route{
    String id;
    double centerLat;
    double centerLng;
    double variance;

    public RandomCoordinate(String id, double centerLat, double centerLng, double variance) {
        this.id = id;
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.variance = variance;
    }

    public Coordinate getCoordinate(){
        Random random = new Random();
        double dLat = (random.nextDouble() * 2 - 1.0) * variance;
        double dLng = (random.nextDouble() * 2 - 1.0) * variance;
        return new Coordinate(centerLat + dLat, centerLng + dLng);
    }

    public String getId(){
        return id;
    }
}
