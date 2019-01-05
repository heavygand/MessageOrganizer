import java.util.ArrayList;
import java.util.List;

import de.ks.messageOrg.app.DBController;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;

public class SachenProbieren {
	
	public static void main(String[] args) {
		
		List rawPerson = DBController.getPerson("Adam Kostorz");
		Object[] objectArray = (Object[]) rawPerson.get(0);
		
		Person person = new Person();
		person.setTitle((String) objectArray[0]);
		person.setThread_path((String) objectArray[1]);
		person.setLastContact((long) objectArray[2]);
		person.setFriendsSince((long) objectArray[3]);
		person.setFirstContact((long) objectArray[4]);

		List rawMessages = DBController.getMessages(person.getThread_path());
		if(rawMessages == null) System.out.println(person.getTitle() + " hat keine Messages");
		Object[] objectArray2 = (Object[]) rawMessages.get(0);
		
		ArrayList<Message> messages = new ArrayList<>();
		for(int i = 0 ; i < objectArray2.length ; i++) {
			
			Message message = new Message();
			message.setContent((String) objectArray2[1]);
			message.setSender_name((String) objectArray2[2]);
			message.setTimestamp_ms((long) objectArray2[3]);
		}
		
		person.setMessages(messages);
	}
}
