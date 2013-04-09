package model;

public class Destination {
	
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
	
	private long id;
	private double latitude;
	private double longitude;
	private String name;
	private int type;
	private String description;
	private int favorite;
	

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
