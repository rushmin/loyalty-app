package org.wso2.sample.loyalty.servlet;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import javax.servlet.ServletContextEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ServletContextListener
 */
public class ServletContextListener implements javax.servlet.ServletContextListener{

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        InputStream configStream = this.getClass().getResourceAsStream("/config.json");

        JSONParser jsonParser = new JSONParser(JSONParser.MODE_PERMISSIVE);

        try {

            JSONObject parsedConfig = (JSONObject) jsonParser.parse(configStream);

            Map<String, String> configs = new HashMap<String, String>();

            for(Map.Entry<String, Object> config : parsedConfig.entrySet()){
                configs.put(config.getKey(), (String) config.getValue());
            }
            servletContextEvent.getServletContext().setAttribute("loyalty.configs", configs);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
