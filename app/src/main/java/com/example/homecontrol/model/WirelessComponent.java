package com.example.homecontrol.model;

public class WirelessComponent {
	long _id;	// unique id
	long _zoneId; // id to match the component to it's respective zone
	String _name;  // user set name of component
	String _cId;	// unique component id of the (bluetooth) component used for pairing
	int _imgResId;	// resource id for associated icon (if used)
	
	/* constructors */
	public WirelessComponent(){}
	
	public WirelessComponent(String name, String cId, int imgResId){
		this._name = name;
		this._cId = cId;
		this._imgResId = imgResId;
	}
	
	public WirelessComponent(long id, long zoneId, String name, String cId, int imgResId){
		this._id = id;
		this._name = name;
		this._cId = cId;
		this._zoneId = zoneId;
		this._imgResId = imgResId;
	}
	
	/* get methods */
	public long getId(){
		return this._id;
	}
	
	public long getZoneId(){
		return this._zoneId;
	}
	
	public String getName(){
		return this._name;
	}
	
	public String getCId(){
		return this._cId;
	}
	
	public int getImgResId(){
		return this._imgResId;
	}
	
	/* set methods */
	public void setId(long id){
		this._id = id;
	}
	
	public void setZoneId(long zoneId){
		this._zoneId = zoneId;
	}
	
	public void setName(String name){
		this._name = name;
	}
	
	public void setId(String cId){
		this._cId = cId;
	}
	
	public void setImgResId(int imgResId){
		this._imgResId = imgResId;
	}
}
