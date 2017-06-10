package org.hzeng.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.hzeng.model.RandomCoordinate;
import org.hzeng.model.Route;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzeng on 6/5/17.
 */

@Service
public class SendLocationService {

    private static final String EXCHANGE_NAME = "BUS_LOCATION";

    SendLocationService() throws IOException, TimeoutException {
        Route route = new RandomCoordinate("random", 39.315770, -76.610532, 0.05);
        registerRoute(route);
    }

    public void registerRoute(final Route route) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // TODO check rabbitmq exchange exists or not

        final String routingKey = route.getId();


        Runnable runnable = new Runnable() {
            public void run() {
                // task to run goes here
                try {
//                    String message = new Date().toString();
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
        };

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }

}
