package de.ks.messageOrg.handlers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.app.MainApp;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Participant;
import de.ks.messageOrg.model.util.MessageCreator;
import de.ks.messageOrg.model.util.ParticipantCreator;

public class ParticipantHandler implements Handler {

    
    
    
    
    
    //                          Operations
    
    public boolean handle(String key, JSONArray jSONArray) {
        
    	if(!key.equals("participants")) return false;
    	
    	jSONArray.forEach(str -> {
    		
    		Participant newParticipant = new Participant();
        	
    		JSONObject participantData = (JSONObject)str;
    		
    		setAttributes(newParticipant, participantData);
    		
    		MainApp.getCurrentPerson().withKid(newParticipant);
    	});
		
		return true;
    }

	/**
	 * @param participant
	 * @param mc
	 * @param participantObject
	 */
	private void setAttributes(Participant participant, JSONObject participantObject) {
		
		ParticipantCreator pc = new ParticipantCreator();

		participantObject.keys().forEachRemaining(partObjKey -> {
			
			pc.setValue(participant, partObjKey, participantObject.get(partObjKey), null);
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
