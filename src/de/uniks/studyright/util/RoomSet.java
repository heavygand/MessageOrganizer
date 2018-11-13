package de.uniks.studyright.util;
import de.uniks.studyright.Room;
import de.uniks.networkparser.list.SimpleSet;
import java.util.Collection;
import de.uniks.networkparser.list.StringList;
import de.uniks.networkparser.list.ObjectSet;
import de.uniks.studyright.University;

public class RoomSet extends SimpleSet<Room>
{

   public Class<?> getTypClass()
   {
      return Room.class;
   }

   public RoomSet()
   {
      // empty
   }

   public RoomSet(Room... objects)
   {
      for (Room obj : objects)
      {
         this.add(obj);
      }
   }

   public RoomSet(Collection<Room> objects)
   {
      this.addAll(objects);
   }
		public static final RoomSet EMPTY_SET = new RoomSet().withFlag(RoomSet.READONLY);

   public String getEntryType()
   {
      return "de.uniks.studyright.Room";
   }
   @Override   public RoomSet getNewList(boolean keyValue)
   {
      return new RoomSet();
   }

   @SuppressWarnings("unchecked")
   public RoomSet with(Object value)
   {
      if (value == null)
      {
         return this;
      }
      else if (value instanceof java.util.Collection)
      {
         this.addAll((Collection<Room>)value);
      }
      else if (value != null)
      {
         this.add((Room) value);
      }
      return this;
   }

   public StringList getRoomNo()
   {
      StringList result = new StringList();
      for (Room obj : this)
      {
         result.add(obj.getRoomNo());
      }
      return result;
   }

   public RoomSet filterRoomNo(String value)
   {
      RoomSet result = new RoomSet();
      for(Room obj : this)
      {
         if (value == obj.getRoomNo())
         {
            result.add(obj);
         }
      }
      return result;
   }

   public RoomSet withRoomNo(String value) {
      for (Room obj : this)
      {
         obj.setRoomNo(value);
      }
      return this;
   }
   public RoomSet getUni()
   {
      RoomSet result = new RoomSet();
      for (Room obj : this)
      {
         result.with(obj.getUni());
      }
      return result;
   }

   public RoomSet filterUni(Object value)
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
      RoomSet answer = new RoomSet();
      for (Room obj : this)
      {
         if (neighbors.contains(obj.getUni()) || (neighbors.isEmpty() && obj.getUni() == null))
         {
            answer.add(obj);
         }
      }
      return answer;
   }

   public RoomSet withUni(University value)
   {
      for (Room obj : this)
      {
         obj.withUni(value);
      }
      return this;
   }
}