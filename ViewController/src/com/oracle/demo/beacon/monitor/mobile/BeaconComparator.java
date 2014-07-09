package com.oracle.demo.beacon.monitor.mobile;

import java.util.Comparator;

    public class BeaconComparator implements Comparator {
        public int compare(Object a, Object b) {
                
            int minorA = ((BeaconBean)a).getMinor();
            int majorA = ((BeaconBean)a).getMajor();
            
            int minorB = ((BeaconBean)b).getMinor();
            int majorB = ((BeaconBean)b).getMajor();
            
            int rc = 0;
            
            if(majorA < majorB) rc = -1;
            else if(majorA > majorB) rc = 1;
            else{
                if(minorA < minorB) rc = -1;
                else if(minorA > minorB) rc = 1;    
            }
                
            return rc;                                                                     
        }
    };