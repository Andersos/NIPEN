package no.helsenorge.nipen.runnable;

import no.helsenorge.nipen.heartRate.HeartRate;
import no.helsenorge.nipen.heartRate.HeartRateServiceImplDB;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;

@ComponentScan("no.helsenorge.nipen")
@EnableAutoConfiguration
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");

        HeartRateServiceImplDB hr = (HeartRateServiceImplDB) context.getBean("heartRate");

        hr.insertHeartRate(new HeartRate(1, 1, new Timestamp(23103819), 80, "bmp"));
        //HeartRate heartRate = new HeartRate(1, 1, new Timestamp(2310293819), 80, "bmp");

        //org.springframework.boot.SpringApplication.run(App.class, args);
    }
}
