package com.example.homecontrol.model;

/**
 * Created by James on 11/5/2014.
 */
public class Component {
    public static final int TYPE_ERR = -1;
    public static final int TYPE_AC_120V_LIGHT = 0;
    public static final int TYPE_DC_12V_LED_WHITE = 1;
    public static final int TYPE_DC_12V_LED_COLOR = 2;

    public enum Status {
        ON,
        OFF
    }

    private int _type;
    private String _zone;
    private String _ip;
    private Status _status = Status.OFF;


    /**
     * Empty Constructor
     */
    public Component(){}

    /**
     * Initialization constructor.
     * @param type  Type of component: AC_LIGHT, DC_LED_LIGHT, etc.
     * @param zone  Zone the component belongs to.
     * @param ip    IP address of the component.
     */
    public Component(String zone, int type, String ip){
        this._type = type;
        this._zone = zone;
        this._ip = ip;
    }

    /*  Set methods */
    public void setType(int type){
        this._type = type;
    }

    public void setZone(String zone){
        this._zone = zone;
    }

    public void setIP(String ip){
        this._ip = ip;
    }

    /* Get methods   */
    public int getType(){
        return this._type;
    }

    public String getZone(){
        return this._zone;
    }

    public String getIP(){
        return this._ip;
    }

    public void turnOn(){
        this._status = Status.ON;
    }

    public void turnOff(){
        this._status = Status.OFF;
    }
}
