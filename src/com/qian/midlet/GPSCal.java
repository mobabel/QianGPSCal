/*
 * Created on 2006-9-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.qian.midlet;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import com.qian.model.Setting;
import com.qian.util.UIController;
import com.qian.view.GPSFuncSel;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GPSCal extends MIDlet implements Runnable{
	private Display display;
	private static UIController controller;
    private boolean isInitialized;
    private boolean splashIsShown;
    private Image   icon_start=null; 
	
	/**
	 * default constructor
	 */
	public GPSCal() {
		super();
		display=Display.getDisplay(this);
	}

	protected void startApp() throws MIDletStateChangeException {
        Thread GPSCal = new Thread(new GPSCal.SplashScreen());
        GPSCal.start();

        Thread myThread = new Thread(this);
        myThread.start(); 

	}
	
    public void run(){
        while(!splashIsShown){
            Thread.yield();
        }       
        doTimeConsumingInit();       
        while(true){
            // soft loop           
            Thread.yield();
        }
    }
    
    private void doTimeConsumingInit(){ 
        // Just mimic some lengthy initialization for 3 secs
        long endTime= System.currentTimeMillis()+3000;
        while(System.currentTimeMillis()<endTime ){}

		controller=new UIController(this);
		controller.init();
        isInitialized=true;
    }
    
	protected void pauseApp() {
		this.notifyPaused();
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		controller=null;
		
	}
	
	public void setCurrent(Displayable disp){
		display.setCurrent(disp);
	}
	
	public void setCurrent(Alert alert, Displayable disp){
		display.setCurrent(alert, disp);
    }
	public Displayable getCurrent(){
		return display.getCurrent();
    }
	
	public void exit(boolean arg0){
		try{
			destroyApp(arg0);
			notifyDestroyed();
		}catch(MIDletStateChangeException e){
			//
		}
	}
	
    public class SplashScreen implements Runnable{
        private SplashCanvas splashCanvas;
        
        public void run(){
            splashCanvas = new SplashCanvas ();      
            display.setCurrent(splashCanvas);
            splashCanvas.repaint();
            splashCanvas.serviceRepaints();
            while(!isInitialized){
                try{
                    Thread.yield();
                }catch(Exception e){}
            }
        }             
    }
    
    public class SplashCanvas extends Canvas {
     
        protected void paint(Graphics g){
 	       try {
	        	icon_start=Image.createImage("/images/icon_start.png");
		    } catch (java.io.IOException e) {
		    	icon_start=null;
				System.out.println("Load image error when initializing Exception happens:" + e.getMessage());
		    }
		    g.drawImage(icon_start,getWidth()/2, getHeight()/2-15, Graphics.BOTTOM| Graphics.HCENTER);
            
		    g.drawString("Qian GPSCal 1.03", getWidth()/2, getHeight()/2, Graphics.BOTTOM| Graphics.HCENTER );
            g.drawString("System is initializing...", getWidth()/2, getHeight()/2+15, Graphics.BOTTOM| Graphics.HCENTER );
            splashIsShown=true;
        }
    }
    
}
