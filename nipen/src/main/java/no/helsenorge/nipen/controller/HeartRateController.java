package no.helsenorge.nipen.controller;

import no.helsenorge.nipen.models.HeartRate;
import no.helsenorge.nipen.service.HeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/human")
public class HeartRateController {

    /*private ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    private HeartRateService heartRateService = (HeartRateService)context.getBean("heartRateService");*/

    @Autowired
    private HeartRateService heartRateService;

    @RequestMapping(value = "heart_rates", method = RequestMethod.GET)
    @ResponseBody
    public List<HeartRate> getHeartRates() {
        return heartRateService.getHeartRates();
    }

    /*public void addHeartRate(@RequestBody String jsonHeartRate) {
        heartRateService.insertHeartRate(new HeartRateJsonParser(jsonHeartRate).toHeartRate());
    }*/

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "heart_rate",
            method = RequestMethod.POST)
    public void addHeartRate(@RequestBody HeartRate heartRate) {
        heartRateService.insertHeartRate(heartRate);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "heart_rate_test",
            method = RequestMethod.POST)
    public void addHeartRate(@RequestBody String heartRate) {
        //heartRateService.insertHeartRate(heartRate);
        System.out.println(heartRate);
    }

    // For testing
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String getHello() {
        System.out.println("serving home");
        return "hello";
    }
}
