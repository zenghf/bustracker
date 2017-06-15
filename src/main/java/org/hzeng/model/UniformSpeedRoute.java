package org.hzeng.model;

import java.util.Collections;

public class UniformSpeedRoute extends AbstractRoute {

    public UniformSpeedRoute(String id, String routeFileName, double speed, double timeZero) {
        super(id, routeFileName);
        this.speed = speed;
        this.timeZero = timeZero;
        this.timeOfRoundJourney = this.lengthOfRoute / speed;
    }

    public Coordinate getCoordinate(){
        double time = System.currentTimeMillis() / 1000.0 - timeZero;
        time = time - Math.floor(time / timeOfRoundJourney) * timeOfRoundJourney;

        double dist = time * speed;
        int ind = Collections.binarySearch(cumDist, dist);
        if (ind >= 0)
            return distFromRoutePoint(ind, 0.0);
        int prevInd = - ind - 1 - 1;
        double remainDist = dist - cumDist.get(prevInd);
        return distFromRoutePoint(prevInd, remainDist);
    }

}
