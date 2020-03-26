package com.hapz.assessment;

/* DO NOT CHANGE */
/* Please do not change the imported functions as you will be assessed based on your usage of the selected libraries */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.logging.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.net.*;
import com.google.gson.*;
import com.google.gson.stream.*;
/* Please do not change the imported functions as you will be assessed based on your usage of the selected libraries */
/* DO NOT CHANGE */

/**
 *
 * @author dev
 */
public class Main {

	
	private static final String API_KEY = "vaibsgharge@gmail.com";
	private static final String GET_URL = "http://assessment.hapz.com/api/?apiKey=%s&Title=%s";
	private static final String GET_SPECIFIC_PAGE_URL = "http://assessment.hapz.com/api/?apiKey=%s&Title=%s&page=%s";

	
	private static final Gson gson = new Gson();
	
	public static String[] getMovieTitles(String searchFor) {
		return collectMovieNamesInAscendingOrder(collectResponsesForGivenMovieTitle(searchFor));
	}

	private static List<String> collectResponsesForGivenMovieTitle(final String searchFor) {
		
		List<String> listOfResponse = new ArrayList<String>();
		
	  try {
			
			String sanitizedQueryParam = URLEncoder.encode(searchFor, "UTF-8");
			
			String firstPageResponse = retrieveFirstResponseForMovie(sanitizedQueryParam);
			ListOfMovies firstPageObject = gson.fromJson(firstPageResponse, ListOfMovies.class);
			
			if(firstPageObject != null && firstPageObject.getDataList() != null && firstPageObject.getDataList().size() > 0) {
				
				int totalNoOfPages = firstPageObject.getTotalPages();
				listOfResponse.add(firstPageResponse);
				
				List<String> remainingPages = IntStream
						.range(2, totalNoOfPages + 1)
						.mapToObj(currentPageNo -> retrieveMovieDetailsForPage(searchFor, currentPageNo))
						.collect(Collectors.toList());
				
				listOfResponse.addAll(remainingPages);
			}
				
		} catch(UnsupportedEncodingException ex) {
			System.out.println("Incorrect URI paramaters "+ ex.getMessage());
		}
	  
		return listOfResponse;
	}

	private static String[] collectMovieNamesInAscendingOrder(List<String> listOfResponse) {

		TreeSet<String> uniqueMovieTitles = new TreeSet<String>();
		
		try {

			for (String response : listOfResponse) {
				
				ListOfMovies responseObject = gson.fromJson(response, ListOfMovies.class);

				for (MovieDetails movieDetails : responseObject.getDataList()) {
					uniqueMovieTitles.add(getMovieTitle(movieDetails));
				}
			}
		}

		catch (Exception excep) {
			throw new RuntimeException("Exception occurred while processing movie responses", excep);
		}

		return uniqueMovieTitles.toArray(new String[uniqueMovieTitles.size()]);
	}

	private static String getMovieTitle(MovieDetails movieDetails) {
		return movieDetails.getTitle() != null ? movieDetails.getTitle() : "";
	}
	
	public static String retrieveFirstResponseForMovie(String searchFor) {

		return accessResourceAtLocation(String.format(GET_URL, API_KEY, searchFor)).toString();
	}

	public static String retrieveMovieDetailsForPage(String searchFor, int pageNumber) {

		return accessResourceAtLocation(String.format(GET_SPECIFIC_PAGE_URL, API_KEY, searchFor, String.valueOf(pageNumber))).toString();
	}

	private static StringBuilder accessResourceAtLocation(String resourceLocation) {
		
		URL url;
		HttpURLConnection requestConnection = null;
		String inputLine = "";
		StringBuilder contentReceived = new StringBuilder("");
		
		try {

			url = new URL(removeSpacesFromQueryParams(resourceLocation));
			requestConnection = (HttpURLConnection) url.openConnection();
			requestConnection.setRequestMethod("GET");
			requestConnection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(requestConnection.getInputStream()));

			while ((inputLine = in.readLine()) != null) {
				contentReceived.append(inputLine);
			}

			in.close();

			requestConnection.disconnect();

		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			
		} finally {
			requestConnection.disconnect();
		}
		return contentReceived;
	}

	private static String removeSpacesFromQueryParams(String resourceLocation) throws UnsupportedEncodingException {
		return resourceLocation.replaceAll("\\s", "%20");
	}

}
