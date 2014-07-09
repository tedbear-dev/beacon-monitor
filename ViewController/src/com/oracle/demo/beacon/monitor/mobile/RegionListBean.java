package com.oracle.demo.beacon.monitor.mobile;

import com.oracle.demo.beacon.monitor.data.Position;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.dc.ws.rest.RestServiceAdapter;
import oracle.adfmf.framework.api.JSONBeanSerializationHelper;
import oracle.adfmf.framework.api.Model;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class RegionListBean {
    
    ArrayList listOfRegions = new ArrayList();

    public ArrayList getListOfRegions() {
        return listOfRegions;
    }

    public RegionListBean() {
        super();
        
        RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();

        restServiceAdapter.clearRequestProperties();
        restServiceAdapter.setConnectionName("Uxh");
        restServiceAdapter.setRequestType(RestServiceAdapter.REQUEST_TYPE_GET);
        restServiceAdapter.setRetryLimit(0);
        restServiceAdapter.setRequestURI("/proximityUuids");

        String response = "";

        // Execute SEND and RECEIVE operation
        try {
           // For GET request, there is no payload
           response = restServiceAdapter.send("");
            JSONApexResult result = (JSONApexResult)(new JSONBeanSerializationHelper()).fromJSON(JSONApexResult.class, response);

            JSONArray arr = result.getItems();
            for(int i = 0;i < arr.length();i++){

                JSONObject obj = arr.getJSONObject(i); 
                HashMap el = new HashMap();
                el.put("id",new Integer(obj.getInt("id")));
                el.put("name",obj.getString("app_name"));
                el.put("uuid",obj.getString("uuid"));
                listOfRegions.add(el);                
            }
        }
        catch (Exception e) {
           e.printStackTrace();
        }
        
        if(listOfRegions.isEmpty()){
            HashMap el = new HashMap();
            el.put("id",new Integer(-1));
            el.put("name","FakeRegion");
            el.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            listOfRegions.add(el);      
        }
    }
    
    public void regionClicked(ActionEvent actionEvent) {
        // Add event code here...
    }
}
