package fixer;

import java.io.File;
import java.util.List;

import helpers.Helpers;

public class CreatorCreatorCreator {

	public static void main(String[] args) {
		
		File folder = new File("C:/Users/erikd/MyControlWorkspace//MessageOrganizer/src/main/java/de/ks/messageOrg/model/util/CreatorCreator.java");
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
			
			if (file.isFile()) {
				
				System.out.println(file.getName());
				
				String fileContent = Helpers.readFile(file);
				
				List<String> lines = Helpers.getLines(file);
				
				for (String line : lines) {
					
					String regex = "(.*public )([a-zA-Z0-9_<>])*( set[a-zA-Z0-9_<>]*\\({1}.*)";
					if(line.matches(regex)) {
						
						lines.set(lines.indexOf(line), line.replaceAll(regex, "$1void$3"));
					}
				}			
				
				Helpers.writeToFile(lines, file);
			}
		}
	}
	
	public static String getText() {
		
		return "";
	}
}
