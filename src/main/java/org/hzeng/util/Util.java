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



    public Util(){}
}
