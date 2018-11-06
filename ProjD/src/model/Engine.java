package model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.net.www.protocol.http.HttpURLConnection;

public class Engine {
	
	private static Engine instance = null;
	private static final int DRONE_SPEED = 150;
	private static final int MINUTES_PER_HOUR = 60;
	private static final int DEGREES_TO_RADIANS_VALUE = 180;
	private static final double RIDE_COST_RATE = 0.50;
	private static final double ROUNDING_FACTOR = 100.0;
	
	private Engine() {}
	
	public synchronized static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
		}
		return instance;
	}

	public int doPrime(String lowVal, String highVal) throws Exception {
		int lowValInt = Integer.parseInt(lowVal);
		int highValInt = Integer.parseInt(highVal);
		
		
		BigInteger lowBI = new BigInteger(lowValInt+"");
		
		if (lowBI.nextProbablePrime().intValue() >= highValInt) {
			throw new Exception("No more primes in range.");
		}
		
		BigInteger result;
		result = lowBI.nextProbablePrime();
		return result.intValue();
	}

	public int doGps(String fromLat, String fromLong, String toLat, String toLong) {
		int lat1, long1, lat2, long2;
		lat1 = Integer.parseInt(fromLat);
		lat2 = Integer.parseInt(toLat);
		long1 = Integer.parseInt(fromLong);
		long2 = Integer.parseInt(toLong);
		System.out.println("1");
//		try { 
//			lat1 = Integer.parseInt(fromLat);
//			lat2 = Integer.parseInt(toLat);
//			long1 = Integer.parseInt(fromLong);
//			long2 = Integer.parseInt(toLong);
//		} catch (NumberFormatException e) {
//			throw new NumberFormatException("Please enter a number.");
//		}
//		
//		if (lat1 > 90 || lat1 < -90 || lat2 > 90 || lat2 < -90) {
//			throw new IllegalArgumentException("Latitudes must be between -90 and 90 (inclusive).");
//		}
//		
//		if (long1 > 180 || long1 < -180 || long2 > 180 || long2 < -180) {
//			throw new IllegalArgumentException("Latitudes must be between -180 and 180 (inclusive).");
//		}
		
		
		double doubledlat1 = lat1 * Math.PI/DEGREES_TO_RADIANS_VALUE;
		double doubledlat2 = lat2 * Math.PI/DEGREES_TO_RADIANS_VALUE;
		double doubledlong1 = long1 * Math.PI/DEGREES_TO_RADIANS_VALUE;
		double doubledlong2 = long2 * Math.PI/DEGREES_TO_RADIANS_VALUE;

		double Y = Math.cos(doubledlat1)*Math.cos(doubledlat2);
		double X = Math.pow(Math.sin((doubledlat2-doubledlat1)/2),2) + Y*Math.pow(Math.sin((doubledlong2-doubledlong1)/2),2);
		double distance = 12742 * Math.atan2(Math.sqrt(X), Math.sqrt(1-X));
		return (int) distance;
	}

	public int doDrone(String startAddress, String endAddress) throws Exception {
		try {
			StringBuilder result = new StringBuilder();
			StringBuilder result2 = new StringBuilder();
			String key = "AIzaSyCJgKGoMC90eqjCq8uueSPiRiHYs7izzm0";
			
			String urlString = "https://maps.googleapis.com/maps/api/geocode/xml?address="+startAddress
					+"&key="+key;
			urlString = urlString.replace(" ", "+");
			URL url = new URL(urlString);
			
			String urlString2 = "https://maps.googleapis.com/maps/api/geocode/xml?address="+endAddress
					+"&key="+key;
			urlString2 = urlString2.replace(" ", "+");
			URL url2 = new URL(urlString2);
			
			
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection(); // Opens a connection with the .cgi URL.
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/xml");
			// We read the XML output of the link requested here:
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader rd = new BufferedReader(isr);
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			
			HttpsURLConnection con2 = (HttpsURLConnection) url2.openConnection(); // Opens a connection with the .cgi URL.
			con2.setRequestMethod("GET");
			con2.setRequestProperty("Accept", "application/xml");
			// We read the XML output of the link requested here:  
			InputStreamReader isr2 = new InputStreamReader(con2.getInputStream());
			BufferedReader rd2 = new BufferedReader(isr2);
			String line2;
			while ((line2 = rd2.readLine()) != null) {
				result2.append(line2);
			}
			rd2.close();
			org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(result.toString())));
			org.w3c.dom.Document doc2 = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(result2.toString())));
			if (!doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent().equals("OK")) {
				throw new IllegalArgumentException("From location not found...");
			} else if (!doc2.getChildNodes().item(0).getChildNodes().item(1).getTextContent().equals("OK")) {
				throw new IllegalArgumentException("To location not found...");
			} else {
				String lat1 = doc.getElementsByTagName("location").item(0).getChildNodes().item(1).getTextContent();
				String long1 = doc.getElementsByTagName("location").item(0).getChildNodes().item(3).getTextContent();
				String lat2 = doc2.getElementsByTagName("location").item(0).getChildNodes().item(1).getTextContent();
				String long2 = doc2.getElementsByTagName("location").item(0).getChildNodes().item(3).getTextContent();
				double distance = this.doDistanceCalculation(lat1, long1, lat2, long2);
				return (int) ((distance/DRONE_SPEED)*MINUTES_PER_HOUR);
			}
		} catch (Exception e) {
			throw new Exception();
		}
	}

	private double doDistanceCalculation(String fromLat, String fromLong, String toLat, String toLong) {
		double lat1, long1, lat2, long2;
		lat1 = Double.parseDouble(fromLat);
		lat2 =  Double.parseDouble(toLat);
		long1 = Double.parseDouble(fromLong);
		long2 =  Double.parseDouble(toLong);
		
		double doubledlat1 = lat1 * Math.PI/DEGREES_TO_RADIANS_VALUE;
		double doubledlat2 = lat2 * Math.PI/DEGREES_TO_RADIANS_VALUE;
		double doubledlong1 = long1 * Math.PI/DEGREES_TO_RADIANS_VALUE;
		double doubledlong2 = long2 * Math.PI/DEGREES_TO_RADIANS_VALUE;

		double Y = Math.cos(doubledlat1)*Math.cos(doubledlat2);
		double X = Math.pow(Math.sin((doubledlat2-doubledlat1)/2),2) + Y*Math.pow(Math.sin((doubledlong2-doubledlong1)/2),2);
		double distance = 12742 * Math.atan2(Math.sqrt(X), Math.sqrt(1-X));
		return distance;
	}

	public double doCost(String startAddress, String endAddress) throws Exception {
		StringBuilder result = new StringBuilder();
		String key = "AIzaSyCJgKGoMC90eqjCq8uueSPiRiHYs7izzm0";
		
		String urlString = "https://maps.googleapis.com/maps/api/distancematrix/xml?"
				+ "origins="+startAddress+
				"&destinations="+endAddress+
				"&departure_time=now&key="+key;
		urlString = urlString.replace(" ", "+");
		URL url = new URL(urlString);
		
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection(); // Opens a connection with the .cgi URL.
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/xml");
		// We read the XML output of the link requested here:
		InputStreamReader isr = new InputStreamReader(con.getInputStream());
		BufferedReader rd = new BufferedReader(isr);
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(result.toString())));

		String seconds = doc.getElementsByTagName("duration_in_traffic").item(0).getChildNodes().item(1).getTextContent();
		double minutes = (Double.parseDouble(seconds))/MINUTES_PER_HOUR;
		return (minutes*RIDE_COST_RATE);
	}
	
	public List<StudentBean> doSis(String namePrefix, String minGpa, String sortOption) throws Exception {
		StudentDAO sd = new StudentDAO();
		double gpa;
		
		try {
			if (minGpa.equals("")) {
				minGpa = "0";
			}
			gpa = Double.parseDouble(minGpa);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Please enter a GPA");
		}
		
		if (sortOption.equals("NONE")) {
			sortOption = "SURNAME, GIVENNAME";
		}
		
		List<StudentBean> result = sd.retrieve(namePrefix, gpa, sortOption);	
		
		return result;
	}

}
