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

    Set<String> routeNames;

    public BusTracker(Channel channel) {
        this.channel = channel;
        routeNames = new HashSet<>();
        String[] rs = {"random"};
        for (String route : rs)
            routeNames.add(route);
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
}
