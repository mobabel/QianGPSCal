package com.qian.model;

import java.util.*;
import MathFP;



public class GPSCalFunc {
	
	  /** Square root from 3 */
	public final static double SQRT3 = 1.732050807568877294;
	public static double pi = 3.14159265358979;
	
    /* Ellipsoid model constants (actual values here are for WGS84) */
	public static double sm_a = 6378137.0;
	public static double sm_b = 6356752.314;
	public static double sm_EccSquared = 6.69437999013e-03;

	public static double UTMScaleFactor = 0.9996;
	public static double R =6371; // earth's mean radius in km
	
    /*
	    * ArcLengthOfMeridian
	    *
	    * Computes the ellipsoidal distance from the equator to a point at a
	    * given latitude.
	    *
	    * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	    * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	    *
	    * Inputs:
	    *     phi - Latitude of the point, in radians.
	    *
	    * Globals:
	    *     sm_a - Ellipsoid model major axis.
	    *     sm_b - Ellipsoid model minor axis.
	    *
	    * Returns:
	    *     The ellipsoidal distance of the point from the equator, in meters.
	    *
	    */
	    public double ArcLengthOfMeridian (double phi)
	    {
	        double alpha, beta, gamma, delta, epsilon, n;
	        double result;

	        /* Precalculate n */
	        n = (sm_a - sm_b) / (sm_a + sm_b);
	        //int in = Integer.parseInt(Double.toString(n));
	        /* Precalculate alpha */
	        alpha = ((sm_a + sm_b) / 2.0)
	           * (1.0 + (this.pow (n, 2) / 4.0) + (this.pow (n, 4) / 64.0));

	        /* Precalculate beta */
	        beta = (-3.0 * n / 2.0) + (9.0 * this.pow (n, 3) / 16.0)
	           + (-3.0 * this.pow (n, 5) / 32.0);

	        /* Precalculate gamma */
	        gamma = (15.0 * this.pow (n, 2) / 16.0)
	            + (-15.0 * this.pow (n, 4) / 32.0);
	    
	        /* Precalculate delta */
	        delta = (-35.0 * this.pow (n, 3) / 48.0)
	            + (105.0 * this.pow (n, 5) / 256.0);
	    
	        /* Precalculate epsilon */
	        epsilon = (315.0 * this.pow (n, 4) / 512.0);
	    
	    /* Now calculate the sum of the series and return */
	    result = alpha
	        * (phi + (beta * Math.sin (2.0 * phi))
	            + (gamma * Math.sin (4.0 * phi))
	            + (delta * Math.sin (6.0 * phi))
	            + (epsilon * Math.sin (8.0 * phi)));

	    return result;
	    }
	
	    /*
	     * UTMCentralMeridian
	     *
	     * Determines the central meridian for the given UTM zone.
	     *
	     * Inputs:
	     *     zone - An integer value designating the UTM zone, range [1,60].
	     *
	     * Returns:
	     *   The central meridian for the given UTM zone, in radians, or zero
	     *   if the UTM zone parameter is outside the range [1,60].
	     *   Range of the central meridian is the radian equivalent of [-177,+177].
	     *
	     */
	     public double UTMCentralMeridian (int zone)
	     {
	         double cmeridian;

	         cmeridian = Deg2Rad (-183.0 + (zone * 6.0));
	     
	         return cmeridian;
	     }

	     /*
	      * FootpointLatitude
	      *
	      * Computes the footpoint latitude for use in converting transverse
	      * Mercator coordinates to ellipsoidal coordinates.
	      *
	      * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	      *   GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	      *
	      * Inputs:
	      *   y - The UTM northing coordinate, in meters.
	      *
	      * Returns:
	      *   The footpoint latitude, in radians.
	      *
	      */
	      public double FootpointLatitude (double y)
	      {
	          double y_, alpha_, beta_, gamma_, delta_, epsilon_, n;
	          double result;
	          
	          /* Precalculate n (Eq. 10.18) */
	          n = (sm_a - sm_b) / (sm_a + sm_b);
	          //int in = Integer.parseInt(Double.toString(n));
	          
	          /* Precalculate alpha_ (Eq. 10.22) */
	          /* (Same as alpha in Eq. 10.17) */
	          alpha_ = ((sm_a + sm_b) / 2.0)
	              * (1 + (this.pow (n, 2) / 4) + (this.pow (n, 4) / 64));
	          
	          /* Precalculate y_ (Eq. 10.23) */
	          y_ = y / alpha_;
	          
	          /* Precalculate beta_ (Eq. 10.22) */
	          beta_ = (3.0 * n / 2.0) + (-27.0 * this.pow (n, 3) / 32.0)
	              + (269.0 * this.pow (n, 5) / 512.0);
	          
	          /* Precalculate gamma_ (Eq. 10.22) */
	          gamma_ = (21.0 * this.pow (n, 2) / 16.0)
	              + (-55.0 * this.pow (n, 4) / 32.0);
	          	
	          /* Precalculate delta_ (Eq. 10.22) */
	          delta_ = (151.0 * this.pow (n, 3) / 96.0)
	              + (-417.0 * this.pow (n, 5) / 128.0);
	          	
	          /* Precalculate epsilon_ (Eq. 10.22) */
	          epsilon_ = (1097.0 * this.pow (n, 4) / 512.0);
	          	
	          /* Now calculate the sum of the series (Eq. 10.21) */
	          result = y_ + (beta_ * Math.sin (2.0 * y_))
	              + (gamma_ * Math.sin (4.0 * y_))
	              + (delta_ * Math.sin (6.0 * y_))
	              + (epsilon_ * Math.sin (8.0 * y_));
	          
	          return result;
	      }
	      
	      /*
	       * MapLatLonToXY
	       *
	       * Converts a latitude/longitude pair to x and y coordinates in the
	       * Transverse Mercator projection.  Note that Transverse Mercator is not
	       * the same as UTM; a scale factor is required to convert between them.
	       *
	       * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	       * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	       *
	       * Inputs:
	       *    phi - Latitude of the point, in radians.
	       *    lambda - Longitude of the point, in radians.
	       *    lambda0 - Longitude of the central meridian to be used, in radians.
	       *
	       * Outputs:
	       *    xy - A 2-element array containing the x and y coordinates
	       *         of the computed point.
	       *
	       * Returns:
	       *    The function does not return a value.
	       *
	       */
	       public void MapLatLonToXY (double phi, double lambda, double lambda0, double xy[])
	       {
	           double N, nu2, ep2, t, t2, l;
	           double l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;
	           double tmp;
	           
	           /* Precalculate ep2 */
	           ep2 = (this.pow (sm_a, 2) - this.pow (sm_b, 2)) / this.pow (sm_b, 2);
	           //System.out.println("ep2 is "+ep2);
	           /* Precalculate nu2 */
	           nu2 = ep2 * this.pow (Math.cos (phi), 2);
	       
	           /* Precalculate N */
	           N = this.pow (sm_a, 2) / (sm_b * Math.sqrt (1 + nu2));
	       
	           /* Precalculate t */
	           t = Math.tan (phi);
	           t2 = t * t;
	           tmp = (t2 * t2 * t2) - this.pow (t, 6);

	           /* Precalculate l */
	           l = lambda - lambda0;
	       
	           /* Precalculate coefficients for l**n in the equations below
	              so a normal human being can read the expressions for easting
	              and northing
	              -- l**1 and l**2 have coefficients of 1.0 */
	           l3coef = 1.0 - t2 + nu2;
	       
	           l4coef = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2);
	       
	           l5coef = 5.0 - 18.0 * t2 + (t2 * t2) + 14.0 * nu2
	               - 58.0 * t2 * nu2;
	       
	           l6coef = 61.0 - 58.0 * t2 + (t2 * t2) + 270.0 * nu2
	               - 330.0 * t2 * nu2;
	       
	           l7coef = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - (t2 * t2 * t2);
	       
	           l8coef = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - (t2 * t2 * t2);

	           /* Calculate easting (x) */
	           double xy0 = N * Math.cos (phi) * l
	               + (N / 6.0 * this.pow (Math.cos (phi), 3) * l3coef * this.pow (l, 3))
	               + (N / 120.0 * this.pow (Math.cos (phi), 5) * l5coef * this.pow (l, 5))
	               + (N / 5040.0 * this.pow (Math.cos (phi), 7) * l7coef * this.pow (l, 7));
	           //System.out.println("xy[0] is "+xy0);
	           /* Calculate northing (y) */
	           double xy1 = ArcLengthOfMeridian (phi)
	               + (t / 2.0 * N * this.pow (Math.cos (phi), 2) * this.pow (l, 2))
	               + (t / 24.0 * N * this.pow (Math.cos (phi), 4) * l4coef * this.pow (l, 4))
	               + (t / 720.0 * N * this.pow (Math.cos (phi), 6) * l6coef * this.pow (l, 6))
	               + (t / 40320.0 * N * this.pow (Math.cos (phi), 8) * l8coef * this.pow (l, 8));
	           //System.out.println("xy[1] is "+xy1);
	           xy[0]=xy0;
	           xy[1]=xy1;
	           
	           //xy.addElement(String.valueOf(xy0).toString());
	           //xy.addElement(String.valueOf(xy1).toString());
	           //return;
	       }    
	
	       
	       /*
	        * MapXYToLatLon
	        *
	        * Converts x and y coordinates in the Transverse Mercator projection to
	        * a latitude/longitude pair.  Note that Transverse Mercator is not
	        * the same as UTM; a scale factor is required to convert between them.
	        *
	        * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	        *   GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	        *
	        * Inputs:
	        *   x - The easting of the point, in meters.
	        *   y - The northing of the point, in meters.
	        *   lambda0 - Longitude of the central meridian to be used, in radians.
	        *
	        * Outputs:
	        *   philambda - A 2-element containing the latitude and longitude
	        *               in radians.
	        *
	        * Returns:
	        *   The function does not return a value.
	        *
	        * Remarks:
	        *   The local variables Nf, nuf2, tf, and tf2 serve the same purpose as
	        *   N, nu2, t, and t2 in MapLatLonToXY, but they are computed with respect
	        *   to the footpoint latitude phif.
	        *
	        *   x1frac, x2frac, x2poly, x3poly, etc. are to enhance readability and
	        *   to optimize computations.
	        *
	        */
	        public void MapXYToLatLon (double x, double y, double lambda0, double philambda[])
	        {
	            double phif, Nf, Nfpow, nuf2, ep2, tf, tf2, tf4, cf;
	            double x1frac, x2frac, x3frac, x4frac, x5frac, x6frac, x7frac, x8frac;
	            double x2poly, x3poly, x4poly, x5poly, x6poly, x7poly, x8poly;
	        	
	            /* Get the value of phif, the footpoint latitude. */
	            phif = FootpointLatitude (y);
	            	
	            /* Precalculate ep2 */
	            ep2 = (this.pow (sm_a, 2) - this.pow (sm_b, 2))
	                  / this.pow (sm_b, 2);
	            	
	            /* Precalculate cos (phif) */
	            cf = Math.cos (phif);
	            	
	            /* Precalculate nuf2 */
	            nuf2 = ep2 * this.pow (cf, 2);
	            	
	            /* Precalculate Nf and initialize Nfpow */
	            Nf = this.pow (sm_a, 2) / (sm_b * Math.sqrt (1 + nuf2));
	            Nfpow = Nf;
	            	
	            /* Precalculate tf */
	            tf = Math.tan (phif);
	            tf2 = tf * tf;
	            tf4 = tf2 * tf2;
	            
	            /* Precalculate fractional coefficients for x**n in the equations
	               below to simplify the expressions for latitude and longitude. */
	            x1frac = 1.0 / (Nfpow * cf);
	            
	            Nfpow *= Nf;   /* now equals Nf**2) */
	            x2frac = tf / (2.0 * Nfpow);
	            
	            Nfpow *= Nf;   /* now equals Nf**3) */
	            x3frac = 1.0 / (6.0 * Nfpow * cf);
	            
	            Nfpow *= Nf;   /* now equals Nf**4) */
	            x4frac = tf / (24.0 * Nfpow);
	            
	            Nfpow *= Nf;   /* now equals Nf**5) */
	            x5frac = 1.0 / (120.0 * Nfpow * cf);
	            
	            Nfpow *= Nf;   /* now equals Nf**6) */
	            x6frac = tf / (720.0 * Nfpow);
	            
	            Nfpow *= Nf;   /* now equals Nf**7) */
	            x7frac = 1.0 / (5040.0 * Nfpow * cf);
	            
	            Nfpow *= Nf;   /* now equals Nf**8) */
	            x8frac = tf / (40320.0 * Nfpow);
	            
	            /* Precalculate polynomial coefficients for x**n.
	               -- x**1 does not have a polynomial coefficient. */
	            x2poly = -1.0 - nuf2;
	            
	            x3poly = -1.0 - 2 * tf2 - nuf2;
	            
	            x4poly = 5.0 + 3.0 * tf2 + 6.0 * nuf2 - 6.0 * tf2 * nuf2
	            	- 3.0 * (nuf2 *nuf2) - 9.0 * tf2 * (nuf2 * nuf2);
	            
	            x5poly = 5.0 + 28.0 * tf2 + 24.0 * tf4 + 6.0 * nuf2 + 8.0 * tf2 * nuf2;
	            
	            x6poly = -61.0 - 90.0 * tf2 - 45.0 * tf4 - 107.0 * nuf2
	            	+ 162.0 * tf2 * nuf2;
	            
	            x7poly = -61.0 - 662.0 * tf2 - 1320.0 * tf4 - 720.0 * (tf4 * tf2);
	            
	            x8poly = 1385.0 + 3633.0 * tf2 + 4095.0 * tf4 + 1575 * (tf4 * tf2);
	            	
	            /* Calculate latitude */
	           philambda[0] = phif + x2frac * x2poly * (x * x)
	            	+ x4frac * x4poly * this.pow (x, 4)
	            	+ x6frac * x6poly * this.pow (x, 6)
	            	+ x8frac * x8poly * this.pow (x, 8);
	            	
	            /* Calculate longitude */
	           philambda[1] = lambda0 + x1frac * x
	            	+ x3frac * x3poly * this.pow (x, 3)
	            	+ x5frac * x5poly * this.pow (x, 5)
	            	+ x7frac * x7poly * this.pow (x, 7);

	            //philambda.addElement(String.valueOf(philambda0).toString());
	            //philambda.addElement(String.valueOf(philambda1).toString());
		           
	            return;
	        }
	        
	        /*
	         * LatLonToUTMXY
	         *
	         * Converts a latitude/longitude pair to x and y coordinates in the
	         * Universal Transverse Mercator projection.
	         *
	         * Inputs:
	         *   lat - Latitude of the point, in radians.
	         *   lon - Longitude of the point, in radians.
	         *   zone - UTM zone to be used for calculating values for x and y.
	         *          If zone is less than 1 or greater than 60, the routine
	         *          will determine the appropriate zone from the value of lon.
	         *
	         * Outputs:
	         *   xy - A 2-element array where the UTM x and y values will be stored.
	         *
	         * Returns:
	         *   The UTM zone used for calculating the values of x and y.
	         *
	         */
	         public int LatLonToUTMXY (double lat, double lon, int zone, double xy[])
	         {
	             MapLatLonToXY (lat, lon, UTMCentralMeridian (zone), xy);
	             //System.out.println("xy ist "+xy);
	             /* Adjust easting and northing for UTM system. */
	             xy[0] = xy[0] * UTMScaleFactor + 500000.0;
	             xy[1] = xy[1] * UTMScaleFactor;
	             if (xy[1] < 0.0){
	                 xy[1] = xy[1] + 10000000.0;
	             }
	             
	             return zone;
	         }
	         
	         
	         
	         /*
	         * UTMXYToLatLon
	         *
	         * Converts x and y coordinates in the Universal Transverse Mercator
	         * projection to a latitude/longitude pair.
	         *
	         * Inputs:
	         *	x - The easting of the point, in meters.
	         *	y - The northing of the point, in meters.
	         *	zone - The UTM zone in which the point lies.
	         *	southhemi - True if the point is in the southern hemisphere;
	         *               false otherwise.
	         *
	         * Outputs:
	         *	latlon - A 2-element array containing the latitude and
	         *            longitude of the point, in radians.
	         *
	         * Returns:
	         *	The function does not return a value.
	         *
	         */
	         public void UTMXYToLatLon (double x, double y, int zone, boolean southhemi, double latlon[])
	         {
	             double cmeridian;
	             	
	             x -= 500000.0;
	             x /= UTMScaleFactor;
	             	
	             /* If in southern hemisphere, adjust y accordingly. */
	             if (southhemi)
	             y -= 10000000.0;
	             		
	             y /= UTMScaleFactor;
	             
	             cmeridian = UTMCentralMeridian (zone);
	             MapXYToLatLon (x, y, cmeridian, latlon);
	             	
	             return;
	         }   
	      
	         /*
	         * Set N or S.
	         */  
	         public String StringNS(double lat){
	        	 String StringNS = null;
	             if (lat < 0)
	                 // Set the S button.
	            	 StringNS = "S";
	             else
	                 // Set the N button.
	            	 StringNS = "N";
	        	 return StringNS;
	         }
    /*
    *
    * Converts decimal degrees to dd°mm'ss" degrees.
    */
    public Vector dec2deg(String num){
//    	120.5597=>120°33'35"
    	String[] deg = splitdec(num,".");
    	double d = Double.valueOf(deg[0]).doubleValue() ;
    	//System.out.println("d is "+d) ;
    	double temp= (Double.valueOf("0."+ deg[1]).doubleValue())*60;
    	String[] m_ = splitdec(String.valueOf(temp).toString(),".");
    	double m = Double.valueOf(m_[0]).doubleValue();
    	double s = (Double.valueOf("0."+ m_[1]).doubleValue())*60;
    	
    	Vector degree = new Vector();
    	degree.addElement(String.valueOf(d).toString());
    	degree.addElement(String.valueOf(m).toString());
    	degree.addElement(String.valueOf(s).toString());
    	
    	return degree;
    }
    
    /*
    *
    * Converts decimal degrees dd°mmmm' degrees.
    */
    public Vector dec2degm(String num){
//    	120.5597=>120°33.582'
    	String[] deg = splitdec(num,".");
    	double d = Double.valueOf(deg[0]).doubleValue() ;

    	//System.out.println("d is "+d) ;
    	double m= (Double.valueOf("0."+ deg[1]).doubleValue())*60;    	
    	Vector degreem = new Vector();
    	degreem.addElement(String.valueOf(d).toString());
    	degreem.addElement(String.valueOf(m).toString());
    	
    	return degreem;
    } 
    
    /*
    *
    * Converts dd°mm'ss" degrees to decimal degrees.
    */
    public double deg2dec(String d, String m, String s){
//    	116°23.00'27" =116.39083333
    	double d1 = Double.valueOf(d).doubleValue() ;
    	double m1 = Double.valueOf(m).doubleValue() ;
    	double s1 = Double.valueOf(s).doubleValue() ;
    	double num;
    	if(d1<0){
    		num = 0-(Math.abs(d1) + m1/60 + s1/3600);
    	}
    	else
    	num = d1 + m1/60 + s1/3600;
    	
    return num;	
    }
    
    /*
    *
    * Converts dd°mm'ss" degrees to dd°mmmm' degrees.
    */
    public Vector deg2degm(String d, String m, String s){
//    	116°23'27" =116°23.45'
    	double d1 = Double.valueOf(d).doubleValue() ;
    	double m1 = Double.valueOf(m).doubleValue() ;
    	double s1 = Double.valueOf(s).doubleValue() ;
    	double mm = m1 + s1/60;
    	
    	Vector degm = new Vector();
    	degm.addElement(String.valueOf(d1).toString());
    	degm.addElement(String.valueOf(mm).toString());
    	
    return degm;	
    }
  
    /*
    *
    * Converts dd°mmmm' degrees to decimal degrees.
    */
    public double degm2dec(String d, String m){
//    	116°23.45' =116.39083333
    	double d1 = Double.valueOf(d).doubleValue() ;
    	double m1 = Double.valueOf(m).doubleValue() ;
    	double num;
    	if(d1<0){
    		num =  0-(Math.abs(d1) + m1/60);
    	}
    	else
    	num = d1 + m1/60;
    	return num;
    }
    
    /*
    *
    * Converts dd°mmmm' degrees to dd°mm'ss" degrees.
    */
    public Vector degm2deg(String d, String m){
//    	116°23.45' =116°23'27"
    	double d1 = Double.valueOf(d).doubleValue() ;
    	String[] m1 = splitdec(m,".");
    	double mm = Double.valueOf(m1[0]).doubleValue() ;
    	double s= (Double.valueOf("0."+ m1[1]).doubleValue())*60;
    	
    	Vector degree = new Vector();
    	degree.addElement(String.valueOf(d1).toString());
    	degree.addElement(String.valueOf(mm).toString());
    	degree.addElement(String.valueOf(s).toString());
    	return degree;
    }
    
    /*
     *
     * Converts decimal degrees to radians.
     */
    public double Deg2Rad (double deg)
     {
         return (deg / 180.0 * pi);
     }

     /*
     *
     * Converts radians to decimal degrees.
     */
     public double Rad2Deg (double rad)
     {
         return (rad / pi * 180.0);
     }
     
 	
     /*
     *
     * Calculate b power of a.
     */
     public double pow(double a, int b){
    	 double atemp = a;
    	 if (b==0){
    		 atemp = 1;
    	 }
    	 else if (b==1){
    		 atemp = a;
    	 }
    	 else if (b==-1){
    		 atemp = 1/a;
    	 }
    	 else if (b>1){
    	 		for (int i = 0; i < b-1; i++) {
    				atemp = atemp*a;
    			} 
    	 }
    	 else if (b<-1){
 	 		for (int i = 0; i < b-1; i++) {
 				atemp = atemp*a;
 			}
 	 		atemp=1/atemp;
 	 }

    	 return atemp;
     }
     

     /*
     * split decimal degrees with "." .
     */
    public static String[] splitdec(String original, String regex) {
		int startIndex = 0;
		Vector v = new Vector();
		String[] str = null;
		int index = 0;
		startIndex = original.indexOf(regex);
		//if the user input an Integer, add .0 behind
		if(startIndex==-1){
			original=original.concat(".0");
			startIndex = original.indexOf(regex);
		}
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			//System.out.println(" " + startIndex);
			v.addElement(temp);
			index = startIndex + regex.length();
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
		v.addElement(original.substring(index + 1 - regex.length()));

		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
     return str;
    }
    
    public static String[] splitdeg(String original, String regex) {
		int startIndex = 0;
		Vector v = new Vector();
		String[] str = null;
		int index = 0;
		startIndex = original.indexOf(regex);
		
//		if(startIndex==-1){
//			original=original.concat(regex);
//			startIndex = original.indexOf(regex);
//		}
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			//System.out.println(" " + startIndex);
			v.addElement(temp);
			index = startIndex + regex.length();
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
//		if these is no separator, the whole string is the first array item
		v.addElement(original.substring(index + 1 - regex.length()));

		str = new String[3];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		
		if (v.size()==1){str[1]="0";str[2]="0";}
		if (v.size()==2){str[2]="0";}

     return str;
    }
    
    public static String[] splitdegm(String original, String regex) {
		int startIndex = 0;
		Vector v = new Vector();
		String[] str = null;
		int index = 0;
		startIndex = original.indexOf(regex);
		
//		if(startIndex==-1){
//			original=original.concat(regex);
//			startIndex = original.indexOf(regex);
//		}
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			//System.out.println(" " + startIndex);
			v.addElement(temp);
			index = startIndex + regex.length();
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
//		if these is no separator, the whole string is the first array item
		v.addElement(original.substring(index + 1 - regex.length()));

		str = new String[2];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		
		if (v.size()==1){str[1]="0";}

     return str;
    }
    
//    public boolean isNumeric(String str)
//    {
//    Pattern pattern = Pattern.compile("[0-9]*");
//    Matcher isNum = pattern.matcher(str);
//    if( !isNum.matches() )
//    {
//    return false;
//    }
//    return true;
//    } 
    
  public boolean isNumeric(String str){
    String strRef = "- .1234567890";
    String tempChar;
    for (int i=0;i<str.length();i++) {
    	  tempChar= str.substring(i,i+1);
    	  if (strRef.indexOf(tempChar,0)==-1) {
    	   return false;
    	  }
   }
    return true;
  }
  
  
  
}
