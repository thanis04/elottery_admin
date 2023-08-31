/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.util.common;

import java.util.ResourceBundle;

/**
 *
 * @author admin
 */
public class Configuration {

    private static ResourceBundle bundle = ResourceBundle.getBundle("com.epic.dlb.util.common.ConfigurationFile");

        public static String getConfiguration(String name) {
        return bundle.getString(name);
    }

}
