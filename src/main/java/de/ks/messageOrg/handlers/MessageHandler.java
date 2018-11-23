package de.ks.messageOrg.handlers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.app.MainApp;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.util.MessageCreator;

public class MessageHandler implements Handler {

    
    
    
    
    
    //                          Operations
    
    public boolean handle(String key, JSONArray jSONArray) {
        
    	if(!key.equals("messages")) return false;
    	
    	ArrayList<Message> messages = new ArrayList<>();
    	
    	jSONArray.forEach(str -> {
    		
    		Message newMessage = new Message();
        	
    		JSONObject messageData = (JSONObject)str;
    		
    		setAttributes(newMessage, messageData);
    		
    		messages.add(newMessage);
    	});
    	
    	MainApp.getCurrentPerson().setMessages(messages);
		
		return true;
    }

	/**
	 * @param message
	 * @param mc
	 * @param messageObject
	 */
	private void setAttributes(Message message, JSONObject messageObject) {
		
		MessageCreator mc = new MessageCreator();

		messageObject.keys().forEachRemaining(messObjKey -> {
			
			mc.setValue(message, messObjKey, messageObject.get(messObjKey), null);
		});
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
