package org.hzeng.service;

import org.hzeng.model.Coordinate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by hzeng on 6/4/17.
 */

@Service
public class LocationService {

    HashMap<String, Coordinate> coordinateMap;

    LocationService(){
        coordinateMap = new HashMap<>();
    }

    public Coordinate getCoordinate(String id){
        return coordinateMap.get(id);
    }
}
