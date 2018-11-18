package helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;

public class H {

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
	private static byte[] readAllBytes(String file) {
		
		byte[] readAllBytes = null;
		try {
			readAllBytes = Files.readAllBytes(Paths.get(file)) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return readAllBytes;
	}
	private static byte[] readAllBytes(File file) {

		return readAllBytes(file.getPath());
	}
	
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

	public static String cleanUp(String data) {

		Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
		Matcher m = p.matcher(data);
		StringBuffer buf = new StringBuffer(data.length());
		while (m.find()) {
			String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
			m.appendReplacement(buf, Matcher.quoteReplacement(ch));
		}
		m.appendTail(buf);
		return buf.toString();
	}
	public static Class getClass(String string) {

		Class cls = null;
		try {
			cls = Class.forName(string);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cls;
	}

	public static Object newInstance(Class newClass) {

		try {
			return newClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getClassName(String key) {

		return "de.ks.messageOrg.model."+(key.charAt(0)+"").toUpperCase()+key.substring(1, key.length()-1);
	}

	public static String getCreatorName(String key) {

		return "de.ks.messageOrg.model.util."+cutOffCharsAtEnd(getUpperFirst(key), 1)+"Creator";
	}
	
	public static String cutOffCharsAtEnd(String input, int i) {

		return input.substring(0, input.length()-i);
	}

	public static String getUpperFirst(String input) {
		
		return (input.charAt(0)+"").toUpperCase()+input.substring(1);
	}

	public static boolean isJsonArray(JSONObject jsonObj, String key) {

		try {
			jsonObj.getJSONArray(key);
		} catch (JSONException e) {
			
			return false;
		}
		return true;
	}
}
