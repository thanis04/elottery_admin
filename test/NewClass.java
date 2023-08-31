
import com.epic.dlb.repository.JDBCConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kasun_n
 */
public class NewClass {

    public static void main(String ar[]) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
       Date now = new Date();
 
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        System.out.println(simpleDateformat.format(now));
 
        simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        System.out.println(simpleDateformat.format(now));
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
     


    }
    
    
     

}
