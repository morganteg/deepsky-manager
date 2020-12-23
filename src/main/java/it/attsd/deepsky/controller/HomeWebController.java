package it.attsd.deepsky.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeWebController {
	
	@GetMapping(value = "/")
    public String index(){
        
        return "index";
    }

}
