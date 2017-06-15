package org.hzeng.model;


import com.rabbitmq.client.Channel;
import org.springframework.core.env.Environment;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hzeng on 6/10/17.
 */
public class BusTracker {

    Channel channel;

    String consumerTag;

    String queueName;

    Set<String> routeNames;

    public BusTracker(Channel channel, String queueName) {
        this.channel = channel;
        this.queueName = queueName;
        routeNames = new HashSet<>();
    }

    public Channel getChannel() {
        return channel;
    }

    public Set<String> getRouteNames() {
        return routeNames;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public String getQueueName() {
        return queueName;
    }

    public void addRouteName(String name){
        routeNames.add(name);
    }
}
