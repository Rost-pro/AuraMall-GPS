package ru.rostpro.model;

import com.google.android.maps.GeoPoint;

public class BusModel {

	private GeoPoint position;
	private String decription;
	private String routeName;
	private int id;
	
	public BusModel(GeoPoint iposition,String idecription,int iid,String irouteName){
		position = iposition;
		decription = idecription;
		routeName = irouteName;
		id = iid;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public GeoPoint getPosition() {
		return position;
	}

	public void setPosition(GeoPoint position) {
		this.position = position;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
}
