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
import javax.microedition.lcdui.TextField;

import com.qian.util.UIController;
import com.qian.model.GPSCalFunc;import com.qian.model.GPSLL2DisFunc;
import com.qian.model.Setting;
;

public class GPSLL2Dis extends Form implements CommandListener{
    private UIController controller;
    private GPSCalFunc gpscalfun;
    private GPSLL2DisFunc gpsll2disfunc;
    private AlertCalResult alertcalresult;
    private AlertError alerterror;
    
    private Command        okCommand= new Command("Convert",Command.OK,1);
    private Command        backCommand = new Command("Back",Command.BACK,2);
    private Command        helpCommand = new Command("Help",Command.HELP,3);
    private Command        clearCommand = new Command("Clear",Command.OK,3);

    private TextField LondecaField, LatdecaField, LondecbField, LatdecbField;

    
    private ChoiceGroup formatList, formulaList;
    private double distance,distHaversine,distCosineLaw,bearing,rhumbBrng,rhumbDist;
    private double[] Midpoint;
    
	public GPSLL2Dis(String title,UIController control){
		super(title);
    	controller=control;
    	this.setCommandListener(this);
        gpsll2disfunc = new GPSLL2DisFunc();
        gpscalfun = new GPSCalFunc();
 
    	this.addCommand(okCommand);
    	this.addCommand(backCommand);
    	this.addCommand(helpCommand);
    	this.addCommand(clearCommand);
    	
    	formatList = new ChoiceGroup("Format Choice:",Choice.EXCLUSIVE);
    	formatList.append("decimal(e.g. 40.7486)",null);
    	formatList.append("dd°mm'ss\"(e.g. 40 44 55)",null);
    	formatList.append("dd°mmmm'(e.g. 40 44.9)",null);
    	
    	LondecaField=new TextField("Longitude 1: ","",15,TextField.ANY);
    	LatdecaField=new TextField("Latitude 1: ","",15,TextField.ANY);
    	LondecbField=new TextField("Longitude 2: ","",15,TextField.ANY);
    	LatdecbField=new TextField("Latitude 2: ","",15,TextField.ANY);
    	
    	formulaList = new ChoiceGroup("Formula Choice:",Choice.EXCLUSIVE);
    	formulaList.append("Haversine",null);
    	formulaList.append("Cosine",null);
    	formulaList.append("Distance on rhumb line",null);
    	
    	this.append(formatList);
    	this.append(LondecaField);
    	this.append(LatdecaField);
    	this.append(LondecbField);
    	this.append(LatdecbField);
    	this.append(formulaList);
    	
	}
	
	public void clear(){
		LondecaField.setString("");
		LatdecaField.setString("");
		LondecbField.setString("");
		LatdecbField.setString("");		
//        TextField tfx;
//        for(int i=0;i<4;i++)
//        {
//            tfx= (TextField) get(i);
//            tfx.setString("");
//        }
	   }
    /*
	    * LLToDis_Execute
	    *
	    * Begin to cal the distance and bearing.
	    *
	    */
	private void  LLToDis_Execute (String londeca, String latdeca, String londecb, String latdecb)
	    {
	        double xy[] = {0,0};
	        
            if(!gpscalfun.isNumeric(londeca)||!gpscalfun.isNumeric(latdeca)||!gpscalfun.isNumeric(londecb)||!gpscalfun.isNumeric(latdecb)){	
                alerterror = new AlertError(Setting.TITLE_AlertError, "Please only enter valid number in the text field.");
                controller.setCurrent(alerterror,this);
 	            	return;
 	            }
	        		        
            if((londeca.length()==0)||(latdeca.length()==0)||(londecb.length()==0)||(latdecb.length()==0)){	
               alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter a valid longitude and latitude in the lon&lat field.");
               controller.setCurrent(alerterror,this);
	            	return;
	            }
	        double lona = Double.valueOf(londeca).doubleValue() ;
	        double lata = Double.valueOf(latdeca).doubleValue() ;
	        double lonb = Double.valueOf(londecb).doubleValue() ;
	        double latb = Double.valueOf(latdecb).doubleValue() ;
	        
	        if ((lona < -180.0) || (180.0 <= lona)||(lonb < -180.0) || (180.0 <= lonb)) {
	        	alerterror = new AlertError(Setting.TITLE_AlertError, "The longitude you entered is out of range.  \n" +
		                   "Please enter a number in the range [-180, 180).");
                controller.setCurrent(alerterror,this);
	            return;
	        }

	        if ((lata < -90.0) || (90.0 < lata)||(latb < -90.0) || (90.0 < latb)) {
	        	alerterror = new AlertError(Setting.TITLE_AlertError, "The latitude you entered is out of range.  \n" +
		                   "Please enter a number in the range [-90, 90].");
                controller.setCurrent(alerterror,this);
	            return;
	        }
	        

	        if(formulaList.isSelected(0)){
	        	distance = gpsll2disfunc.distHaversine(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb));
		        bearing = gpscalfun.Rad2Deg(gpsll2disfunc.bearing(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb)));
	        }
	        if(formulaList.isSelected(1)){
	        	distance = gpsll2disfunc.distCosineLaw(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb));
		        bearing = gpscalfun.Rad2Deg(gpsll2disfunc.bearing(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb)));
	        }
	        if(formulaList.isSelected(2)){
	        	bearing= gpscalfun.Rad2Deg(gpsll2disfunc.brngRhumb(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb)));	        
		        distance= gpsll2disfunc.distRhumb(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb));
	        }
	        

	        
	        Midpoint =  gpsll2disfunc.midPoint(gpsll2disfunc.LatLong(lata, lona), gpsll2disfunc.LatLong(latb, lonb));
//	        double[] a={lata, lona};
//	        double[] b={latb, lonb};
//	        double[] Midpoint =  gpsll2disfunc.midPoint(a, b);
	        
	        /* Set the output controls.  */
	        this.CalAlert(distance,bearing,Midpoint);
	        //this.CalAlert(distance,bearing,0,rhumbBrng, Midpoint);

	        return;
	    }
	
    private void CalAlert(double distance, double bearing, double[] Midpoint ){
    	Vector dgr =gpscalfun.dec2deg(Double.toString(bearing));
        String message = "Distance(km): "+distance+"\n"
        		+"Initial bearing: "+bearing
        		+"("+dgr.elementAt(0)+"°"+dgr.elementAt(1)+"'"+dgr.elementAt(2)+"\""+")"+"\n"
        		+"Midpoint(lat,lon): ("+Midpoint[0]+", "+Midpoint[1]+" )\n";
        
        alertcalresult = new AlertCalResult(Setting.TITLE_AlertCalResult, message);
        controller.setCurrent(alertcalresult,this);
        
    }
    

    	public void commandAction(Command command, Displayable disp){
    			if(command==backCommand){
    				controller.handleEvent(UIController.EventID.EVENT_GPSDec2Deg_BACK);
    	        }
    			else if(command==okCommand){
    	            String londeca=LondecaField.getString().trim();
    	            String latdeca=LatdecaField.getString().trim();
    	            String londecb=LondecbField.getString().trim();
    	            String latdecb=LatdecbField.getString().trim();
    	 	       if(formatList.isSelected(0)){

    	 	       }
    	 	       if(formatList.isSelected(1)){
     	 	    	  double temlondeca = gpscalfun.deg2dec(gpscalfun.splitdeg(londeca," ")[0],gpscalfun.splitdeg(londeca," ")[1],gpscalfun.splitdeg(londeca," ")[2]);
     	 	    	  double temlatdeca = gpscalfun.deg2dec(gpscalfun.splitdeg(latdeca," ")[0],gpscalfun.splitdeg(latdeca," ")[1],gpscalfun.splitdeg(latdeca," ")[2]);
     	 	    	  double temlondecb = gpscalfun.deg2dec(gpscalfun.splitdeg(londecb," ")[0],gpscalfun.splitdeg(londecb," ")[1],gpscalfun.splitdeg(londecb," ")[2]);
     	 	    	  double temlatdecb = gpscalfun.deg2dec(gpscalfun.splitdeg(latdecb," ")[0],gpscalfun.splitdeg(latdecb," ")[1],gpscalfun.splitdeg(londeca," ")[2]);
     	 	    	  londeca=Double.toString(temlondeca);
    	 	    	  latdeca=Double.toString(temlatdeca);
    	 	    	  londecb=Double.toString(temlondecb);
    	 	    	  latdecb=Double.toString(temlatdecb);
    	 	    	 //System.out.println(londeca+"  "+latdeca);
    	 	       }
    	 	       if(formatList.isSelected(2)){
      	 	    	  double temlondeca = gpscalfun.degm2dec(gpscalfun.splitdeg(londeca," ")[0],gpscalfun.splitdeg(londeca," ")[1]);
      	 	    	  double temlatdeca = gpscalfun.degm2dec(gpscalfun.splitdeg(latdeca," ")[0],gpscalfun.splitdeg(latdeca," ")[1]);
      	 	    	  double temlondecb = gpscalfun.degm2dec(gpscalfun.splitdeg(londecb," ")[0],gpscalfun.splitdeg(londecb," ")[1]);
      	 	    	  double temlatdecb = gpscalfun.degm2dec(gpscalfun.splitdeg(latdecb," ")[0],gpscalfun.splitdeg(latdecb," ")[1]);
      	 	    	  londeca=Double.toString(temlondeca);
     	 	    	  latdeca=Double.toString(temlatdeca);
     	 	    	  londecb=Double.toString(temlondecb);
     	 	    	  latdecb=Double.toString(temlatdecb);
     	 	    	 //System.out.println(londeca+"  "+latdeca);
     	 	       }
    	            LLToDis_Execute(londeca,latdeca, londecb,latdecb);
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
