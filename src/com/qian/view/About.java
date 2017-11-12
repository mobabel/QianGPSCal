/*
 * Created on 2005-5-17
 */
package com.qian.view;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Image;

/**
 * @author Administrator
 * Written by leelight. All rights reserved by leelight.
 */
public class About extends Alert{
    
    private Image icon=null;
    private final static String message="This program is writed by leelight.\n"
        					            +"http://gpscal.easywms.com\n"
        					            +"Ich liebe meine Qian";
    
    public About(String title){
        super(title);
        setTimeout(FOREVER);

        try {
	        icon=Image.createImage("/images/icon_start.png");
	        setImage(icon);
	    } catch (java.io.IOException x) {
	    	icon=null;
			System.out.println("Load image error when initializing Exception happens:" + x.getMessage());
	    }
        this.setString(message);
    }
}
