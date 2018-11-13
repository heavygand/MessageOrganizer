package de.uniks.studyright.util;
import de.uniks.studyright.Student;
import de.uniks.networkparser.list.SimpleSet;
import java.util.Collection;
import de.uniks.networkparser.list.NumberList;
import de.uniks.networkparser.list.ObjectSet;
import de.uniks.studyright.University;

public class StudentSet extends SimpleSet<Student>
{

   public Class<?> getTypClass()
   {
      return Student.class;
   }

   public StudentSet()
   {
      // empty
   }

   public StudentSet(Student... objects)
   {
      for (Student obj : objects)
      {
         this.add(obj);
      }
   }

   public StudentSet(Collection<Student> objects)
   {
      this.addAll(objects);
   }
		public static final StudentSet EMPTY_SET = new StudentSet().withFlag(StudentSet.READONLY);

   public String getEntryType()
   {
      return "de.uniks.studyright.Student";
   }
   @Override   public StudentSet getNewList(boolean keyValue)
   {
      return new StudentSet();
   }

   @SuppressWarnings("unchecked")
   public StudentSet with(Object value)
   {
      if (value == null)
      {
         return this;
      }
      else if (value instanceof java.util.Collection)
      {
         this.addAll((Collection<Student>)value);
      }
      else if (value != null)
      {
         this.add((Student) value);
      }
      return this;
   }

   public NumberList getMatNo()
   {
      NumberList result = new NumberList();
      for (Student obj : this)
      {
         result.add(obj.getMatNo());
      }
      return result;
   }

   public StudentSet filterMatNo(int value)
   {
      StudentSet result = new StudentSet();
      for(Student obj : this)
      {
         if (value == obj.getMatNo())
         {
            result.add(obj);
         }
      }
      return result;
   }

   public StudentSet withMatNo(int value) {
      for (Student obj : this)
      {
         obj.setMatNo(value);
      }
      return this;
   }
   public StudentSet getUni()
   {
      StudentSet result = new StudentSet();
      for (Student obj : this)
      {
         result.with(obj.getUni());
      }
      return result;
   }

   public StudentSet filterUni(Object value)
   {
      ObjectSet neighbors = new ObjectSet();
      if (value instanceof Collection)
      {
         neighbors.addAll((Collection<?>) value);
      }
      else
      {
         neighbors.add(value);
      }
      StudentSet answer = new StudentSet();
      for (Student obj : this)
      {
         if (neighbors.contains(obj.getUni()) || (neighbors.isEmpty() && obj.getUni() == null))
         {
            answer.add(obj);
         }
      }
      return answer;
   }

   public StudentSet withUni(University value)
   {
      for (Student obj : this)
      {
         obj.withUni(value);
      }
      return this;
   }
}