package de.ks.messageOrg.model.util;

import de.uniks.networkparser.IdMap;

public class CreatorCreator {

   public static final IdMap createIdMap(String session) {
        IdMap map = new IdMap().withSession(session);
        map.withCreator(new PersonCreator());
        map.withCreator(new ParticipantCreator());
        map.withCreator(new MessageCreator());

        return map;
   }
}