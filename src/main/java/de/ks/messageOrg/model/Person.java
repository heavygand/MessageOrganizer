package de.ks.messageOrg.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Person extends Node {

    private String title;
    private boolean is_still_participant;
    private String thread_type;
    private String thread_path;
    
    
    
    /**
    *   F I E L D  TITLE
    */
    public static final String PROPERTY_TITLE = "title";
    public String getTitle() {
    
        return this.title;
    }
    public void setTitle(String value) {
    
        if (this.title != value)
          {         
            String oldValue = this.title;
            this.title = value;
            firePropertyChange(PROPERTY_TITLE, oldValue, value);
          }
    }
    public Person withTitle(String value) {
    
        setTitle(value);
        return this;
    }
    /**
    *   F I E L D  IS_STILL_PARTICIPANT
    */
    public static final String PROPERTY_IS_STILL_PARTICIPANT = "is_still_participant";
    public boolean getIs_still_participant() {
    
        return this.is_still_participant;
    }
    public void setIs_still_participant(boolean value) {
    
        if (this.is_still_participant != value)
          {         
            boolean oldValue = this.is_still_participant;
            this.is_still_participant = value;
            firePropertyChange(PROPERTY_IS_STILL_PARTICIPANT, oldValue, value);
          }
    }
    public Person withIs_still_participant(boolean value) {
    
        setIs_still_participant(value);
        return this;
    }
    /**
    *   F I E L D  THREAD_TYPE
    */
    public static final String PROPERTY_THREAD_TYPE = "thread_type";
    public String getThread_type() {
    
        return this.thread_type;
    }
    public void setThread_type(String value) {
    
        if (this.thread_type != value)
          {         
            String oldValue = this.thread_type;
            this.thread_type = value;
            firePropertyChange(PROPERTY_THREAD_TYPE, oldValue, value);
          }
    }
    public Person withThread_type(String value) {
    
        setThread_type(value);
        return this;
    }
    /**
    *   F I E L D  THREAD_PATH
    */
    public static final String PROPERTY_THREAD_PATH = "thread_path";
    public String getThread_path() {
    
        return this.thread_path;
    }
    public void setThread_path(String value) {
    
        if (this.thread_path != value)
          {         
            String oldValue = this.thread_path;
            this.thread_path = value;
            firePropertyChange(PROPERTY_THREAD_PATH, oldValue, value);
          }
    }
    public Person withThread_path(String value) {
    
        setThread_path(value);
        return this;
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
