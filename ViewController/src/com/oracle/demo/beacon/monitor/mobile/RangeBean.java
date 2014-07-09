package com.oracle.demo.beacon.monitor.mobile;

import com.oracle.demo.beacon.monitor.mobile.BeaconComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class RangeBean {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private HashMap currentRegion = new HashMap();
    private ArrayList beaconList = new ArrayList();
    private ArrayList oldBeaconList;

    public void startBeaconListUpdate(){
        oldBeaconList = beaconList;
        beaconList = new ArrayList();
    }

    public void addBeaconToList(BeaconBean beacon){
        beaconList.add(beacon);
        
        BeaconBean selectedBeacon = (BeaconBean)(AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.SelectedBeaconBean}"));
        if(selectedBeacon != null){
            if(beacon.getProximityUUID().equals(selectedBeacon.getProximityUUID()) &&
                beacon.getMajor() == selectedBeacon.getMajor() &&
                beacon.getMinor() == selectedBeacon.getMinor()
            ){
                selectedBeacon.copyFrom(beacon);
            }
        }
    }
    
    
    public void endBeaconListUpdate(){   
        
        BeaconComparator comparator = new BeaconComparator();
        
        Collections.sort(beaconList,comparator);
        propertyChangeSupport.firePropertyChange("beaconList", oldBeaconList, beaconList);
    }
    
    public ArrayList getBeaconList(){
        return beaconList;
    }
    
    public void setCurrentRegion(HashMap currentRegion) {
        
        HashMap oldCurrentRegion = this.currentRegion;
        this.currentRegion = currentRegion;
        
        propertyChangeSupport.firePropertyChange("currentRegion", oldCurrentRegion, currentRegion);
    }

    public HashMap getCurrentRegion() {
        return currentRegion;
    }

    public RangeBean() {
    }

    public void startRanging() {
        
        String device = (String)(AdfmfJavaUtilities.evaluateELExpression("#{deviceScope.device.name}"));
        
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            "com.oracle.demo.beacon.monitor.range",
            "window.beaconPlugin.startMonitoring",
            new Object[] {
                this.currentRegion.get("uuid"),
                this.currentRegion.get("name"),
                (device.indexOf("Simulator") >= 0) ? Boolean.TRUE : Boolean.FALSE // Is this the simulator? If so, use fake data
            }
        );
        
        AdfmfJavaUtilities.evaluateELExpression("#{bindings.setBeaconPositions.execute}");
    }

    public void stopRanging() {
        // TODO: Stop ranging
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
