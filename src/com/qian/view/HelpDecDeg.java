/*
 * Created on 2006-9-4
 */
package com.qian.view;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Image;

/**
 * @author Administrator
 * Written by leelight. All rights reserved by leelight.
 */
public class HelpDecDeg extends Alert{
    
    private final static String message="It is easy to convert.\n"
        					            +"Isn't it?\n"
        					            +"Please use whitespace to separate the deg-min-sec value";
    
    public HelpDecDeg(String title){
        super(title);
        setTimeout(FOREVER);

        this.setString(message);
    }
}
