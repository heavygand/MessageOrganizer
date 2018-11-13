package de.uniks.studyright;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import de.uniks.studyright.Room;
import de.uniks.studyright.Student;
import de.uniks.studyright.University;


public class Student
{
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
   public static final String PROPERTY_MATNO = "matNo";

   private int matNo;

   public int getMatNo()
   {
      return this.matNo;
   }

   public void setMatNo(int value)
   {
      if (this.matNo != value)
      {         int oldValue = this.matNo;
         this.matNo = value;
         firePropertyChange(PROPERTY_MATNO, oldValue, value);
      }
   }

   public Student withMatNo(int value)
   {
      setMatNo(value);
      return this;
   }


   public static final String PROPERTY_UNI = "uni";

   private University uni = null;

   public University getUni()
   {
      return this.uni;
   }

   public boolean setUni(University value)
   {
      boolean changed = false;
      if (this.uni != value) {
         University oldValue = this.uni;
         if (this.uni != null) {
            this.uni = null;
            oldValue.withoutStudents(this);
         }
         this.uni = value;
         if (value != null) {
            value.withStudents(this);
         }
         firePropertyChange(PROPERTY_UNI, oldValue, value);
         changed = true;
      }
      return changed;
   }

   public Student withUni(University value)
   {
      this.setUni(value);
      return this;
   }

   public University createUni()
   {
      University value = new University();
      withUni(value);
      return value;
   }
}