package de.ks.messageOrg.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;
import java.util.HashSet;

public class Node {

    private Set<Node> kid;
    
    
    
    /********************************************************************
    * <pre>
    *              many                       many
    * Node ----------------------------------- Node
    *              kid                   kid
    * </pre>
    */
    
    public static final String PROPERTY_KID = "kid";
    
    public HashSet<Node> getKid()
    {
      if (this.kid == null)
      {
         return new HashSet<Node>();
      }
    
      return (HashSet<Node>)this.kid;
    }
    
    public Node withKid(Node... value)
    {
      if(value==null){
         return this;
      }
      for (Node item : value)
      {
         if (item != null)
         {
            if (this.kid == null)
            {
               this.kid = new HashSet<Node>();
            }
            
            boolean changed = this.kid.add (item);
    
            if (changed)
            {
               item.withKid(this);
               getPropertyChangeSupport().firePropertyChange(PROPERTY_KID, null, item);
            }
         }
      }
      return this;
    } 
    
    public Node withoutKid(Node... value)
    {
      for (Node item : value)
      {
         if ((this.kid != null) && (item != null))
         {
            if (this.kid.remove(item))
            {
               item.withoutKid(this);
               getPropertyChangeSupport().firePropertyChange(PROPERTY_KID, item, null);
            }
         }
      }
      return this;
    }
    
    public Node createKid()
    {
      Node value = new Node();
      withKid(value);
      return value;
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
