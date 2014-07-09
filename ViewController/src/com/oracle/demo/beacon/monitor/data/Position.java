package com.oracle.demo.beacon.monitor.data;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Position {

    private double x;
    private double y;
    private double z;
    private double distance;
    
    private double markerSize;
    private String borderColor;
    private String color;
    private String tooltip;

    String uuid;
    int major;
    int minor;

    public void setDistance(double distance) {
        double oldDistance = this.distance;
        this.distance = distance;
        propertyChangeSupport.firePropertyChange("distance", oldDistance, distance);
    }

    public double getDistance() {
        return distance;
    }

    public void setUuid(String uuid) {
        String oldUuid = this.uuid;
        this.uuid = uuid;
        propertyChangeSupport.firePropertyChange("uuid", oldUuid, uuid);
    }

    public String getUuid() {
        return uuid;
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

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Position() {
        super();
        
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        
        this.markerSize = 10.0;
        this.borderColor = "black";
        this.color = "white";
        this.tooltip = "Beacon Position";
        
        this.uuid = "no-uuid";
        this.major = 0;
        this.minor = 0;
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
    public Position(double x,double y,double z,double markerSize,String borderColor,String color,String tooltip,String uuid,int major,int minor){
        
        super();
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.markerSize = markerSize;
        this.borderColor = borderColor;
        this.color = color;
        this.tooltip = tooltip;
        
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }

    public void setX(double x) {
        double oldX = this.x;
        this.x = x;
        propertyChangeSupport.firePropertyChange("x", oldX, x);
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        double oldY = this.y;
        this.y = y;
        propertyChangeSupport.firePropertyChange("y", oldY, y);
    }

    public double getY() {
        return y;
    }

    public void setZ(double z) {
        double oldZ = this.z;
        this.z = z;
        propertyChangeSupport.firePropertyChange("z", oldZ, z);
    }

    public double getZ() {
        return z;
    }

    public void setMarkerSize(double markerSize) {
        double oldMarkerSize = this.markerSize;
        this.markerSize = markerSize;
        propertyChangeSupport.firePropertyChange("markerSize", oldMarkerSize, markerSize);
    }

    public double getMarkerSize() {
        return markerSize;
    }

    public void setBorderColor(String borderColor) {
        String oldBorderColor = this.borderColor;
        this.borderColor = borderColor;
        propertyChangeSupport.firePropertyChange("borderColor", oldBorderColor, borderColor);
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setColor(String color) {
        String oldColor = this.color;
        this.color = color;
        propertyChangeSupport.firePropertyChange("color", oldColor, color);
    }

    public String getColor() {
        return color;
    }

    public void setTooltip(String tooltip) {
        String oldTooltip = this.tooltip;
        this.tooltip = tooltip;
        propertyChangeSupport.firePropertyChange("tooltip", oldTooltip, tooltip);
    }

    public String getTooltip() {
        return tooltip;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
