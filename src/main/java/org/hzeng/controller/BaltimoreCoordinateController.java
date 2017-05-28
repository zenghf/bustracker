package org.hzeng.controller;

import org.hzeng.model.Coordinate;
import org.hzeng.service.RandomCoordinate;
import org.hzeng.service.UniformSpeedRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by hzeng on 5/26/17.
 */
@RestController
public class BaltimoreCoordinateController {

    RandomCoordinate baltimoreCoordinate;
    UniformSpeedRoute uniformSpeedRoute;

    @Autowired
    public void setBaltimoreCoordinate(){
        baltimoreCoordinate = new RandomCoordinate(39.315770, -76.610532, 0.05);
    }

    @Autowired
    public void setUniformSpeedRoute(){
        uniformSpeedRoute = new UniformSpeedRoute("src/main/resources/static/route/homeToBWI.gpx", 0.3, 0.0);
    }

    @GetMapping("/baltimore")
    public Coordinate getCoordinate(){
        return baltimoreCoordinate.getCoordinate();
    }

    @GetMapping("/route/homeToBWI")
    public Coordinate homeToBWI(){
        return uniformSpeedRoute.getCoordinate();
    }

}
