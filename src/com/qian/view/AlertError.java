/*
 * Created on 2005-5-17
 */
package com.qian.view;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Image;

/**
 * @author Administrator
 * Written by leelight. All rights reserved by leelight.
 */
public class AlertError extends Alert{
    
    private Image icon=null;
    
    public AlertError(String title, String message){
        super(title);
        setTimeout(FOREVER);
        this.setType(AlertType.ERROR);
        
//        try {
//	        icon=Image.createImage("/icon/icon_about.png");
//	        setImage(icon);
//	    } catch (java.io.IOException x) {
//	    }
        this.setString(message);
    }
}
