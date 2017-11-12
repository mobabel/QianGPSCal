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


public class GPSDeg2Degm extends Form implements CommandListener{
    private UIController controller;
    private GPSCalFunc gpscalfun;
    private AlertCalResult alertcalresult;
    private AlertError alerterror;
    private Command        okCommand= new Command("Convert",Command.OK,1);
    private Command        backCommand = new Command("Back",Command.BACK,2);
    private Command        cancelCommand = new Command("Cancel",Command.CANCEL, 3);
    private Command        helpCommand = new Command("Help",Command.HELP,3);
    private Command        clearCommand = new Command("Clear",Command.OK,3);
    

    private TextField DegField_d;
    private TextField DegField_m;
    private TextField DegField_s;

	public GPSDeg2Degm(String title,UIController control){
		super(title);
    	controller=control;
    	this.setCommandListener(this);
    	
 
    	this.addCommand(okCommand);
    	this.addCommand(backCommand);
    	//this.addCommand(cancelCommand);
    	this.addCommand(helpCommand);
    	this.addCommand(clearCommand);

    	DegField_d=new TextField("°","",5,TextField.DECIMAL);
    	DegField_m=new TextField("'","",2,TextField.DECIMAL);
    	DegField_s=new TextField("\"","",5,TextField.DECIMAL);
    	
    	this.append(DegField_d);
    	this.append(DegField_m);
    	this.append(DegField_s);

	}
	
	public void clear(){
		DegField_d.setString("");
		DegField_m.setString("");
		DegField_s.setString("");
	    	}
	
    private void CalAlert(String d, String m, String s){
        gpscalfun = new GPSCalFunc();
        Vector degm = gpscalfun.deg2degm(d, m, s);
        double num = gpscalfun.deg2dec(d, m, s);
        double dnum = gpscalfun.Deg2Rad(num) ;
        String message = degm.elementAt(0)+"°"+degm.elementAt(1)+"'"
        		+"\nRadians is "+dnum;
        alertcalresult = new AlertCalResult(Setting.TITLE_AlertCalResult, message);
        controller.setCurrent(alertcalresult,this);
    }
    
    	public void commandAction(Command command, Displayable disp){
    			if(command==backCommand){
    				controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_BACK);
    	        }
    			else if(command==okCommand){
    	            String deg_d=DegField_d.getString();
    	            String deg_m=DegField_m.getString();
    	            String deg_s=DegField_s.getString();
    	            if(deg_d.length()==0 || deg_m.length()==0 || deg_s.length()==0){	
    	                alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter a valid degree value in the dms field.");
    	                controller.setCurrent(alerterror,this);
    	 	            	return;
    	 	            }
    	            CalAlert(deg_d, deg_m, deg_s);
	               
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
