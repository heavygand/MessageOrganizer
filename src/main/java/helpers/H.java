package helpers;

import java.io.*;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.xml.parsers.*;

import org.apache.tika.parser.txt.CharsetDetector;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.junrar.UnrarCallback;

public class H {
	
	/*
	 * 
	 * FILE STUFF
	 * 
	 */

	// READING
	
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
	
	// WRITING
	
	public static void overWriteFile(List<String> lines, String path) {
		
		overWriteFile(lines, new File(path));
	}
	
	public static void overWriteFile(List<String> lines, File file) {
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for( int i = 0 ; i < lines.size() ; i++) {
		  try {
			
			String str = lines.get(i);
			writer.write(str);
			
			
			if (i != lines.size()-1) {
				
				writer.write("\n");
			}
		  }
		  catch (IOException e) {
			  
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
	
	public static void overWriteFile(String content, File file) {
		
		FileWriter writer = null;
		try {
			
			file.createNewFile();
			writer = new FileWriter(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer.write(content);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void overWriteFile(String content, String path) {

		overWriteFile(content, new File(path));
	}
	
	public static void appendToFile(String line, File file) {
		
		String pathString = file.getPath();
		appendToFile(line, pathString);
	}
	
	public static void appendToFile(String line, String pathString) {
		
		Path path = Paths.get(pathString);
		appendToFile(line, path);
	}
	
	public static void appendToFile(String line, Path path) {
		
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

		    writer.write("\n");
		    writer.write(line);
		}
		catch( NoSuchFileException e) {
			
			List<String> initList = new ArrayList<String>();
			initList.add(line);
			overWriteFile(initList, path.toString());
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void appendToFile(List<String> lines, String pathString) {

		Path path = Paths.get(pathString);
		for(String line: lines) {

			appendToFile(line, path);
		}
	}
	
	public static void appendToFile(List<String> lines, File file) {

		appendToFile(lines, file.getPath());
	}
	
	public static void writeJSONObjectToFile(JSONObject jsonObj, String path) {
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String jsonText = jsonObj.toString(2);
		
		out.print(jsonText);
		out.close();
	}
	
	/*
	 * 
	 * CLASS STUFF
	 * 
	 */
	public static Class getClass(String string) {

		Class cls = null;
		try {
			cls = Class.forName(string);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return cls;
	}

	public static Object newInstance(Class newClass) {

		try {
			return newClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*
	 * 
	 * STRING STUFF
	 * 
	 */
	public static String cutOffCharsAtEnd(String input, int i) {

		return input.substring(0, input.length()-i);
	}

	public static String getUpperFirst(String input) {
		
		return (input.charAt(0)+"").toUpperCase()+input.substring(1);
	}

	public static String substringBefore(String line, String beforeWhat) {

		return line.substring(0, line.indexOf(beforeWhat));
	}

	public static String substringAfter(String line, String afterWhat) {

		return line.substring(line.indexOf(afterWhat)+afterWhat.length());
	}
	public static String substringBeforeLast(String line, String beforeWhat) {

		return line.substring(0, line.lastIndexOf(beforeWhat));
	}
	public static String substringBetween(String line, String begin, String end) {

		return substringBefore(substringAfter(line, begin), end);
	}

	public static String cleanUp(String data) {
		
//		if(data.startsWith("Andre R"))System.out.println(data + " wird gecleaned");
		
		CharsetDetector detector = new CharsetDetector();
		detector.setText(data.getBytes());
		String charSetName = detector.detect().getName();
		int confidence = detector.detect().getConfidence();
		
		if (charSetName.equals("ISO-8859-1") && !(
				data.contains("Ã") ||
				data.contains("±") || 
				data.contains("¼") || 
				data.contains("©") || 
				data.contains("¿") || 
				data.contains("¨") || 
				data.contains("¸") || 
				data.contains("¤") || 
				data.contains("¡") || 
				data.contains("¶")
				)) {
			
			return data;
		}		
		
		String result = null;
		try {
			
			result = new String(data.getBytes("ISO-8859-1"), "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

//		if(data.startsWith("Andre R"))System.out.println(result + " kam dabei raus");
		return result;
	}
	/*
	 * 
	 * JSON/XML STUFF
	 * 
	 */
	public static boolean isJsonArray(JSONObject jsonObj, String key) {

		try {
			jsonObj.getJSONArray(key);
		} catch (JSONException e) {
			
			return false;
		}
		return true;
	}

	public static Document getXmlDocument(String path) {

		// Create a DocumentBuilder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		
		// Create a Document from a file or stream
		String fXmlFile = H.readFile(path);
		ByteArrayInputStream input = null;
		try {
			input = new ByteArrayInputStream(fXmlFile.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Document doc = null;
		try {
			doc = builder.parse(input);
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return doc;
	}

	/*
	 * 
	 * DATE STUFF
	 * 
	 */
	public static LocalDateTime getLocalDateTime(Timestamp timeStamp) {

		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp.getTime()), TimeZone.getDefault().toZoneId());
	}

	public static LocalDateTime getLocalDateTime(long timeStamp) {

		return getLocalDateTime(new Timestamp(timeStamp));
	}

	public static LocalDateTime getLocalDate(long timeStamp) {

		return getLocalDateTime(new Timestamp(timeStamp));
	}

	public static LocalDateTime getLocalHour(long timeStamp) {

		return getLocalDateTime(new Timestamp(timeStamp));
	}
	
	public static String getGermanDateTimeString(LocalDateTime triggerTime) {

		return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMAN).format(triggerTime);
	}
	
	public static String getGermanDateString(LocalDateTime triggerTime) {

		return DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN).format(triggerTime);
	}
	
	public static String getGermanHourString(LocalDateTime triggerTime) {

		return DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN).format(triggerTime);
	}
	
	public static String getGermanDateTimeString(Timestamp timeStamp) {

		return getGermanDateTimeString(getLocalDateTime(timeStamp));
	}
	
	public static String getGermanDateTimeString(long timeStamp) {

		return getGermanDateTimeString(getLocalDateTime(timeStamp));
	}
	
	public static String getGermanDateString(long timeStamp) {

		return getGermanDateString(getLocalDate(timeStamp));
	}
	
	public static String getGermanHourString(long timeStamp) {

		return getGermanHourString(getLocalHour(timeStamp));
	}
	
	public static int getSeconds(long milliseconds) {
		
		return (int)TimeUnit.MILLISECONDS.toSeconds(milliseconds);
	}
	
	public static int getMinutes(long milliseconds) {
		
		return (int)TimeUnit.MILLISECONDS.toMinutes(milliseconds);
	}
	
	public static int getHours(long milliseconds) {
		
		return (int)TimeUnit.MILLISECONDS.toHours(milliseconds);
	}
	
	public static int getDays(long milliseconds) {
		
		return (int)TimeUnit.MILLISECONDS.toDays(milliseconds);
	}

	public static int getDayDifference(long time1, long time2) {

		long difference = Math.abs(time1 - time2);
		
		return getDays(difference);
	}

	public static boolean isEarlierOrToday(long earlierOrToday) {

        long currentTime = System.currentTimeMillis();
        
		long substraction = getDays(currentTime) - getDays(earlierOrToday);
		
		return substraction >= 0 ? true : false;
	}
	
	public static Timestamp getTimeStamp(LocalDate dpValue) {

		return Timestamp.valueOf(dpValue.atStartOfDay());
	}
	
	public static long getTimeStampAsLong(LocalDate dpValue) {

		return getTimeStamp(dpValue).getTime();
	}
}
