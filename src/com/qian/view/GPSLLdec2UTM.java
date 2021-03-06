package com.qian.view;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

import com.qian.util.UIController;
import com.qian.model.GPSCalFunc;import com.qian.model.Setting;
;

public class GPSLLdec2UTM extends Form implements CommandListener{
    private UIController controller;
    private GPSCalFunc gpscalfun;
    private AlertCalResult alertcalresult;
    private AlertError alerterror;
    
    private Command        okCommand= new Command("Convert",Command.OK,1);
    private Command        backCommand = new Command("Back",Command.BACK,2);
    private Command        helpCommand = new Command("Help",Command.HELP,3);
    private Command        clearCommand = new Command("Clear",Command.OK,3);

    private TextField LondecField, LatdecField;

	public GPSLLdec2UTM(String title,UIController control){
		super(title);
    	controller=control;
    	this.setCommandListener(this);
    	
 
    	this.addCommand(okCommand);
    	this.addCommand(backCommand);
    	this.addCommand(helpCommand);
    	this.addCommand(clearCommand);
    	
    	LondecField=new TextField("Longitude: ","",10,TextField.DECIMAL);
    	LatdecField=new TextField("Latitude: ","",10,TextField.DECIMAL);
    	
    	this.append(LondecField);
    	this.append(LatdecField);

	}
	
	public void clear(){
		LondecField.setString("");
		LatdecField.setString("");
	    	}
    /*
	    * LLTOUTM_Execute
	    *
	    * Begin to convert LL to UTM.
	    *
	    */
	private void  LLToUTM_Execute (String londec, String latdec)
	    {
	        double xy[] = {0,0};
	        
            if((londec.length()==0)||(latdec.length()==0)){	
               alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter a valid longitude and latitude in the lon&lat field.");
               controller.setCurrent(alerterror,this);
	            	return;
	            }

	        double lon = Double.valueOf(londec).doubleValue() ;
	        double lat = Double.valueOf(latdec).doubleValue() ;

	        if ((lon < -180.0) || (180.0 <= lon)) {
	        	alerterror = new AlertError(Setting.TITLE_AlertError, "The longitude you entered is out of range.  \n" +
		                   "Please enter a number in the range [-180, 180).");
                controller.setCurrent(alerterror,this);
	            return;
	        }

	        if ((lat < -90.0) || (90.0 < lat)) {
	        	alerterror = new AlertError(Setting.TITLE_AlertError, "The latitude you entered is out of range.  \n" +
		                   "Please enter a number in the range [-90, 90].");
                controller.setCurrent(alerterror,this);
	            return;
	        }

	        // Compute the UTM zone.
	        double zoned = Math.floor ((lon + 180.0) / 6) + 1;
	        //System.out.print("zoned is "+zoned);
	        int zone= (int)zoned;
	        //System.out.println("zone is "+zone);
	        gpscalfun = new GPSCalFunc();
	        zone = gpscalfun.LatLonToUTMXY (gpscalfun.Deg2Rad (lat), gpscalfun.Deg2Rad (lon), zone, xy);

	        /* Set the output controls.  */
	        this.CalAlert(xy[0],xy[1],zone,gpscalfun.StringNS(lat));

	        return;
	    }
	    
    private void CalAlert(double x, double y, int zone, String NS){
        //String num = londec;
        //gpscalfun = new GPSCalFunc();
        //Vector degree = gpscalfun.dec2deg(num);
       
        String message = "East: "+x+"\n"
        		+"North: "+y+"\n"
        		+"Zone: "+zone+"\n"
        		+"N/S: "+NS+"\n";
        
        alertcalresult = new AlertCalResult(Setting.TITLE_AlertCalResult, message);
        controller.setCurrent(alertcalresult,this);
    }
    

    	public void commandAction(Command command, Displayable disp){
    			if(command==backCommand){
    				controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_BACK);
    	        }
    			else if(command==okCommand){
    	            String londec=LondecField.getString();
    	            String latdec=LatdecField.getString();
    	            LLToUTM_Execute(londec,latdec);
    	            //controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_OK);    	                    	            
    			}
    			else if(command==helpCommand){
    				controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_HELP);   
    			}
    			else if(command==clearCommand){
    				this.clear();
    			}
    	}

}
