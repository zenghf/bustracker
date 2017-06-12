package org.hzeng.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.hzeng.model.RandomCoordinate;
import org.hzeng.model.Route;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzeng on 6/5/17.
 */

@Service
public class BusSendLocationService {

    @Value("${bustracker.exchange-name}")
    String EXCHANGE_NAME; // = "BUS_LOCATION";

    Map<Route, Channel> routeChannelMap;

    BusSendLocationService() throws IOException, TimeoutException {
        routeChannelMap = new HashMap<>();
        Route route = new RandomCoordinate("random", 39.315770, -76.610532, 0.05);
        addRoute(route);
    }

    public void addRoute(Route route) throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        routeChannelMap.put(route, channel);
    }

    @Scheduled(fixedDelayString="${bustracker.location-update-interval}")
    public void sendLocation(){
        for (Route route : routeChannelMap.keySet()){
            Channel channel = routeChannelMap.get(route);
            String routingKey = route.getId();
            try {
                // String message = new Date().toString();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", route.getId());
                jsonObject.put("coordinate", route.getCoordinate().toJSONObject());
                String message = jsonObject.toJSONString();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
