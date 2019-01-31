package com.niuzj.demo.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@RestController
public class IndexController {

    private Logger logger = Logger.getLogger(IndexController.class.getName());

    private Random random = new Random();

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/hello")
    @HystrixCommand
    public String hello(String name, @RequestHeader String airen, @RequestBody Map<String, Object> map){
        ServiceInstance localServiceInstance = discoveryClient.getLocalServiceInstance();
        logger.info("name=" + name);
        logger.info("map=" + map);
        logger.info("airen=" + airen);
        logger.info("/hello, host=" + localServiceInstance.getHost() + ",serviceId=" + localServiceInstance.getServiceId());
        return "http://www.baidu.com";
    }

    @RequestMapping("/uri")
    @HystrixCommand
    public String uri(@RequestBody Map<String, Object> map, HttpServletResponse response){
        logger.info("params=" + map);
        response.addHeader("Location", "http://www.baidu.com?" + map.get("name"));
        return "";
    }

    //1600-2100
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    @HystrixCommand
    public String put(@PathVariable Integer id){
        logger.info(id.toString());
        //随机休眠1800到3000毫秒,大于2000毫秒时会发生熔断
        int i = random.nextInt(500) + 1600;
        logger.info(String.valueOf(i));
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
        return "success";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @HystrixCommand
    public String delete(@PathVariable Integer id){
        logger.info(id.toString());
        return "success";
    }

}
