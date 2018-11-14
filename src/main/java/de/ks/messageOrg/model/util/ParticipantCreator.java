package de.ks.messageOrg.model.util;

import de.uniks.networkparser.interfaces.SendableEntityCreator;
import de.uniks.networkparser.IdMap;
import de.ks.messageOrg.model.Participant;

public class ParticipantCreator implements SendableEntityCreator {
    
   private final String[] properties = new String[]
   {
      Participant.PROPERTY_NAME,
      
   };

   @Override
   public String[] getProperties()
   {
      return properties;
   }

   @Override
   public Object getSendableInstance(boolean prototyp)
   {
      return new Participant();
   }

   @Override
   public Object getValue(Object entity, String attribute)
   {
      if(attribute == null || entity instanceof Participant == false) {
          return null;
      }
      Participant element = (Participant)entity;
      int pos = attribute.indexOf('.');
      String attrName = attribute;

      if (pos > 0)
      {
         attrName = attribute.substring(0, pos);
      }
      if(attrName.length()<1) {
         return null;
      }

      if (Participant.PROPERTY_NAME.equalsIgnoreCase(attrName))
      {
          return element.getName();
      }
      

      return null;
   }

   @Override
   public boolean setValue(Object entity, String attribute, Object value, String type)
   {
      if(attribute == null || entity instanceof Participant == false) {
          return false;
      }
      Participant element = (Participant)entity;
      if (SendableEntityCreator.REMOVE.equals(type) && value != null)
      {
         attribute = attribute + type;
      }

      if (Participant.PROPERTY_NAME.equalsIgnoreCase(attribute))
      {
          element.setName((String) value);
          return true;
      }
      

      return false;
   }
    public IdMap createMap(String session) {
 	   return CreatorCreator.createIdMap(session);
    }
}
