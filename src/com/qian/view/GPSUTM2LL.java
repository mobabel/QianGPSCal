package com.qian.view;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;

import com.qian.util.UIController;
import com.qian.model.GPSCalFunc;
import com.qian.model.Setting;


public class GPSUTM2LL extends Form implements CommandListener{
    private UIController controller;
    private GPSCalFunc gpscalfun;
    private AlertCalResult alertcalresult;
    private AlertError alerterror;
    
    private Command        okCommand= new Command("Convert",Command.OK,1);
    private Command        backCommand = new Command("Back",Command.BACK,2);
    private Command        helpCommand = new Command("Help",Command.HELP,3);
    private Command        clearCommand = new Command("Clear",Command.OK,3);
    
    private TextField EastField, NorthField, ZoneField;
    private ChoiceGroup NSList;

	public GPSUTM2LL(String title,UIController control){
		super(title);
    	controller=control;
    	this.setCommandListener(this);
    	
 
    	this.addCommand(okCommand);
    	this.addCommand(backCommand);
    	this.addCommand(helpCommand);
    	this.addCommand(clearCommand);
    	
    	EastField=new TextField("East: ","",18,TextField.DECIMAL);
    	NorthField=new TextField("North: ","",18,TextField.DECIMAL);
    	ZoneField=new TextField("Zone: ","",2,TextField.DECIMAL);
    	NSList = new ChoiceGroup("N/S:",Choice.EXCLUSIVE);
    	NSList.append("N",null);
    	NSList.append("S",null);
    	
    	
    	this.append(EastField);
    	this.append(NorthField);
    	this.append(ZoneField);
    	this.append(NSList);
    	

	}
	public void clear(){
		EastField.setString("");
		NorthField.setString("");
		ZoneField.setString("");
	    	}
	
       /*
	    * UTMToLL_Execute
	    *
	    * Begin to convert UTM to LL.
	    *
	    */
	private void  UTMToLL_Execute(String east, String north, String Zone, boolean bsouthhemi)
	    {                                  
	        double latlon[] = {0,0};
	        double x, y; 
	        int zone;
	        boolean southhemi;
	        
	        if ((east.length()==0)||(north.length()==0)) {
	            alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter a valid easting in the east and north field.");
	            controller.setCurrent(alerterror,this);
	            return;
	        }
	        
	        x = Double.valueOf(east).doubleValue() ;
	        y = Double.valueOf(north).doubleValue() ;

	        if (Zone.length()==0) {
	            alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter a valid UTM zone in the zone field.");
	            controller.setCurrent(alerterror,this);
	            return;
	        }
	        zone= Integer.parseInt(Zone);

	        if ((zone < 1) || (60 < zone)) {
	            alerterror = new AlertError(Setting.TITLE_AlertError, "The UTM zone you entered is out of range.  " +
		                   "Please enter a number in the range [1, 60].");
	            controller.setCurrent(alerterror,this);
	            return;
	        }
	        
	        southhemi = bsouthhemi;

	        gpscalfun = new GPSCalFunc();
	        gpscalfun.UTMXYToLatLon (x, y, zone, southhemi, latlon);
	        
	        /* Set the output controls.  */
	        this.CalAlert(gpscalfun.Rad2Deg (latlon[1]),gpscalfun.Rad2Deg (latlon[0]));
	        return;
	    }
	    
    private void CalAlert(double lon, double lat){
        Vector degreelon = gpscalfun.dec2deg(String.valueOf(lon));
        Vector degreemlon = gpscalfun.dec2degm(String.valueOf(lon));
        Vector degreelat = gpscalfun.dec2deg(String.valueOf(lat));
        Vector degreemlat = gpscalfun.dec2degm(String.valueOf(lat));
       
        String message = "Longitdue: "+lon+"\n"
        		+"Latitude: "+lat+"\n\n"
        		+"Longitdue: "+degreelon.elementAt(0)+"°"+degreelon.elementAt(1)+"'"+degreelon.elementAt(2)+"\""+"\n"
        		+"Latitude: "+degreelat.elementAt(0)+"°"+degreelat.elementAt(1)+"'"+degreelat.elementAt(2)+"\""+"\n\n"
        		+"Longitdue: "+degreemlon.elementAt(0)+"°"+degreemlon.elementAt(1)+"'"+"\n"
        		+"Latitude: "+degreemlat.elementAt(0)+"°"+degreemlat.elementAt(1)+"'"+"\n";
        
        alertcalresult = new AlertCalResult(Setting.TITLE_AlertCalResult, message);
        controller.setCurrent(alertcalresult,this);
    }
    

    	public void commandAction(Command command, Displayable disp){
    			if(command==backCommand){
    				controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_BACK);
    	        }
    			else if(command==okCommand){
    	            String east=EastField.getString();
    	            String north=NorthField.getString();
    	            String zone = ZoneField.getString();
    	            boolean bsouthhemi = NSList.isSelected(1);
    	            UTMToLL_Execute(east,north,zone, bsouthhemi);
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
