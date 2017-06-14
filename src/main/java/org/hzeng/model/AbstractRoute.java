package org.hzeng.model;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRoute implements Route{

    private String id;

    private String routeFileName;

    private double latCorrection; // correction for Latitute
    private double lngCorrection; // correction for Longitude

    protected double timeZero;
    protected double speed;
    protected double timeOfRoundJourney;

    protected List<List<Double>> route;
    protected List<Double> cumDist;
    protected double lengthOfRoute;

    public AbstractRoute(String id, String routeFileName) {
        this.id = id;
        this.routeFileName = routeFileName;
        this.latCorrection = -0.0002;
        this.lngCorrection = 0.0;

        readRouteFromGPX();
        cumDistance();
    }

    private void readRouteFromGPX(){
        route = new ArrayList<>();

        try {
//            ClassPathA
//            ApplicationContext ctx = new ClassPathXmlApplicationContext();
            Resource routeResource =  new ClassPathResource(routeFileName);
//            File file = resource.getInputStream();
            InputStream xmlInputStrem = routeResource.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlInputStrem);

            doc.getDocumentElement().normalize();

            NodeList coordList = doc.getElementsByTagName("gpxx:rpt");

            int n = coordList.getLength();
            for (int i = 0; i < n; i++) {

                Node node = coordList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // System.out.print("\nlat :" + element.getAttribute("lat"));
                    // System.out.print("\nlon :" + element.getAttribute("lon"));
                    List<Double> coord = new ArrayList<>();
                    coord.add(Double.valueOf(element.getAttribute("lat")));
                    coord.add(Double.valueOf(element.getAttribute("lon")));
                    route.add(coord);
                }
            }
            // System.out.println(result);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double distanceCoordinate(List<Double> coord1, List<Double> coord2){
        double REARTH = 3958.8; // Radius of earth in mile
        double lat1 = coord1.get(0) * Math.PI / 180.0;
        double lat2 = coord2.get(0) * Math.PI / 180.0;
        double dLat = (coord2.get(0) - coord1.get(0)) * Math.PI / 180.0;
        double dLon = (coord2.get(1) - coord1.get(1)) * Math.PI / 180.0;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = REARTH * c;
        // System.out.println(d);
        return d;
    }

    public void setCoordinateCorrection(double latCorrection, double lngCorrection){
        this.latCorrection = latCorrection;
        this.lngCorrection = lngCorrection;
    }


    private void cumDistance(){
        cumDist = new ArrayList<>();
        cumDist.add(0.0);
        double cumsum = 0.0;
        for (int i = 1; i < route.size(); i++){
            cumsum += distanceCoordinate(route.get(i), route.get(i - 1));
            cumDist.add(cumsum);
        }
        lengthOfRoute = cumDist.get(cumDist.size() - 1);
    }

    protected Coordinate distFromRoutePoint(int prevInd, double remainDist){
        if (remainDist < 0.0001)
            return new Coordinate(route.get(prevInd).get(0) + latCorrection, route.get(prevInd).get(1) + lngCorrection);

        double distSection = cumDist.get(prevInd + 1) - cumDist.get(prevInd);
        double r = remainDist / distSection;
        double lat = route.get(prevInd).get(0) + r * (route.get(prevInd + 1).get(0) - route.get(prevInd).get(0));
        double lng = route.get(prevInd).get(1) + r * (route.get(prevInd + 1).get(1) - route.get(prevInd).get(1));
        return new Coordinate(lat + latCorrection, lng + lngCorrection);
    }

    public String getId(){
        return this.id;
    }
}
