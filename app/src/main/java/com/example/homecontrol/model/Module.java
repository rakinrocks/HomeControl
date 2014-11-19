package com.example.homecontrol.model;

public class Module {

    public static final int TYPE_ERR = -1;
    public static final int MOD_ON = 0;
    public static final int MOD_OFF = 1;
    public static final int MOD_NA = 2;

    private String _zone;
    private String _name;    // name of the module
    private int _status;    // status of the module
    private String _type;    // module type

    /* Empty constructor */
    public Module() {
    }

    /**
     * Initialization constructor.
     *
     * @param zone   Zone the module belongs to.
     * @param name   Name of the module.
     * @param status Status of module: ON, OFF, etc.
     * @param type   Type of the module.
     */
    public Module(String zone, String name, int status, String type) {
        this._zone = zone;
        this._name = name;
        this._status = status;
        this._type = type;
    }

    public void setModule(String type) {
        this._type = type;
    }

    /* Get Methods */
    public String getZone() {
        return this._zone;
    }

    /* Set Methods */
    public void setZone(String zone) {
        this._zone = zone;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getStatus() {
        return this._status;
    }

    public void setStatus(int status) {
        this._status = status;
    }

    public String getType() {
        return this._type;
    }

}
