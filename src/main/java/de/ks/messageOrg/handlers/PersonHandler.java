package de.ks.messageOrg.handlers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.json.JSONArray;

import de.ks.messageOrg.app.MainApp;
import de.ks.messageOrg.model.util.PersonCreator;

public class PersonHandler implements Handler {

    
    
    
    
    
    //                          Operations
    
    public boolean handle(String key, JSONArray value) {
        
    	if(		key.equals("is_still_participant") ||
    			key.equals("thread_type") ||
    			key.equals("title") ||
    			key.equals("thread_path")) {
    		
    		PersonCreator personCreator = new PersonCreator();
    		personCreator.setValue(MainApp.getCurrentPerson(), key, value, null);
    		
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
