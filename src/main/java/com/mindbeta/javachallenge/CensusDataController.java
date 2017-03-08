package com.mindbeta.javachallenge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Gathers Aggregate Demographic results from US Census Data by State.
 *
 * @author Kirk Harr
 */
public class CensusDataController
{
    // Set static User-Agent for all connections.
    private final String USER_AGENT = "Mozilla/5.0";
    /**
     * Gather demographic data for a list of US states from the US Census data for those states and displaying an aggregate output of that data. Format options include a weighted average for income distribution below poverty of all the states, and CSV output for all states individually.
     * @param args comma-delimited list of US States followed by 'CSV' for CSV output and 'averages' for the weighted-average income below poverty output.
     */
    public static void main( String[] args ) {
    }

    /**
     * Lookup the US Census FIPS ID for the provided state name.
     *
     * @param stateName the name of the state to lookup for it's US Census FIPS ID.
     * @return US Census FIPS ID for the state.
     */
    public Integer StateIdLookup( String stateName ) {
        // URL of Census Data API with proper request options.
        String url = "https://www.broadbandmap.gov/broadbandmap/census/state/" + stateName + "?maxresults=1&all=false&format=json";
        StringBuffer response = new StringBuffer();
        try {
            URL apiRequest = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) apiRequest.openConnection();
            connection.setRequestMethod("GET");
            // Provide dummy User-Agent.
            connection.setRequestProperty("User-Agent", USER_AGENT);
            if (connection.getResponseCode() == 200){
                BufferedReader input = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                String line;
                // Gather JSON output line by line.
                while ((line = input.readLine()) != null ){
                    response.append(line);
                }
                input.close();
            }
            // In the event of a unsuccessful GET return error condition.
            else {
                System.out.println("Error retrieving URL from remote API. Please try again later.");
            }
        }
        // Any exceptions caught during the operations will print out a stack trace for debugging.
        catch (Exception e) {
            e.printStackTrace();
        }
        // Unboxing the JSON data structure.
        JSONObject fullOutput = new JSONObject(response.toString());
        JSONObject results = fullOutput.getJSONObject("Results");
        JSONArray stateDataArray = results.getJSONArray("state");
        JSONObject stateData = (JSONObject) stateDataArray.get(0);
        // Return just the FIPS ID.
        Integer returnInt = Integer.valueOf((String)stateData.get("fips"));
        return returnInt;
    }

    /**
     * Display Weighted-Average demographic data for income below poverty for the states of the provided US Census FIPS IDs.
     *
     * @param fipsIds US Census FIPS IDs for the states.
     */
    public void StateDataOutput ( int[] fipsIds ) {

    }

    /**
     * Display CSV formatted demographic data for population, number of households, distribution of income below poverty, and the median income for each state.
     *
     * @param fipsIds US Census FIPS IDs for the states.
     */
    public void StateCSVOutput ( int[] fipsIds ) {

    }
}
