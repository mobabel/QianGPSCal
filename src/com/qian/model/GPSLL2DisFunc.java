package com.qian.model;

import java.util.Vector;

import MathFP;
import com.qian.model.Float11;;


public class GPSLL2DisFunc {

	public static double pi = 3.14159265358979;
	  /** Log10 constant */
	final static public double LOG10 = 2.302585092994045684;
	public static double R =6371; // earth's mean radius in km
    
	private GPSCalFunc gpscalfun;
	private Float11 float11;
        /* GPSLL2DisFunc constructor:
    	 *
    	 *   arguments are in degrees: signed decimal or d-m-s + NSEW as per GPSLL2DisFunc.llToRad()
    	 */ 	
    	public double[] LatLong(double degLat, double degLon){
    		gpscalfun = new GPSCalFunc();
    		
      	  double lat = gpscalfun.Deg2Rad(degLat);
    	  double lon = gpscalfun.Deg2Rad(degLon);
    	  double[] latlon = {0,0};
    	  
    	  latlon[0]=lat;
    	  latlon[1]=lon;
    	  return latlon;
    	}
    	  //this.lat = GPSLL2DisFunc.llToRad(degLat);
    	  //this.lon = GPSLL2DisFunc.llToRad(degLong);
    	
    	public double[] LatLon(double degLat, double degLon){
    	  double[] latlon = {0,0};   	  
    	  latlon[0]=degLat;
    	  latlon[1]=degLon;
    	  return latlon;
    	}

    /*
     * Calculate distance (in km) between two points specified by latitude/longitude with Haversine formula
     *
     * from: Haversine formula - R. W. Sinnott, "Virtues of the Haversine",
     *       Sky and Telescope, vol 68, no 2, 1984
     *       http://www.census.gov/cgi-bin/geo/gisfaq?Q5.1
     */
    public double distHaversine(double p1[], double p2[]) {
      double dLat  = p2[0] - p1[0];
      double dLong = p2[1] - p1[1];

      double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(p1[1]) * Math.cos(p2[1]) * Math.sin(dLong/2) * Math.sin(dLong/2);
      //System.out.println("atan2 " + gpscalfun.atan2(0,1)); 
      double c = 2 * float11.atan2(Math.sqrt(a), Math.sqrt(1-a));
      double d = R * c;

      return d;
    }
    
    /*
     * Calculate distance (in km) between two points specified by latitude/longitude using law of cosines.
     */
    public double distCosineLaw (double p1[],double p2[]) {
      double d = float11.acos((Math.sin(p1[0])*Math.sin(p2[0]) +
              Math.cos(p1[0])*Math.cos(p2[0])*Math.cos(p2[1]-p1[1]))) * R;
      return d;
    }


    /*
     * calculate (initial) bearing (in radians clockwise) between two points
     *
     * from: Ed Williams' Aviation Formulary, http://williams.best.vwh.net/avform.htm#Crs
     */
    public double bearing (double p1[],double p2[]) {
      double y = Math.sin(p2[1]-p1[1]) * Math.cos(p2[0]);
      double x = Math.cos(p1[0])*Math.sin(p2[0]) -
              Math.sin(p1[0])*Math.cos(p2[0])*Math.cos(p2[1]-p1[1]);
      
      return float11.atan2(y, x);
    }


    /*
     * calculate distance of point along a given vector defined by origin point
     * and direction in radians (uses planar not spherical geometry, so only valid
     * for small distances).
     */
    public double distAlongVector(double[] orig, double dirn) {
    	double empty[] = {0,0};
      double dist = distHaversine(empty, orig);  // distance from orig to point
      double brng = bearing(empty, orig);        // bearing between orig and point
      return dist * Math.cos(brng-dirn);
    }


    /*
     * calculate midpoint of great circle line between p1 & p2.
     *   see http://mathforum.org/library/drmath/view/51822.html for derivation
     */
    public double[] midPoint(double p1[], double p2[]) {
      double dLon = p2[1] - p1[1];

      double Bx = Math.cos(p2[0]) * Math.cos(dLon);
      double By = Math.cos(p2[0]) * Math.sin(dLon);

      double lat3 = float11.atan2(Math.sin(p1[0])+Math.sin(p2[0]),Math.sqrt((Math.cos(p1[0])+Bx)*(Math.cos(p1[0])+Bx) + By*By ) );
      double lon3 = p1[1] + float11.atan2(By, Math.cos(p1[0]) + Bx);
      double[] ll3 ={0,0};
      if (Double.isNaN(lat3) || Double.isNaN(lon3)) {
    	  ll3[0]=0 ;
    	  ll3[1]=0;
    	  }

      //ll3=LatLong(lat3*180/Math.PI, lon3*180/Math.PI);
	  ll3[0]=Math.toDegrees(lat3) ;
	  ll3[1]=Math.toDegrees(lon3);
      return ll3;
    }


    /*
     * calculate destination point given start point, initial bearing and distance
     *   see http://williams.best.vwh.net/avform.htm#LL
     */
    public double[] destPoint(double p1[], double brng, double dist) {
      double[] p2 = {0,0}; 
      double d = dist/R;  // d = angular distance covered on earth's surface
      gpscalfun = new GPSCalFunc();
      brng = gpscalfun.Deg2Rad(brng);
    //System.out.println(p1[0]+"  "+p1[1]);
      double p20 = float11.asin( Math.sin(p1[0])*Math.cos(d) + Math.cos(p1[0])*Math.sin(d)*Math.cos(brng) );
      double p21 = p1[1] + float11.atan2(Math.sin(brng)*Math.sin(d)*Math.cos(p1[0]), Math.cos(d)-Math.sin(p1[0])*Math.sin(p20));
      p2[0] = p20;
      p2[1] = p21;

      //if (isNaN(p2[1]) || isNaN(p2[0])) return null;     
      return p2;
    }


    /*
     * calculate final bearing arriving at destination point given start point, initial bearing and distance
     */
    public double finalBrng(double p1[],double p2[],double brng, double dist) {
	  //p2 = destPoint(p1, brng, dist);
      // get reverse bearing point 2 to point 1 & reverse it by adding 180º
      double h2 = (bearing(p2, p1) + Math.PI) % (2*Math.PI);
      return h2;
    }


    /*
     * calculate distance, bearing, destination point on rhumb line
     *   see http://williams.best.vwh.net/avform.htm#Rhumb
     */
    public double distRhumb (double[] p1, double[] p2) {
      double dLat = p2[0]-p1[0], dLon = Math.abs(p2[1]-p1[1]);
      double dPhi = float11.log(Math.tan(p2[0]/2+Math.PI/4)/Math.tan(p1[0]/2+Math.PI/4));
      double q = dLat/dPhi;
      if (!Double.isInfinite(q)){
      q = Math.cos(p1[0]);
      }
      // if dLon over 180° take shorter rhumb across 180° meridian:
      if (dLon > Math.PI) {dLon = 2*Math.PI - dLon;}
      
      double d = Math.sqrt(dLat*dLat + q*q*dLon*dLon); 
      return d * R;
    }


    public double brngRhumb(double[] p1, double[] p2) {
      double dLon = p2[1]-p1[1];
      double dPhi = float11.log(Math.tan(p2[0]/2+Math.PI/4)/Math.tan(p1[0]/2+Math.PI/4));
      if (Math.abs(dLon) > Math.PI) {
    	  dLon = dLon>0 ? -(2*Math.PI-dLon) : (2*Math.PI+dLon);
      }  

      return float11.atan2(dLon,dPhi);
    }

    
    public double[] destPointRhumb(double p1[], double brng, double dist) {
    	  double[] p2 = {0,0}; 
    	  double d = dist/R;  // d = angular distance covered on earth's surface
    	  gpscalfun = new GPSCalFunc();
    	  brng = gpscalfun.Deg2Rad(brng);

    	  p2[0] = p1[0] + d*Math.cos(brng);
    	  double dPhi = float11.log(Math.tan(p2[1]/2+Math.PI/4)/Math.tan(p1[1]/2+Math.PI/4));
    	  double q = (p2[0]-p1[0])/dPhi;
    	  if (!Double.isInfinite(q)){
    	  q = Math.cos(p1[0]);
    	  }
    	  double dLon = d*Math.sin(brng)/q;
    	  // check for some daft bugger going past the pole
    	  if (Math.abs(p2[0]) > Math.PI/2) p2[0] = p2[0]>0 ? Math.PI-p2[0] : -Math.PI-p2[0];
    	  p2[1] = (p1[1]+dLon+Math.PI)%(2*Math.PI) - Math.PI;
    	 
    	  if (Double.isNaN(p2[0]) || Double.isNaN(p2[1])) return null;
    	  return p2;
    	}

    	/*
    	 * convert radians to (signed) degrees, minutes, seconds; eg -0.1rad = -000°05'44"
    	 */
    	public double radToDegMinSec(double rad) {
    	  return (rad<0?'-':' ') + gpscalfun.Rad2Deg(rad);
    	}


    	/*
    	 * override toPrecision method with one which displays trailing zeros in place
    	 *   of exponential notation
    	 *
    	 * (for Haversine, use 4 sf to reflect reasonable indication of accuracy)
    	 */
//    	public double toPrecision(int fig) {
//    	  int scale = Math.ceil(Math.log(this)*MathFP.log(LOG10));
//    	  double mult = gpscalfun.pow(10, fig-scale);
//    	  return Math.round(this*mult)/mult;
//    	}

    
}
