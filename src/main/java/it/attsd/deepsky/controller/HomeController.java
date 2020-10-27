package it.attsd.deepsky.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping(value = "/")
    public String index(){
        System.out.println("Home Page");
        return "index";
    }
	
//	@GetMapping(value = "/home")
//    public String home(Model model) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("project_name", "DeepSky Manager");
//        
//        model.addAttribute("name", "DeepSky Manager");
//
//        return "index";
//    }

}
