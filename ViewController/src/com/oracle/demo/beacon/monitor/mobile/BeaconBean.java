package com.oracle.demo.beacon.monitor.mobile;

import java.util.Comparator;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class BeaconBean {
    
    private int rssi = 1;
    private float accuracy = 0.0f;
    private String proximityUUID = "";
    private int proximity = -1;
    private String regionID = "";
    private int major = 0;
    private int minor = 0;
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void reset(){
        rssi = 1;
        accuracy = 0.0f;
        proximityUUID = "";
        proximity = -1;
        regionID = "";
        major = 0;
        minor = 0;        
    }
    
    public void copyFrom(BeaconBean other){
        setRssi(other.getRssi());
        setAccuracy(other.getAccuracy());
        setProximityUUID(other.getProximityUUID());
        setProximity(other.getProximity());
        setRegionID(other.getRegionID());
        setMajor(other.getMajor());
        setMinor(other.getMinor());
    }
    
    public void setRssi(int rssi) {
        int oldRssi = this.rssi;
        this.rssi = rssi;
        propertyChangeSupport.firePropertyChange("rssi", oldRssi, rssi);
    }

    public int getRssi() {
        return rssi;
    }

    public void setAccuracy(float accuracy) {
        float oldAccuracy = this.accuracy;
        this.accuracy = accuracy;
        propertyChangeSupport.firePropertyChange("accuracy", oldAccuracy, accuracy);
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setProximityUUID(String proximityUUID) {
        String oldProximityUUID = this.proximityUUID;
        this.proximityUUID = proximityUUID;
        propertyChangeSupport.firePropertyChange("proximityUUID", oldProximityUUID, proximityUUID);
    }

    public String getProximityUUID() {
        return proximityUUID;
    }

    public void setProximity(int proximity) {
        int oldProximity = this.proximity;
        this.proximity = proximity;
        propertyChangeSupport.firePropertyChange("proximity", oldProximity, proximity);
    }

    public int getProximity() {
        return proximity;
    }

    public void setRegionID(String regionID) {
        String oldRegionID = this.regionID;
        this.regionID = regionID;
        propertyChangeSupport.firePropertyChange("regionID", oldRegionID, regionID);
    }

    public String getRegionID() {
        return regionID;
    }

    public void setMajor(int major) {
        int oldMajor = this.major;
        this.major = major;
        propertyChangeSupport.firePropertyChange("major", oldMajor, major);
    }

    public int getMajor() {
        return major;
    }

    public void setMinor(int minor) {
        int oldMinor = this.minor;
        this.minor = minor;
        propertyChangeSupport.firePropertyChange("minor", oldMinor, minor);
    }

    public int getMinor() {
        return minor;
    }
                                                                                                                                                            
    public BeaconBean() {
        super();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
