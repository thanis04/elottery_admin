/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.controller;

import javax.servlet.http.HttpSession;
//import org.hibernate.mapping.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nipuna_k
 */
@Controller
@RequestMapping("test")
public class TestController {

    @RequestMapping("/show_page.htm")
    public String showPage(HttpSession session, Model model) {

        return "pages/test";
    }

    @RequestMapping(value = "save.htm", method = RequestMethod.POST)
    @ResponseBody
    public String saveRecord(HttpSession session,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {

        // Fetch the service account key JSON file contents
//        FileInputStream serviceAccount;
//        try {
//            serviceAccount = new FileInputStream("path/to/serviceAccount.json");
//
//            // Initialize the app with a service account, granting admin privileges
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredential.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://testproject-ff23b.firebaseio.com/")
//                    .build();
//            FirebaseApp.initializeApp(options);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("server/saving-data/fireblog");
//        DatabaseReference usersRef = ref.child("users");
//
//        Map<String, Test> test = new HashMap<>();
//        test.put("akf8lV2yR7qID7McJvRb", new Test("aaaaaaaa", image));
        return "/show_page.htm";
    }

}
