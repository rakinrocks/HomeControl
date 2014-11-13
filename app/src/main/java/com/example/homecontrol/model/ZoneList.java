package com.example.homecontrol.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ZoneList extends ArrayList<Zone> implements Parcelable{

	/**
	 * generated id for serialization
	 */
	private static final long serialVersionUID = 398049914460451488L;

	/*
	 * Constructors 
	 */
	public ZoneList(){}
	
	public ZoneList(ZoneList l){
		populateFromList(l);
	}
	
	/*
	 * set methods
	 */
	private void populateFromList(ZoneList l){
		this.clear();
		for (int i = 0; i < l.size(); i++)
			this.add(l.get(i));
	}
	
	public ZoneList(Parcel in){
		readFromParcel(in);
	}
		
	
	/*
	 * Static field used to regenerate object either
	 * individually or as arrays
	 */
	public static final Parcelable.Creator<ZoneList> CREATOR = new Parcelable.Creator<ZoneList>(){

		@Override
		public ZoneList createFromParcel(Parcel source) {
			return new ZoneList(source);
		}

		@Override
		public ZoneList[] newArray(int size) {
			return new ZoneList[size];
		}
		
	};

	/*
	 * Send data to parcel
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		int size = this.size();
		dest.writeInt(size);
		
		for (int i = 0; i < size; i++){
			/* get ith "zone" in this zonelist */
			Zone z = this.get(i);
			dest.writeString(z.getName());
			dest.writeInt(z.getImgResId());
		}
	}
	
	/*
	 * Create object from parcel
	 */
	public void readFromParcel(Parcel in){
		int size = in.readInt();
		
		for (int i = 0; i < size; i++){
			/*
			 * Create a new zone object, setting it's 
			 * values and add it to the list
			 */
			Zone z = new Zone();
			z.setName(in.readString());
			z.setImgResId(in.readInt());
			this.add(z);
		}
	}
	
	@Override
	public int describeContents() {
		// unused
		return 0;
	}
	

}
