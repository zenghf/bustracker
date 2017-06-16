package org.hzeng.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.hzeng.config.BusTrackerSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzeng on 5/26/17.
 */
@Controller
public class FrontEndController {

    List<String> defaultRouteNames;

    String websocketServer;

    @Autowired
    FrontEndController(@Autowired BusTrackerSettings settings,
                       @Value("${server.host}") String host,
                       @Value("${server.port}") Integer port){
        defaultRouteNames = new ArrayList<>();
        for (BusTrackerSettings.RouteSetting r : settings.getRouteSettings()){
            if (r.isEnabled())
                defaultRouteNames.add(r.getId());
        }
        websocketServer = host + ":" + port;
    }

    @GetMapping({"/", "/index"})
    public String testWS(Model model){
        model.addAttribute("websocketServer", websocketServer);
        model.addAttribute("defaultRouteNames", defaultRouteNames);
        return "index";
    }
}
