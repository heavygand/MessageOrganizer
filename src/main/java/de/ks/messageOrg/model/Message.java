package de.ks.messageOrg.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Message extends Node {

    private String sender_name;
    private long timestamp_ms;
    private String content;
    private String type;
    
    
    
    /**
    *   F I E L D  SENDER_NAME
    */
    public static final String PROPERTY_SENDER_NAME = "sender_name";
    public String getSender_name() {
    
        return this.sender_name;
    }
    public void setSender_name(String value) {
    
        if (this.sender_name != value)
          {         
            String oldValue = this.sender_name;
            this.sender_name = value;
            firePropertyChange(PROPERTY_SENDER_NAME, oldValue, value);
          }
    }
    public Message withSender_name(String value) {
    
        setSender_name(value);
        return this;
    }
    /**
    *   F I E L D  TIMESTAMP_MS
    */
    public static final String PROPERTY_TIMESTAMP_MS = "timestamp_ms";
    public long getTimestamp_ms() {
    
        return this.timestamp_ms;
    }
    public void setTimestamp_ms(long value) {
    
        if (this.timestamp_ms != value)
          {         
            long oldValue = this.timestamp_ms;
            this.timestamp_ms = value;
            firePropertyChange(PROPERTY_TIMESTAMP_MS, oldValue, value);
          }
    }
    public Message withTimestamp_ms(long value) {
    
        setTimestamp_ms(value);
        return this;
    }
    /**
    *   F I E L D  CONTENT
    */
    public static final String PROPERTY_CONTENT = "content";
    public String getContent() {
    
        return this.content;
    }
    public void setContent(String value) {
    
        if (this.content != value)
          {         
            String oldValue = this.content;
            this.content = value;
            firePropertyChange(PROPERTY_CONTENT, oldValue, value);
          }
    }
    public Message withContent(String value) {
    
        setContent(value);
        return this;
    }
    /**
    *   F I E L D  TYPE
    */
    public static final String PROPERTY_TYPE = "type";
    public String getType() {
    
        return this.type;
    }
    public void setType(String value) {
    
        if (this.type != value)
          {         
            String oldValue = this.type;
            this.type = value;
            firePropertyChange(PROPERTY_TYPE, oldValue, value);
          }
    }
    public Message withType(String value) {
    
        setType(value);
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
