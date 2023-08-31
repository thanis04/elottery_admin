/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.email.service;

import com.epic.dlb.dto.EmailDTO;
import com.epic.dlb.model.DlbWbEmailConfiguration;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.service.PasswordResetService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author nipuna_k
 */
@Component("emailService")
public class EmailService {
//    local url

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public String sendResetEmail(DlbWbSystemUser dlbWbSystemUser) {

        DlbWbEmailConfiguration configuration = (DlbWbEmailConfiguration) genericRepository.get(1, DlbWbEmailConfiguration.class);

        String resetUrl = passwordResetService.generateUrl(dlbWbSystemUser.getUsername(), "ACTIVE");

        Map<String, String> keyValuePairs = new HashMap<>();
        keyValuePairs.put("firstname", dlbWbSystemUser.getDlbWbEmployee().getName());
        keyValuePairs.put("url", resetUrl);

        String params = "{\n"
                + "    \"from\": \"no-reply@dlbsweep.com\",\n"
                + "    \"to\": \"" + dlbWbSystemUser.getDlbWbEmployee().getEmail() + "\",\n"
                + "    \"cc\": \"\",\n"
                + "    \"bcc\": \"\",\n"
                + "    \"subject\": \"Reset your password\",\n"
                + "    \"text\": \"New messages.\",\n"
                + "    \"templatePath\": \"password_reset_email\",\n"
                + "    \"keyValuePairs\": {\n"
                + "        \"firstname\": \"" + dlbWbSystemUser.getDlbWbEmployee().getName() + "\",\n"
                + "        \"url\": \"" + resetUrl + "\"\n"
                + "    }\n"
                + "}";

        return sendEmail1(params, configuration.getUrl());
    }

    @Transactional(rollbackFor = Exception.class)
    public String sendCreatePasswordEmail(DlbWbSystemUser dlbWbSystemUser) {

        DlbWbEmailConfiguration configuration = (DlbWbEmailConfiguration) genericRepository.get(1, DlbWbEmailConfiguration.class);

        String resetUrl = passwordResetService.generateUrl(dlbWbSystemUser.getUsername(), "STATIC");
        String params = "{\n"
                + "    \"from\": \"no-reply@dlbsweep.com\",\n"
                + "    \"to\": \"" + dlbWbSystemUser.getDlbWbEmployee().getEmail() + "\",\n"
                + "    \"cc\": \"\",\n"
                + "    \"bcc\": \"\",\n"
                + "    \"subject\": \"Create your password\",\n"
                + "    \"text\": \"New messages.\",\n"
                + "    \"templatePath\": \"create_password_email\",\n"
                + "    \"keyValuePairs\": {\n"
                + "        \"name\": \"" + dlbWbSystemUser.getDlbWbEmployee().getName() + "\",\n"
                + "        \"username\": \"" + dlbWbSystemUser.getUsername() + "\",\n"
                + "        \"url\": \"" + resetUrl + "\"\n"
                + "    }\n"
                + "}";

        return sendEmail1(params, configuration.getUrl());
    }

    @Transactional(rollbackFor = Exception.class)
    public String sendEmail(EmailDTO emailDTO, String url) {
        try {
            URI uri = new URI(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmailDTO> request = new HttpEntity<>(emailDTO, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println(response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Done";
    }

    @Transactional(rollbackFor = Exception.class)
    public String sendEmail1(String prams, String url) {
        try {
            System.out.println(url);
            System.out.println(prams);
            URL obj = new URL(url);
            HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");

            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(prams.getBytes());
            os.flush();
            os.close();

            int responseCode = postConnection.getResponseCode();

            System.out.println("POST Response Code :  " + responseCode);
            System.out.println("POST Response Message : " + postConnection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        postConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("POST NOT WORKED");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Done";
    }

}
