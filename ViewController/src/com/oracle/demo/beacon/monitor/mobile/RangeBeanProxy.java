package com.oracle.demo.beacon.monitor.mobile;

import com.sun.util.logging.Level;

import javax.el.PropertyNotFoundException;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class RangeBeanProxy {
    public RangeBeanProxy() {
    }
    
    public void startBeaconListUpdate(){
        ((RangeBean)(AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.RangeBean}"))).startBeaconListUpdate(); 
    }
    
    public void addBeaconToList(BeaconBean beacon){
        ((RangeBean)(AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.RangeBean}"))).addBeaconToList(beacon); 
    }

    public void endBeaconListUpdate(){
        ((RangeBean)(AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.RangeBean}"))).endBeaconListUpdate();
        
        try{
            AdfmfJavaUtilities.evaluateELExpression("#{bindings.updatePosition.execute}");
            // Trace.log(Utility.FrameworkLogger,Level.SEVERE,RangeBeanProxy.class, "endBeaconListUpdate", "Updated position for chart");   
        }
        catch(PropertyNotFoundException pnfe){
            // NOOP
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,RangeBeanProxy.class, "endBeaconListUpdate", ex);   
        }
        // AdfmfJavaUtilities.flushDataChangeEvent();
    }
}
