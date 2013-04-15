package com.csc591.utils;

public class PathHelper {

	 public static String makeURL (double sourcelat, double sourcelng, double destlat, double destlng ){
	        StringBuilder urlString = new StringBuilder();
	        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	       
	        urlString.append("?origin=");// from
	        urlString.append(Double.toString(sourcelat));
	        urlString.append(",");
	        urlString.append(Double.toString( sourcelng));
	        
	        urlString.append("&destination=");// to
	        urlString.append(Double.toString( destlat));
	        urlString.append(",");
	        urlString.append(Double.toString( destlng));
	        
	        urlString.append("&sensor=false&mode=walking&sensor=true");
	        return urlString.toString();
	 }
	
}
