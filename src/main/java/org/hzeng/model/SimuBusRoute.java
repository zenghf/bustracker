package org.hzeng.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimuBusRoute extends AbstractRoute{

    private double timeStopAtStation;
    private ArrayList<Integer> stationIndexList;
    private boolean[] isStation;
    private ArrayList<Double> cumTime;

    public SimuBusRoute(String id, String routeFileName, double speed, double timeZero, double timeStopAtStation, double minDistanceBetweenStation) {

        super(id, routeFileName);
        this.speed = speed;
        this.timeZero = timeZero;
        this.timeStopAtStation = timeStopAtStation;

        // create station index list
        stationIndexList = new ArrayList<>();
        int n = cumDist.size();
        double prevDist = 0.0;
        for (int i = 1; i < n - 1; i++){
            if (cumDist.get(i) - prevDist >= minDistanceBetweenStation){
                stationIndexList.add(i);
                prevDist = cumDist.get(i);
            }
        }
        if (this.lengthOfRoute - prevDist < minDistanceBetweenStation * 0.3)
            stationIndexList.set(stationIndexList.size() - 1, n - 1);
        else
            stationIndexList.add(n - 1);
        int nStation = stationIndexList.size();
        isStation = new boolean[n];
        for (int i = 0; i < nStation; i++)
            isStation[stationIndexList.get(i)] = true;
        this.timeOfRoundJourney = lengthOfRoute / speed + nStation * timeStopAtStation;

        cumTime = new ArrayList<>();
        cumTime.add(0.0);
        int nPrevStations = 0;
        for (int i = 1; i < n; i++){
            cumTime.add(cumDist.get(i) / speed + nPrevStations * timeStopAtStation);
            if (isStation[i])
                nPrevStations++;
        }
    }

    public Coordinate getCoordinate(){
        double time = System.currentTimeMillis() / 1000.0 - timeZero;
        time = time - Math.floor(time / timeOfRoundJourney) * timeOfRoundJourney;
        int ind = Collections.binarySearch(cumTime, time);
        if (ind >= 0)
            return distFromRoutePoint(ind, 0.0);
        int prevInd = - ind - 1 - 1;
        double remainTime = time - cumTime.get(prevInd);
        if (isStation[prevInd])
            remainTime = remainTime > timeStopAtStation ? remainTime - timeStopAtStation : 0.0;
        if (prevInd == cumTime.size() - 1)
            remainTime = 0.0;
        return distFromRoutePoint(prevInd, remainTime * speed);
    }
}
