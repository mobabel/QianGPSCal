package com.qian.view;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.qian.model.GPSCalFunc;
import com.qian.model.Setting;
import com.qian.util.UIController;


public class GPSDec2Degm extends Form implements CommandListener{
    private UIController controller;
    private GPSCalFunc gpscalfun;
    private AlertCalResult alertcalresult;
    private AlertError alerterror;
    private Command        okCommand= new Command("Convert",Command.OK,1);
    private Command        backCommand = new Command("Back",Command.BACK,2);
    private Command        cancelCommand = new Command("Cancel",Command.CANCEL, 3);
    private Command        helpCommand = new Command("Help",Command.HELP,3);
    private Command        clearCommand = new Command("Clear",Command.OK,3);

    private TextField DecField;

	public GPSDec2Degm(String title,UIController control){
		super(title);
    	controller=control;
    	this.setCommandListener(this);
    	
 
    	this.addCommand(okCommand);
    	this.addCommand(backCommand);
    	//this.addCommand(cancelCommand);
    	this.addCommand(helpCommand);
    	this.addCommand(clearCommand);
    	
    	DecField=new TextField("d.dddddd","",8,TextField.DECIMAL);
    	
    	this.append(DecField); 	

	}
	public void clear(){
		DecField.setString("");
	    	}
	
    private void CalAlert(String dec){
        String num = dec;
        gpscalfun = new GPSCalFunc();
        Vector degree = gpscalfun.dec2degm(num);
        double dnum = gpscalfun.Deg2Rad(Double.valueOf(num).doubleValue()) ;
        String message = degree.elementAt(0)+"°"+degree.elementAt(1)+"'"
        		+"\nRadians is "+dnum;
        alertcalresult = new AlertCalResult(Setting.TITLE_AlertCalResult, message);
        controller.setCurrent(alertcalresult,this);
    }
    
    	public void commandAction(Command command, Displayable disp){
    			if(command==backCommand){
    				controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_BACK);
    	        }
    			else if(command==okCommand){
    	            String dec=DecField.getString();
    	            if((dec.length()==0)){	
    	                alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter a valid decimal value in the decimal field.");
    	                controller.setCurrent(alerterror,this);
    	 	            	return;
    	 	            }
    	            CalAlert(dec);
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
