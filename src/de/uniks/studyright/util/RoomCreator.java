package de.uniks.studyright.util;
import de.uniks.networkparser.interfaces.SendableEntityCreator;
import de.uniks.studyright.Room;
import de.uniks.studyright.University;
import de.uniks.networkparser.IdMap;


public class RoomCreator implements SendableEntityCreator
{

   private final String[] properties = new String[]
   {
      Room.PROPERTY_ROOMNO,
      Room.PROPERTY_UNI,
   };

   @Override
   public String[] getProperties()
   {
      return properties;
   }

   @Override
   public Object getSendableInstance(boolean prototyp)
   {
      return new Room();
   }

   @Override
   public Object getValue(Object entity, String attribute)
   {
      if(attribute == null || entity instanceof Room == false) {
          return null;
      }
      Room element = (Room)entity;
      int pos = attribute.indexOf('.');
      String attrName = attribute;

      if (pos > 0)
      {
         attrName = attribute.substring(0, pos);
      }
      if(attrName.length()<1) {
         return null;
      }

      if (Room.PROPERTY_ROOMNO.equalsIgnoreCase(attrName))
      {
         return element.getRoomNo();
      }

      if (Room.PROPERTY_UNI.equalsIgnoreCase(attrName))
      {
         return element.getUni();
      }

      return null;
   }

   @Override
   public boolean setValue(Object entity, String attribute, Object value, String type)
   {
      if(attribute == null || entity instanceof Room == false) {
          return false;
      }
      Room element = (Room)entity;
      if (SendableEntityCreator.REMOVE.equals(type) && value != null)
      {
         attribute = attribute + type;
      }

      if (Room.PROPERTY_ROOMNO.equalsIgnoreCase(attribute))
      {
         element.setRoomNo((String) value);
         return true;
      }

      if (Room.PROPERTY_UNI.equalsIgnoreCase(attribute))
      {
         element.setUni((University) value);
         return true;
      }

      return false;
   }
    public IdMap createMap(String session) {
 	   return CreatorCreator.createIdMap(session);
    }
}