package com.example.homecontrol.model;

import java.util.ArrayList;


public class Zone{

	private String _name;	// name of zone
	private int _imgResId;	// resource id for icon
	ArrayList<Component> _components;	// list of components associated with zone (i.e. lights)
	
	/* constructors */
	public Zone(){}
	
	public Zone(String name){
		this._name = name;
        this._components = null;
	}
	
	public Zone(String name, int imgResId){
		this._name = name;
		this._imgResId = imgResId;
        this._components = null;
	}

    public Zone(String name, int imgResId, ArrayList<Component> components){
        this._name = name;
        this._imgResId = imgResId;
        this._components = components;
    }
	
	/* Set Methods */
	public void setName(String name){
		this._name = name;
	}
	
	public void setImgResId(int imgResId){
		this._imgResId = imgResId;
	}

    public void setComponents(ArrayList<Component> components){
        this._components = components;
    }

	/* Get Methods */
	public String getName(){
		return this._name;
	}
	
	public int getImgResId(){
		return this._imgResId;
	}

    public ArrayList<Component> getComponents(){
        return this._components;
    }

    /* Modifiers */
    public void addComponent(Component c){
        if (!_components.contains(c))
            _components.add(c);
    }

    public void removeComponent(Component c){
        if (_components.contains(c))
            _components.remove(c);
    }
}
