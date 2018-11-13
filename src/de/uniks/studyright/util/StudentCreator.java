package de.uniks.studyright.util;
import de.uniks.networkparser.interfaces.SendableEntityCreator;
import de.uniks.studyright.Student;
import de.uniks.studyright.University;
import de.uniks.networkparser.IdMap;


public class StudentCreator implements SendableEntityCreator
{

   private final String[] properties = new String[]
   {
      Student.PROPERTY_MATNO,
      Student.PROPERTY_UNI,
   };

   @Override
   public String[] getProperties()
   {
      return properties;
   }

   @Override
   public Object getSendableInstance(boolean prototyp)
   {
      return new Student();
   }

   @Override
   public Object getValue(Object entity, String attribute)
   {
      if(attribute == null || entity instanceof Student == false) {
          return null;
      }
      Student element = (Student)entity;
      int pos = attribute.indexOf('.');
      String attrName = attribute;

      if (pos > 0)
      {
         attrName = attribute.substring(0, pos);
      }
      if(attrName.length()<1) {
         return null;
      }

      if (Student.PROPERTY_MATNO.equalsIgnoreCase(attrName))
      {
         return element.getMatNo();
      }

      if (Student.PROPERTY_UNI.equalsIgnoreCase(attrName))
      {
         return element.getUni();
      }

      return null;
   }

   @Override
   public boolean setValue(Object entity, String attribute, Object value, String type)
   {
      if(attribute == null || entity instanceof Student == false) {
          return false;
      }
      Student element = (Student)entity;
      if (SendableEntityCreator.REMOVE.equals(type) && value != null)
      {
         attribute = attribute + type;
      }

      if (Student.PROPERTY_MATNO.equalsIgnoreCase(attribute))
      {
         element.setMatNo((int) value);
         return true;
      }

      if (Student.PROPERTY_UNI.equalsIgnoreCase(attribute))
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