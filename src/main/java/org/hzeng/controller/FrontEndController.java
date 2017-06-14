package org.hzeng.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.hzeng.config.BusTrackerSettings;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    FrontEndController(@Autowired BusTrackerSettings settings){
        defaultRouteNames = new ArrayList<>();
        for (BusTrackerSettings.RouteSetting r : settings.getRouteSettings()){
            if (r.isEnabled())
                defaultRouteNames.add(r.getId());
        }
    }

    @GetMapping("/view")
    public String viewBus(){

        return "viewBus_fun";
    }

    @GetMapping("/message")
    public String message(){
        return "testServerSendMessage";
    }

    @GetMapping("/testWS")
    public String testWS(Model model){
        model.addAttribute("defaultRouteNames", defaultRouteNames);
        return "testWS";
    }
}
