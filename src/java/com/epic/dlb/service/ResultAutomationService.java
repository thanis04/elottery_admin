/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author nipuna_k
 */
@Component("resultAutomationService")
public class ResultAutomationService {

    @Autowired
    private ResultService resultService;
    private static SimpleDateFormat dateFormat;

    private Integer count = 1;

    public void startAutomation() {
        System.out.println("Start Automation");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            resultService.automationStartEndLog("AUTOMATION_STARTED", "Automation Started");
            String url = "https://drawresult.api.dlb.lk/api/v1.0/" + dateFormat.format(new Date());
//            String url = "https://drawresult.api.dlb.lk/api/v1.0/2021-08-05";

            URI uri = new URI(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Vendor-Id", "epic");
            headers.set("API-Key", "5336ba8a-91f2-41df-9ccb-b0fa98f9bbfb");

            HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            String output = response.getBody();
            JSONParser parser = new JSONParser();
            JSONArray jSONArray = (JSONArray) parser.parse(output);
            
            resultService.automationStartEndLog("FETCHED_DATA", "Fetched Process Done");
            
            if (!output.contains("[]")) {
                resultService.automationStartEndLog("NOT EMPTY RESULT", "Not Empty Result");
                resultService.saveResultJson(output, 27);

            } else {
                resultService.automationStartEndLog("EMPTY RESULT", "Empty Result");
                System.out.println("Empty JSON");
            }

            if (output.contains("Pending")) {
                resultService.automationStartEndLog("PENDING RESULTS AVAILABLE", "Pending data availble");
                if (count < 6) {
                    TimeUnit.MINUTES.sleep(10);
                    System.out.println("Pending ->");
                    count = getCount() + 1;
                    setCount(count);
                    startAutomation();
                } else {
                    setCount(1);
                    System.out.println("Automation Completed");
                }
            } else {
                setCount(1);
                System.out.println("Automation Completed");
                resultService.automationStartEndLog("PENDING RESULTS NOT AVAILABLE", "Pending data not availble");
            }
            
            resultService.automationStartEndLog("AUTOMATION_ENDED", "End");
        } catch (Exception ex) {
            System.out.println("Error Triggered");
            Logger.getLogger(ResultAutomationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
    
//            Test URL
//            String url = "https://test.drawresult.api.dlb.lk/api/v1.0/" + dateFormat.format(new Date());
//            String url = "https://test.drawresult.api.dlb.lk/api/v1.0/2020-10-28";
//            Production URL
//            String url = "https://drawresult.api.dlb.lk/api/v1.0/2021-07-27";
    
//            Test URL
//            headers.set("API-Key", "b1eb12db-8988-458e-916d-1a141b39042e");

//            Production URL
    
}
