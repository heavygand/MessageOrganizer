package de.uniks.studyright.util;
import de.uniks.networkparser.interfaces.SendableEntityCreator;
import de.uniks.studyright.University;
import de.uniks.studyright.Student;
import de.uniks.studyright.Room;
import de.uniks.networkparser.IdMap;


public class UniversityCreator implements SendableEntityCreator
{

   private final String[] properties = new String[]
   {
      University.PROPERTY_NAME,
      University.PROPERTY_STUDENTS,
      University.PROPERTY_ROOMS,
   };

   @Override
   public String[] getProperties()
   {
      return properties;
   }

   @Override
   public Object getSendableInstance(boolean prototyp)
   {
      return new University();
   }

   @Override
   public Object getValue(Object entity, String attribute)
   {
      if(attribute == null || entity instanceof University == false) {
          return null;
      }
      University element = (University)entity;
      int pos = attribute.indexOf('.');
      String attrName = attribute;

      if (pos > 0)
      {
         attrName = attribute.substring(0, pos);
      }
      if(attrName.length()<1) {
         return null;
      }

      if (University.PROPERTY_NAME.equalsIgnoreCase(attrName))
      {
         return element.getName();
      }

      if (University.PROPERTY_STUDENTS.equalsIgnoreCase(attrName))
      {
         return element.getStudents();
      }

      if (University.PROPERTY_ROOMS.equalsIgnoreCase(attrName))
      {
         return element.getRooms();
      }

      return null;
   }

   @Override
   public boolean setValue(Object entity, String attribute, Object value, String type)
   {
      if(attribute == null || entity instanceof University == false) {
          return false;
      }
      University element = (University)entity;
      if (SendableEntityCreator.REMOVE.equals(type) && value != null)
      {
         attribute = attribute + type;
      }

      if (University.PROPERTY_NAME.equalsIgnoreCase(attribute))
      {
         element.setName((String) value);
         return true;
      }

      if (University.PROPERTY_STUDENTS.equalsIgnoreCase(attribute))
      {
         element.withStudents((Student) value);
         return true;
      }

      if (University.PROPERTY_ROOMS.equalsIgnoreCase(attribute))
      {
         element.withRooms((Room) value);
         return true;
      }

      return false;
   }
    public IdMap createMap(String session) {
 	   return CreatorCreator.createIdMap(session);
    }
}