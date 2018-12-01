package helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.tika.parser.txt.CharsetDetector;
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
	
	public static void writeToFile(JSONObject jsonObj, String path) {
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String jsonText = jsonObj.toString(2);
		
		out.print(jsonText);
		out.close();
	}

	public static String cleanUp(String data) {
		
		CharsetDetector detector = new CharsetDetector();
		detector.setText(data.getBytes());
		String charSetName = detector.detect().getName();
		
		if (charSetName.equals("ISO-8859-1")) {
			
//			if (data.contains("Alexander H")) {
//				System.out.println(data + " muss nicht umgewandelt werden.");
//			}
			return data;
		}		
		
		String result = null;
		try {
			result = new String(data.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		if (data.contains("Alexander H") && !data.equals(result)) {
//			
//			System.out.println(data);
//			System.out.println("wurde geändert zu");
//			System.out.println(result);
//			System.out.println("Charset war " + charSetName);
//		}
		return result;
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

	/*
	 * 
	 * DATUMSZEUG
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
