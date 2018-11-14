package de.ks.messageOrg.handlers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageHandler extends Handler {

    
    
    
    
    
    //                          Operations
    
    public boolean handle(String key, Object value) {
        
    	if(key.equals("messages")) {
    		
    		String valueString = value.toString();
    		
    		System.out.println(valueString.toString());
    		
			JSONArray json = new JSONArray(valueString);
			json.forEach(jsonObj -> {

	    		System.out.println(jsonObj.toString());
			});
    		
    		return true;
    	}
    	return false;
    }
    
    
/**
*   PROPERTYCHANGESTUFF
*/
    protected PropertyChangeSupport listeners = null;
    
    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (listeners != null) {
           listeners.firePropertyChange(propertyName, oldValue, newValue);
           return true;
        }
        return false;
     }

     public boolean addPropertyChangeListener(PropertyChangeListener listener)
     {
        if (listeners == null) {
           listeners = new PropertyChangeSupport(this);
        }
        listeners.addPropertyChangeListener(listener);
        return true;
     }

     public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
     {
        if (listeners == null) {
           listeners = new PropertyChangeSupport(this);
        }
        listeners.addPropertyChangeListener(propertyName, listener);
        return true;
     }

     public boolean removePropertyChangeListener(PropertyChangeListener listener)
     {
        if (listeners != null) {
           listeners.removePropertyChangeListener(listener);
        }
        listeners.removePropertyChangeListener(listener);
        return true;
     }

     public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
     {
        if (listeners != null) {
           listeners.removePropertyChangeListener(propertyName, listener);
        }
        return true;
     }
	        
    public PropertyChangeSupport getPropertyChangeSupport()
    {
    	if(listeners == null) listeners = new PropertyChangeSupport(this);
    	
       return listeners;
    }
}
