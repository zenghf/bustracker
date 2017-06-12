package org.hzeng.util;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by hzeng on 6/7/17.
 */
@Component
//@PropertySource("classpath:/application.yml")
public class Util {

    @Value("${bustracker.exchange-name}")
    String EXCHANGE_NAME; // = "BUS_LOCATION";
//
//    public Runnable generateTask(final SseEmitter emitter, String[] routingKeys){
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                ConnectionFactory factory = new ConnectionFactory();
//                factory.setHost("localhost");
//                try{
//                    Connection connection = factory.newConnection();
//                    Channel channel = connection.createChannel();
//
//
//                    String queueName = channel.queueDeclare().getQueue();
//
//                    for (String routingKey : routingKeys) {
//                        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
//                    }
//                    Consumer consumer = new DefaultConsumer(channel) {
//                        @Override
//                        public void handleDelivery(String consumerTag, Envelope envelope,
//                                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
//                            String message = new String(body, "UTF-8");
//                            //                BaltimoreCoordinateController.this.message = message;
//                            try {
//                                emitter.send(message);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                emitter.completeWithError(e);
//                                return;
//                            }
//                            System.out.println(" [-] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
//                        }
//                    };
//
//                    String consumerTag = channel.basicConsume(queueName, true, consumer);
//                    try {
//                        Thread.sleep(10000);
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    emitter.complete();
//                    channel.basicCancel(consumerTag);
//                    channel.close();
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        return runnable;
//    }

    public Util(){}
}
