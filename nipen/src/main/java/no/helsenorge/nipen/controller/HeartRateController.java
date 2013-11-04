package no.helsenorge.nipen.controller;

import no.helsenorge.nipen.parser.HeartRateJsonParser;
import no.helsenorge.nipen.service.HeartRateService;
import no.helsenorge.nipen.model.HeartRate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HeartRateController {
    private ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    private HeartRateService heartRateService = (HeartRateService)context.getBean("heartRateService");

    @RequestMapping(value="/api/human/heart_rates", method =  RequestMethod.GET)
    @ResponseBody
    public List<HeartRate> getHeartRates() {
        return heartRateService.getHeartRates();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/api/human/heart_rate", method = RequestMethod.POST)
    public void addHeartRate(@RequestBody String jsonHeartRate) {
        heartRateService.insertHeartRate(new HeartRateJsonParser(jsonHeartRate).toHeartRate());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/api/human/heart_rate/reset", method = RequestMethod.POST)
    public void resetHeartRates() {
        heartRateService.deleteHeartRates();
    }

    // For testing
    @RequestMapping(value="/api/human/test", method =  RequestMethod.GET)
    @ResponseBody
    public String getHello() {
        return "Hello World!";
    }
}
