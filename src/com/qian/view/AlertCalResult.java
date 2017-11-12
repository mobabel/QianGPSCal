/*
 * Created on 2005-5-17
 */
package com.qian.view;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Image;

/**
 * @author leelight
 * Written by leelight. All rights reserved by leelight.
 * @param title, message
 * Display an Alert with @message
 */
public class AlertCalResult extends Alert{
    
    public AlertCalResult(String title, String message){
        super(title);
        this.setTimeout(FOREVER);
        this.setType(AlertType.INFO);

        this.setString(message);
    }
}
