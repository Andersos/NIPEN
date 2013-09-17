package no.helsenorge.nipen.heartRate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HeartRateController {

    /*@Autowired
    private HeartRateService heartRateService;*/
    private HeartRateService heartRateService = new HeartRateServiceImplDB();

    @RequestMapping(value="/api/human/heart_rates", method =  RequestMethod.GET)
    @ResponseBody
    public List<HeartRate> getHeartRates(@RequestParam(value="id", required=false, defaultValue="0") Integer userId) {
        return heartRateService.getHeartRates();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/api/human/heart_rate", method = RequestMethod.POST)
    @ResponseBody
    public String addHeartRate(/*@ModelAttribute*//*@RequestBody HeartRate snooze*/){
        //heartRateService.insertHeartRate(snooze);
        return "Yuppi !!";
    }

    // For testing
    @RequestMapping(value="/api/human/test", method =  RequestMethod.GET)
    @ResponseBody
    public String getHello() {
        return "Hello World!";
    }
}
