package com.niuzj.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class IndexController {

    private Logger logger = Logger.getLogger(IndexController.class.getName());

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/hello")
    public String hello(String name, @RequestHeader String airen, @RequestBody Map<String, Object> map){
        ServiceInstance localServiceInstance = discoveryClient.getLocalServiceInstance();
        logger.info("name=" + name);
        logger.info("map=" + map);
        logger.info("airen=" + airen);
        logger.info("/hello, host=" + localServiceInstance.getHost() + ",serviceId=" + localServiceInstance.getServiceId());
        return "http://www.baidu.com";
    }

    @RequestMapping("/uri")
    public String uri(@RequestBody Map<String, Object> map, HttpServletResponse response){
        logger.info("params=" + map);
        response.addHeader("Location", "http://www.baidu.com?" + map.get("name"));
        return "";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public String put(@PathVariable Integer id){
        logger.info(id.toString());
        return "success";
    }

}
