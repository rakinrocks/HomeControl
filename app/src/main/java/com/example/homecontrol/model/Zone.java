package com.example.homecontrol.model;

import java.util.ArrayList;


public class Zone {

    ArrayList<Component> _components;    // list of components associated with zone (i.e. lights)
    ArrayList<Module> _modules;    // list of modules associated with zone (i.e. modules)
    private String _name;    // name of zone
    private int _imgResId;    // resource id for icon

    /* constructors */
    public Zone() {
    }

    public Zone(String name) {
        this._name = name;
        this._components = null;
    }

    public Zone(String name, int imgResId) {
        this._name = name;
        this._imgResId = imgResId;
        this._components = null;
    }

    public Zone(String name, int imgResId, ArrayList<Component> components) {
        this._name = name;
        this._imgResId = imgResId;
        this._components = components;
    }

    public Zone(String name, int imgResId, ArrayList<Component> components, ArrayList<Module> modules) {
        this._name = name;
        this._imgResId = imgResId;
        this._components = components;
        this._modules = modules;
    }

    /* Get Methods */
    public String getName() {
        return this._name;
    }

    /* Set Methods */
    public void setName(String name) {
        this._name = name;
    }

    public int getImgResId() {
        return this._imgResId;
    }

    public void setImgResId(int imgResId) {
        this._imgResId = imgResId;
    }

    public ArrayList<Component> getComponents() {
        return this._components;
    }

    public void setComponents(ArrayList<Component> components) {
        this._components = components;
    }

    public ArrayList<Module> getModules() {
        return this._modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this._modules = modules;
    }

    /* Modifiers */
    public void addComponent(Component c) {
        if (!_components.contains(c))
            _components.add(c);
    }

    public void removeComponent(Component c) {
        if (_components.contains(c))
            _components.remove(c);
    }

    public void addModule(Module m) {
        if (!_modules.contains(m))
            _modules.add(m);
    }

    public void removeModule(Module m) {
        if (_modules.contains(m))
            _modules.remove(m);
    }
}
