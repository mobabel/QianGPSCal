/*
 * Created on 2006-9-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.qian.util;
import com.qian.midlet.GPSCal;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.TextField;

import com.qian.model.Setting;
import com.qian.view.About;
import com.qian.view.GPSDec2Deg;
import com.qian.view.GPSFuncSel;
import com.qian.view.HelpDecDeg;

/**
 * @author Administrator
 */
public class UIController {
	private GPSCal gpscalMidlet;
	private Alert alert; 
	private About about;
	private HelpDecDeg helpdecdeg;
	private GPSFuncSel gpsfuncsel;
	private GPSDec2Deg gpsdec2deg;
	
	public UIController(GPSCal gpscal){
		gpscalMidlet=gpscal;
	}
	
	public void init(){
		//init GUI
		gpsfuncsel=new GPSFuncSel(Setting.TITLE_GPSFuncSel,this);
		about=new About(Setting.TITLE_ABOUT);
		setCurrent(gpsfuncsel);
	}
	
	public void setCurrent(Displayable disp){
		gpscalMidlet.setCurrent(disp);
    }
	public void setCurrent(Alert alert, Displayable disp){
		gpscalMidlet.setCurrent(alert, disp);
    }
	
	//Define Event ID inside Class
    public static class EventID{
        private EventID(){
        }
        
        public static final byte EVENT_SEL_OK=0;//sel ok
        public static final byte EVENT_EXIT=11;//exit
        
        //public static final byte EVENT_GPSDec2Deg_OK =1;//add
        public static final byte EVENT_GPSDec2Deg_HELP =2;//record
        public static final byte EVENT_GPSDec2Deg_BACK =3;//record
        
        
        public static final byte EVENT_DELETE =4;//delete
        
        public static final byte EVENT_VIEW_DETAIL =5;//check
        public static final byte EVENT_VIEW_BACK= 6;//return check
        
        public static final byte EVENT_EDIT=7;//edit
        public static final byte EVENT_EDIT_BACK=8;//edit return
        public static final byte EVENT_EDIT_SAVE=9;//save edit
        
        public static final byte EVENT_ABOUT=10;//about
        public static final byte EVENT_CLEAR=12;//save edit
    }
    
    //Event process
    public void handleEvent( int eventID){   
    	switch (eventID)
        {   
	        case EventID.EVENT_SEL_OK:
	        {		
	        	gpsdec2deg=new GPSDec2Deg(Setting.TITLE_GPSDec2Deg,this);
	        	this.setCurrent(gpsdec2deg);
	    	    break;
	        }
	        case EventID.EVENT_EXIT:
    	    {
    	    	gpscalMidlet.exit(false);
    	    	break;
    	    }
    	    case EventID.EVENT_VIEW_DETAIL:
    	    {

    	        //setCurrent(viewForm);
    	        break;
    	    }
    	    case EventID.EVENT_GPSDec2Deg_HELP:
    	    {
    	    	helpdecdeg = new HelpDecDeg(Setting.TITLE_HelpDecDeg);
    	    	setCurrent(helpdecdeg);
    	    	break;
    	    }
    	    case EventID.EVENT_GPSDec2Deg_BACK:
    	    case EventID.EVENT_VIEW_BACK:    
    	    case EventID.EVENT_EDIT_BACK:
    	    {
    	    	setCurrent(gpsfuncsel);
    	    	break;
    	    }
//    	    case EventID.EVENT_GPSDec2Deg_OK:
//    	    {
//    	        //setCurrent(list);
//    	        break;
//    	    }
    	    case EventID.EVENT_EDIT:
    	    {

    	        //setCurrent(editForm);
    	        break;
    	    }
    	    case EventID.EVENT_EDIT_SAVE:
    	    {
    	        //setCurrent(list);
    	        break;
    	    }
    	    case EventID.EVENT_DELETE:
    	    {

    	        //list.refresh(phoneRecords.getPhones());
    	        //setCurrent(list);
    	        break;
    	    }
    	    case EventID.EVENT_ABOUT:
    	    {
    	        setCurrent(about);
    	        break;
    	    }
         	default:
         	    break;
        }
    }
    
}
