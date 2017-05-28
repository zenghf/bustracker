package org.hzeng.service;

import org.hzeng.model.Coordinate;

import java.util.Random;

/**
 * Created by hzeng on 5/26/17.
 */
public class RandomCoordinate {
    double centerLat;
    double centerLng;
    double variance;

    public RandomCoordinate(double centerLat, double centerLng, double variance) {
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
}
