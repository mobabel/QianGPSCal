package com.qian.view;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.List;

import com.qian.model.Setting;
import com.qian.util.UIController;
import com.qian.util.UIController.EventID;
import com.qian.view.GPSDec2Deg;

public class GPSFuncSel extends Form implements ItemCommandListener{
	
	private UIController    controller;
    private Display        display;
	private GPSDec2Deg gpsdec2deg;
	private GPSDec2Degm gpsdec2degm;
	private GPSDeg2Dec gpsdeg2dec;
	private GPSDeg2Degm gpsdeg2degm;
	private GPSDegm2Dec gpsdegm2dec;
	private GPSDegm2Deg gpsdegm2deg;
	
	private GPSLLdec2UTM gpslldec2utm;
	private GPSLLdeg2UTM gpslldeg2utm;
	private GPSLLdegm2UTM gpslldegm2utm;	
	private GPSUTM2LL gpsutm2ll;
	
	private  GPSLL2Dis gpsll2dis;
	private  GPSDis2LL gpsdis2ll;
	
	//private Item item;
	
    //private Command        okCommand_llutm= new Command("OK",Command.OK,1);
    //private Command        okCommand_decdeg= new Command("OK",Command.OK,1);
    private Command        okCommand= new Command("OK",Command.OK,1);
    private Command        exitCommand = new Command("Exit",Command.EXIT,2);
    private Command        aboutCommand = new Command("About",Command.HELP,3);
    
    private ChoiceGroup dec2deg ;
    private ChoiceGroup ll2utm ;
    private ChoiceGroup ll2dis ;

	public GPSFuncSel(String title,UIController control) {
		super(title);
		controller=control;
		Image icon = null;
		try{
			icon = Image.createImage("/images/i.png");
		}catch(Exception e){}
		
		dec2deg=new ChoiceGroup("Dec2Deg",Choice.POPUP);
		dec2deg.append("d.dddddd->dd°mm'ss\"",null);
		dec2deg.append("d.dddddd->d°mmmm'",null);
		dec2deg.append("dd°mm'ss\"->d.dddddd",null);
		dec2deg.append("dd°mm'ss\"->d°mmmm'",null);
		dec2deg.append("d°mmmm'->d.dddddd",null);
		dec2deg.append("d°mmmm'->dd°mm'ss\"",null);
		

		ll2utm=new ChoiceGroup("Lon&Lat2UTM",Choice.POPUP);
		ll2utm.append("d.dddddd->UTM",null);
		ll2utm.append("dd°mm'ss\"->UTM",null);
		ll2utm.append("d°mmmm'->UTM",null);
		ll2utm.append("UTM->Lon&Lat",null);
		
		ll2dis=new ChoiceGroup("Lon&Lat2Dis",Choice.POPUP);
		ll2dis.append("LL&LL->Dis",null);
		ll2dis.append("LL&Dis->LL",null);

		this.append(dec2deg);
		this.append(ll2utm);
		this.append(ll2dis);
		
	    //this.addCommand(exitCommand);
	    
	    dec2deg.addCommand(exitCommand);
	    dec2deg.addCommand(okCommand);
	    dec2deg.addCommand(aboutCommand);
        
	    ll2utm.addCommand(exitCommand);
	    ll2utm.addCommand(okCommand);
	    ll2utm.addCommand(aboutCommand);
	    
	    ll2dis.addCommand(exitCommand);
	    ll2dis.addCommand(okCommand);
	    ll2dis.addCommand(aboutCommand);
	    
        //this.addCommand(aboutCommand);        
	    //this.setCommandListener(this);
	    dec2deg.setItemCommandListener(this);
	    ll2utm.setItemCommandListener(this);
	    ll2dis.setItemCommandListener(this);
	}
	
    public void commandAction(Command c, Item item) {
 	   if(c==okCommand) {
			// controller.handleEvent(UIController.EventID.EVENT_SEL_OK);
 		String label = item.getLabel();
 		if(label.equals("Dec2Deg")){
			//String ind_dec2deg = dec2deg.getString(0);
			int ind_dec2deg = dec2deg.getSelectedIndex();
 				if (ind_dec2deg == 0) {
 					gpsdec2deg = new GPSDec2Deg(Setting.TITLE_GPSDec2Deg,
 							controller);
 					controller.setCurrent(gpsdec2deg);
 				}
 				else if (ind_dec2deg == 1) {
 					gpsdec2degm = new GPSDec2Degm(Setting.TITLE_GPSDec2Degm,
 							controller);
 					controller.setCurrent(gpsdec2degm);
 				}
 				else if (ind_dec2deg == 2) {
 					gpsdeg2dec = new GPSDeg2Dec(Setting.TITLE_GPSDeg2Dec,
 							controller);
 					controller.setCurrent(gpsdeg2dec);
 				}
 				else if (ind_dec2deg == 3) {
 					gpsdeg2degm = new GPSDeg2Degm(Setting.TITLE_GPSDeg2Degm,
 							controller);
 					controller.setCurrent(gpsdeg2degm);
 				}
 				else if (ind_dec2deg ==4) {
 					gpsdegm2dec = new GPSDegm2Dec(Setting.TITLE_GPSDegm2Dec,
 							controller);
 					controller.setCurrent(gpsdegm2dec);
 				}
 				else if (ind_dec2deg == 5) {
 					gpsdegm2deg = new GPSDegm2Deg(Setting.TITLE_GPSDegm2Deg,
 							controller);
 					controller.setCurrent(gpsdegm2deg);
 				} 
 		}
 	   
 		if(label.equals("Lon&Lat2UTM")){
			int ind_ll2utm = ll2utm.getSelectedIndex();

			if (ind_ll2utm == 0) {
				gpslldec2utm = new GPSLLdec2UTM(Setting.TITLE_GPSLLdec2UTM,controller);
				controller.setCurrent(gpslldec2utm);
			}
			else if (ind_ll2utm == 1) {
				gpslldeg2utm = new GPSLLdeg2UTM(Setting.TITLE_GPSLLdeg2UTM,controller);
				controller.setCurrent(gpslldeg2utm);
			}
			else if (ind_ll2utm == 2) {
				gpslldegm2utm = new GPSLLdegm2UTM(Setting.TITLE_GPSLLdegm2UTM,controller);
				controller.setCurrent(gpslldegm2utm);
			}
			else if (ind_ll2utm == 3) {
				gpsutm2ll = new GPSUTM2LL(Setting.TITLE_GPSUTM2LL,controller);
				controller.setCurrent(gpsutm2ll);
			}    	
 	   }
 		if(label.equals("Lon&Lat2Dis")){
			int ind_ll2dis = ll2dis.getSelectedIndex();

			if (ind_ll2dis == 0) {
				gpsll2dis = new GPSLL2Dis(Setting.TITLE_GPSLL2Dis,controller);
				controller.setCurrent(gpsll2dis);
			}
			else if (ind_ll2dis == 1) {
				gpsdis2ll = new GPSDis2LL(Setting.TITLE_GPSDis2LL,controller);
				controller.setCurrent(gpsdis2ll);
			}    	
 	   }
 	   }
	    else if(c==exitCommand ) {
	    	controller.handleEvent(UIController.EventID.EVENT_EXIT);
	        }
        else if(c==aboutCommand){
		    controller.handleEvent(UIController.EventID.EVENT_ABOUT);
		} 



}


}
