package org.hzeng.controller;


import com.rabbitmq.client.*;
import org.hzeng.model.Coordinate;
import org.hzeng.model.RandomCoordinate;
import org.hzeng.model.UniformSpeedRoute;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by hzeng on 5/26/17.
 */
@RestController
public class BaltimoreCoordinateController {
    private static final String EXCHANGE_NAME = "BUS_LOCATION";

    RandomCoordinate baltimoreCoordinate;
    UniformSpeedRoute uniformSpeedRoute;

    String message;

    @Autowired
    public void setBaltimoreCoordinate(){
        baltimoreCoordinate = new RandomCoordinate("random", 39.315770, -76.610532, 0.05);
    }

    @Autowired
    public void setUniformSpeedRoute(){
        uniformSpeedRoute = new UniformSpeedRoute("uniformHomeToBWI","src/main/resources/static/route/homeToBWI.gpx", 0.3, 0.0);
    }

    @Autowired
    public void messageReceive() throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "random");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                BaltimoreCoordinateController.this.message = message;
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    @GetMapping("/baltimore")
    public Coordinate getCoordinate(){
        return baltimoreCoordinate.getCoordinate();
    }

    @GetMapping("/route/homeToBWI")
    public Coordinate homeToBWI(){
        return uniformSpeedRoute.getCoordinate();
    }

    @GetMapping("/route/hello")
    public String testRabbitMQ() {
        return message;
    }

}
