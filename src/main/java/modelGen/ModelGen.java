package modelGen;

import org.junit.jupiter.api.Test;

import de.uniks.networkparser.ext.ClassModel;
import de.uniks.networkparser.graph.*;

public class ModelGen {

	@Test
	public void generate() {
		
		ClassModel model = new ClassModel("de.uniks.studyright");
		Clazz uni = model.createClazz("University").withAttribute("name", DataType.STRING);
		Clazz student = model.createClazz("Student").withAttribute("matNo", DataType.INT);
	
		uni.withBidirectional(student, "students", Cardinality.MANY, "uni", Cardinality.ONE);
		Clazz room = model.createClazz("Room").withAttribute("roomNo", DataType.STRING);
		uni.withBidirectional(room, "rooms", Cardinality.MANY, "uni", Cardinality.ONE);
		model.generate();
	}
}