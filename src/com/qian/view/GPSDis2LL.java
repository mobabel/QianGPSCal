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

public class GPSDis2LL extends Form implements CommandListener{
    private UIController controller;
    private GPSCalFunc gpscalfun;
    private GPSLL2DisFunc gpsll2disfunc;
    private AlertCalResult alertcalresult;
    private AlertError alerterror;
    
    private Command        okCommand= new Command("Convert",Command.OK,1);
    private Command        backCommand = new Command("Back",Command.BACK,2);
    private Command        helpCommand = new Command("Help",Command.HELP,3);
    private Command        clearCommand = new Command("Clear",Command.OK,3);

    private TextField LondecaField, LatdecaField,BrngField,DistField;

    
    private ChoiceGroup formatList, formulaList;
    private double finalbearing;
    private double[] latlonb;
    
	public GPSDis2LL(String title,UIController control){
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
    	BrngField=new TextField("Bearing : ","",15,TextField.ANY);
    	DistField=new TextField("Distance(km): ","",10,TextField.NUMERIC);
    	
    	formulaList = new ChoiceGroup("Formula Choice:",Choice.EXCLUSIVE);
    	formulaList.append("Great circle",null);
    	formulaList.append("Rhumb line",null);
    	
    	this.append(formatList);
    	this.append(LondecaField);
    	this.append(LatdecaField);
    	this.append(BrngField);
    	this.append(DistField);
    	this.append(formulaList);
    	
	}
	
	public void clear(){
		LondecaField.setString("");
		LatdecaField.setString("");
		BrngField.setString("");
		DistField.setString("");		
//        TextField tfx;
//        for(int i=0;i<4;i++)
//        {
//            tfx= (TextField) get(i);
//            tfx.setString("");
//        }
	   }
    /*
	    * DisToLL_Execute
	    *
	    * Begin to cal the Ll and finalbearing.
	    *
	    */
	private void  DisToLL_Execute (String londeca, String latdeca, String brngs, String dists)
	    {
	        double xy[] = {0,0};
	        
            if(!gpscalfun.isNumeric(londeca)||!gpscalfun.isNumeric(latdeca)||!gpscalfun.isNumeric(brngs)){	
                alerterror = new AlertError(Setting.TITLE_AlertError, "Please only enter valid number in the text field.");
                controller.setCurrent(alerterror,this);
 	            	return;
 	            }
            
            if((londeca.length()==0)||(latdeca.length()==0)||(brngs.length()==0)||(dists.length()==0)){	
               alerterror = new AlertError(Setting.TITLE_AlertError, "Please enter valid longitude&latitude, bearing and distance in the text field.");
               controller.setCurrent(alerterror,this);
	            	return;
	            }
            
	        double lona = Double.valueOf(londeca).doubleValue() ;
	        double lata = Double.valueOf(latdeca).doubleValue() ;
	        double brng = Double.valueOf(brngs).doubleValue() ;
	        double dist = Double.valueOf(dists).doubleValue() ;
	        
	        if ((lona < -180.0) || (180.0 <= lona)) {
	        	alerterror = new AlertError(Setting.TITLE_AlertError, "The longitude you entered is out of range.  \n" +
		                   "Please enter a number in the range [-180, 180).");
                controller.setCurrent(alerterror,this);
	            return;
	        }

	        if ((lata < -90.0) || (90.0 < lata)) {
	        	alerterror = new AlertError(Setting.TITLE_AlertError, "The latitude you entered is out of range.  \n" +
		                   "Please enter a number in the range [-90, 90].");
                controller.setCurrent(alerterror,this);
	            return;
	        }
	        

	        if(formulaList.isSelected(0)){
	        	latlonb = gpsll2disfunc.destPoint(gpsll2disfunc.LatLong(lata, lona), brng, dist);
	        	finalbearing = gpscalfun.Rad2Deg(gpsll2disfunc.finalBrng(gpsll2disfunc.LatLong(lata, lona),latlonb, brng, dist));
	        }
	        if(formulaList.isSelected(1)){
	        	latlonb = gpsll2disfunc.destPointRhumb(gpsll2disfunc.LatLong(lata, lona), brng, dist);
	        	finalbearing =gpscalfun.Rad2Deg(gpsll2disfunc.finalBrng(gpsll2disfunc.LatLong(lata, lona),latlonb, brng, dist));
	        }
	   
	        
	        /* Set the output controls.  */
	        this.CalAlert(latlonb,finalbearing);
	        //this.CalAlert(distance,bearing,0,rhumbBrng, Midpoint);

	        return;
	    }
	
    private void CalAlert(double[] latlonb, double finalbearing){
        Vector dgr =gpscalfun.dec2deg(Double.toString(finalbearing));
        Vector dgrla = gpscalfun.dec2deg(Double.toString(gpscalfun.Rad2Deg(latlonb[0])));
        Vector dgrlo = gpscalfun.dec2deg(Double.toString(gpscalfun.Rad2Deg(latlonb[1])));
        String message = "Destination(lat,lon): ("+gpscalfun.Rad2Deg(latlonb[0])+", "+gpscalfun.Rad2Deg(latlonb[1])+" )"
                          +"("+dgrla.elementAt(0)+"°"+dgrla.elementAt(1)+"'"+dgrla.elementAt(2)+"\","
                          +dgrlo.elementAt(0)+"°"+dgrlo.elementAt(1)+"'"+dgrlo.elementAt(2)+"\")"+"\n"
                          +"Final bearing: "+finalbearing
                          +"("+dgr.elementAt(0)+"°"+dgr.elementAt(1)+"'"+dgr.elementAt(2)+"\""+")"+"\n";
        
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
    	            String brng=BrngField.getString().trim();
    	            String dist=DistField.getString().trim();
    	 	       if(formatList.isSelected(0)){

    	 	       }
    	 	       if(formatList.isSelected(1)){
     	 	    	  double temlondeca = gpscalfun.deg2dec(gpscalfun.splitdeg(londeca," ")[0],gpscalfun.splitdeg(londeca," ")[1],gpscalfun.splitdeg(londeca," ")[2]);
     	 	    	  double temlatdeca = gpscalfun.deg2dec(gpscalfun.splitdeg(latdeca," ")[0],gpscalfun.splitdeg(latdeca," ")[1],gpscalfun.splitdeg(latdeca," ")[2]);
     	 	    	  londeca=Double.toString(temlondeca);
    	 	    	  latdeca=Double.toString(temlatdeca);
    	 	    	 //System.out.println(londeca+"  "+latdeca);
    	 	       }
    	 	       if(formatList.isSelected(2)){
      	 	    	  double temlondeca = gpscalfun.degm2dec(gpscalfun.splitdeg(londeca," ")[0],gpscalfun.splitdeg(londeca," ")[1]);
      	 	    	  double temlatdeca = gpscalfun.degm2dec(gpscalfun.splitdeg(latdeca," ")[0],gpscalfun.splitdeg(latdeca," ")[1]);
      	 	    	  londeca=Double.toString(temlondeca);
     	 	    	  latdeca=Double.toString(temlatdeca);
     	 	    	 //System.out.println(londeca+"  "+latdeca);
     	 	       }
    	 	      DisToLL_Execute(londeca,latdeca, brng, dist);
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
