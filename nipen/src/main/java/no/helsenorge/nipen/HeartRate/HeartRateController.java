package no.helsenorge.nipen.HeartRate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HeartRateController {
    private ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    private HeartRateService heartRateService = (HeartRateServiceImplDB)context.getBean("heartRateService");

    @RequestMapping(value="/api/human/heart_rates", method =  RequestMethod.GET)
    @ResponseBody
    public List<HeartRate> getHeartRates(@RequestParam(value="id", required=false, defaultValue="0") Integer userId) {
        return heartRateService.getHeartRates();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/api/human/heart_rate", method = RequestMethod.POST)
    public void addHeartRate(@RequestBody String jsonHeartRate) {
        heartRateService.insertHeartRate(new HeartRateJsonParser(jsonHeartRate).toHeartRate());
    }

    // For testing
    @RequestMapping(value="/api/human/test", method =  RequestMethod.GET)
    @ResponseBody
    public String getHello() {
        return "Hello World!";
    }
}
