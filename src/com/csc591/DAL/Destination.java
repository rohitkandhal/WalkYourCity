package com.csc591.DAL;

import java.io.Serializable;

// SERIALIZABLE - implemented as serializable because we need to pass the destination object from 
// home activity to map direction activity. You can send string or int directly from one activity to another
// but to send an object it should be either serializable or parcable type. Implementation of serializable is 
// easier and done currently but performance of parcable type is much more better than
// serializable. Can think of changing to parceable type if suggested. (Parceable is somewhat difficult to implement) 
@SuppressWarnings("serial")
public class Destination implements Serializable{
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	
	public int getWalkingTime()
	{
		return walkingTime;
	}
	public void setWalkingTime(int walkingTime)
	{
		this.walkingTime = walkingTime;
	}
	
	private long id;
	private double latitude;
	private double longitude;
	private String name;
	private int type;
	private String description;
	private int favorite;
	private int walkingTime = 0;	// This field is populated in UI using Google Distance Matrix Api
								// Don't hard code anything into it
	

	public Destination()
	{
		
	}
	
	
	public Destination(int id, double latitude, double longitude, String name, int type, String description, int favorite)
	{
		
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.type = type;
		this.description = description;
		this.favorite = favorite;
	}
	
}