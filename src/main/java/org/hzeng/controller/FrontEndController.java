package org.hzeng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by hzeng on 5/26/17.
 */
@Controller
public class FrontEndController {

    @GetMapping("/view")
    public String viewBus(){

        return "viewBus_fun";
    }
}
