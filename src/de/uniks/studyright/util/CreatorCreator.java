package de.uniks.studyright.util;
import de.uniks.networkparser.IdMap;

class CreatorCreator {

   public static final IdMap createIdMap(String session) {
        IdMap map = new IdMap().withSession(session);
        map.withCreator(new RoomCreator());
        map.withCreator(new StudentCreator());
        map.withCreator(new UniversityCreator());

        return map;
   }
}