package org.hzeng.controller;


import org.hzeng.model.Coordinate;
import org.hzeng.model.RandomCoordinate;
import org.hzeng.model.UniformSpeedRoute;
import org.hzeng.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by hzeng on 5/26/17.
 */
@RestController
public class BaltimoreCoordinateController {

    @Autowired
    Util util;
    RandomCoordinate baltimoreCoordinate;
    UniformSpeedRoute uniformSpeedRoute;
    String message;


//    public void setUtil(Util util){
//        this.util = util;
//    }

    @Autowired
    public void setBaltimoreCoordinate(){
        baltimoreCoordinate = new RandomCoordinate("random", 39.315770, -76.610532, 0.05);
    }

    @Autowired
    public void setUniformSpeedRoute(){
        uniformSpeedRoute = new UniformSpeedRoute("uniformHomeToBWI", "route/homeToBWI.gpx", 0.3, 0.0);
    }



    // @Autowired
//    public void messageReceive() throws IOException, TimeoutException{
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//
//        String queueName = channel.queueDeclare().getQueue();
//
//        channel.queueBind(queueName, EXCHANGE_NAME, "random");
//
//        Consumer consumer = new DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope,
//                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
//                String message = new String(body, "UTF-8");
//                BaltimoreCoordinateController.this.message = message;
//                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
//            }
//        };
//
//        channel.basicConsume(queueName, true, consumer);
//    }

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

//    @GetMapping("/sendMessage")
//    public ResponseEntity<SseEmitter> sendMessage(){
//            final SseEmitter emitter = new SseEmitter();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        emitter.send(new Date().toString());
//                    } catch (Exception e){
//                        e.printStackTrace();
//                        emitter.completeWithError(e);
//                        return;
//                    }
//                }
//            };
//        ScheduledExecutorService service = Executors
//                .newSingleThreadScheduledExecutor();
//        ScheduledFuture<?> future = service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
//
//        return new ResponseEntity(emitter, HttpStatus.OK);
//    }

//    @GetMapping("/sendMessage")
//    public ResponseEntity<SseEmitter> sendMessage() throws IOException, TimeoutException, InterruptedException{
//
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        final SseEmitter emitter = new SseEmitter();
//        String[] routingKeys = {"random"};
////
//        Runnable runnable = util.generateTask(emitter, routingKeys);
//
//        service.submit(runnable);
//        return new ResponseEntity(emitter, HttpStatus.OK);
//    }
}

