package com.oracle.demo.beacon.monitor.data;

import com.oracle.demo.beacon.monitor.math.Point3D;
import com.oracle.demo.beacon.monitor.math.Sphere;
import com.oracle.demo.beacon.monitor.math.SphereIntersector;
import com.oracle.demo.beacon.monitor.mobile.BeaconBean;
import com.oracle.demo.beacon.monitor.mobile.JSONApexResult;
import com.oracle.demo.beacon.monitor.mobile.RangeBean;

import com.oracle.demo.beacon.monitor.mobile.RangeBeanProxy;

import com.sun.util.logging.Level;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import oracle.adfmf.dc.ws.rest.RestServiceAdapter;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.api.JSONBeanSerializationHelper;
import oracle.adfmf.framework.api.Model;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class PositionList {
    
    private ProviderChangeSupport providerChangeSupport = new ProviderChangeSupport(this);
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private ArrayList positionHistory;
    private ArrayList positions;
    private HashMap beacons;
    private long startTime;
    
    public PositionList() {
        super();
        
        positions = new ArrayList();
        positionHistory = new ArrayList();
        beacons = new HashMap();
        startTime = (new Date()).getTime();
    }
    
    private Sphere createSphere(Object obj){
    
        Position pos = (Position)obj;
        return new Sphere(new Point3D(pos.getX(),pos.getY(),pos.getZ()),pos.getDistance());
    }
    
    public synchronized void setBeaconPositions(String uuid){
        
        Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "setBeaconPositions", "Setting beacon positions from web service for: " + uuid);   
        
        beacons = new HashMap();
    
        RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();

        restServiceAdapter.clearRequestProperties();
        restServiceAdapter.setConnectionName("Uxh");
        restServiceAdapter.setRequestType(RestServiceAdapter.REQUEST_TYPE_GET);
        restServiceAdapter.setRetryLimit(0);
        
        // Get from https://apex.oraclecorp.com/pls/apex/futl/uxh/beaconPositions/{uuid}
        restServiceAdapter.setRequestURI("/beaconPositions/"+uuid);
        
        String response = "";

        // Execute SEND and RECEIVE operation
        try {
           // For GET request, there is no payload
           response = restServiceAdapter.send("");
            JSONApexResult result = (JSONApexResult)(new JSONBeanSerializationHelper()).fromJSON(JSONApexResult.class, response);

            JSONArray arr = result.getItems();
            for(int i = 0;i < arr.length();i++){

                JSONObject obj = arr.getJSONObject(i); 
                Position pos = new Position();
                
                pos.setUuid(obj.getString("uuid"));
                pos.setMajor(obj.getInt("major"));
                pos.setMinor(obj.getInt("minor"));
                pos.setX(obj.getDouble("x"));
                pos.setY(obj.getDouble("y"));
                pos.setZ(obj.getDouble("z"));
                pos.setColor(obj.getString("color"));

                beacons.put(pos.getMajor()+"."+pos.getMinor(),pos);
            }
        }
        catch (Exception e) {
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "setBeaconPositions", e);   
        }
        
        Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "setBeaconPositions", "Found beacons: " + beacons.size());   
    }
    
    public synchronized void resetPositionHistory(){

        positionHistory = new ArrayList();
        startTime = (new Date()).getTime();

        providerChangeSupport.fireProviderRefresh("positionHistory");
        propertyChangeSupport.firePropertyChange("positionHistory", null, positionHistory);

        AdfmfJavaUtilities.flushDataChangeEvent();
    }
    
    public synchronized void updatePosition(){
    
        // Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "updatePosition", "Updating position for chart");   
        
        RangeBean rb = ((RangeBean)(AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.RangeBean}")));
        ArrayList bs = rb.getBeaconList();

        positions = new ArrayList();
        
        long nowTime = (new Date()).getTime() - startTime;
        
        Iterator i = bs.iterator();
        while(i.hasNext()){
            BeaconBean b = (BeaconBean)i.next();
            double dist = b.getAccuracy(); 
            if(dist > 0.0){
                Position p = (Position)beacons.get(b.getMajor()+"."+b.getMinor());
                if(p != null){
                    p.setDistance(dist);               
                    positions.add(p);
                    // Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "updatePosition", "Adding " +p.getColor()+", with accuracy " + dist);   
                    
                    Position ph = new Position();
                    ph.setUuid(p.getUuid());
                    ph.setMajor(p.getMajor());
                    ph.setMinor(p.getMinor());
                    
                    ph.setX(((double)nowTime)/1000.0);
                    ph.setY(dist);
                    
                    ph.setColor(p.getColor());
                    
                    positionHistory.add(ph);                                
                }
            }
        }
        
        if(positions.size() >= 3){
            Sphere a = createSphere(positions.get(0));
            Sphere b = createSphere(positions.get(1));
            Sphere c = createSphere(positions.get(2));
            
            Point3D[] pos = SphereIntersector.getIntersection(a, b, c, 0.1);
            for(int p = 0;p<pos.length;p++){
                positions.add(new Position(pos[p].x,pos[p].y,pos[p].z,10,null,"black","Device: " + p,"",0,0));
            }
            
            if(pos.length <= 0){
                // Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "updatePosition", "No intersections found.");   
            }
        }
        else{
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,PositionList.class, "updatePosition", "Less than three positions found.");   
        }
        
        providerChangeSupport.fireProviderRefresh("positions");
        
        providerChangeSupport.fireProviderRefresh("positionHistory");
        propertyChangeSupport.firePropertyChange("positionHistory", null, positionHistory);
        
        AdfmfJavaUtilities.flushDataChangeEvent();
    }
    
    public synchronized Position[] getPositions(){

        return (Position[]) positions.toArray(new Position[positions.size()]);
    }
    
    public synchronized Position[] getPositionHistory(){

        return (Position[]) positionHistory.toArray(new Position[positionHistory.size()]);
    }
    
    public void addProviderChangeListener(ProviderChangeListener l){
        providerChangeSupport.addProviderChangeListener(l);
    }
    
    public void removeProviderChangeListener(ProviderChangeListener l){
        providerChangeSupport.removeProviderChangeListener(l);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l){
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l){
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
