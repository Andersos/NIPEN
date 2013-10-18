package no.helsenorge.nipen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Controller
public class ResourceController {
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value="/resources/{dir}/{name}.{type}", method =  RequestMethod.GET)
    @ResponseBody
    public String getResource(@PathVariable(value="dir") String dir,
                              @PathVariable(value="name") String name,
                              @PathVariable(value="type") String type) {
        File file = new File(servletContext.getRealPath("/") + "WEB-INF/web-resources/" + dir);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(name + "." + type)) {
                    file = files[i];
                    break;
                }
            }
        }

        String output = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            output = sb.toString();

            br.close();
        }
        catch (Exception e) {
        }

        return output;
    }
}
