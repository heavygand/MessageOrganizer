package helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Helpers {

	private static String readFileKacke(String file) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	/**
	 * @param file
	 * @return 
	 */
	public static String readFile(File file) {
		
		return readFile(file.getPath());
	}
	
	public static String readFile(String file) {
		
		String content = null;

		try {
			content = readFileKacke(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static List<String> getLines(String file){
		
		Stream<String> streamLines = null;
		
		try {
			streamLines = Files.lines(Paths.get(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> lines = new ArrayList<>();
		
		streamLines.forEach(line -> {
			
			lines.add(line);
		});
		
		return lines;
	}
	
	public static List<String> getLines(File file){
		
		return getLines(file.getPath());
	}
	
	public static void writeToFile(List<String> lines, File file) {
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String str: lines) {
		  try {
			writer.write(str);
			writer.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		try {
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
