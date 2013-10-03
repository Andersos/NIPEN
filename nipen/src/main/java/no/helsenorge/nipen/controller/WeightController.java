package no.helsenorge.nipen.controller;

import no.helsenorge.nipen.parser.WeightJsonParser;
import no.helsenorge.nipen.service.WeightService;
import no.helsenorge.nipen.model.Weight;
import no.helsenorge.nipen.service.WeightServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WeightController {
    private ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    private WeightService weightService = (WeightServiceImpl)context.getBean("weightService");

    @RequestMapping(value="/api/human/weights", method =  RequestMethod.GET)
    @ResponseBody
    public List<Weight> getWeights() {
        return weightService.getWeights();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/api/human/weight", method = RequestMethod.POST)
    public void addWeight(@RequestBody String jsonWeight) {
        weightService.insertWeight(new WeightJsonParser(jsonWeight).toWeight());
    }
}
