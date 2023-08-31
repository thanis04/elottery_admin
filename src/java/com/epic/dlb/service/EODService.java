/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.report.service.WinningFileReportService;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/**
 *
 * @author kasun_n
 */

@Component("eODService")
public class EODService{
    
//   @Autowired
//    private TaskScheduler scheduler;
   
   @Autowired
   private WinningFileReportService winningFileReportService;


    public void runTasks() {
       try {
           // generate prize pay file
           winningFileReportService.generatePrize();
       } catch (SQLException ex) {
           Logger.getLogger(EODService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (FileNotFoundException ex) {
           Logger.getLogger(EODService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (UnsupportedEncodingException ex) {
           Logger.getLogger(EODService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (ParseException ex) {
           Logger.getLogger(EODService.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

}
