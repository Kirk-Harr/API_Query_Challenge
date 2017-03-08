package com.mindbeta.javachallenge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import java.util.Collections;
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
    /**
     * Gather demographic data for a list of US states from the US Census data for those states and displaying an aggregate output of that data. Format options include a weighted average for income distribution below poverty of all the states, and CSV output for all states individually.
     * @param args comma-delimited list of US States followed by 'CSV' for CSV output and 'averages' for the weighted-average income below poverty output.
     */
    public static void main( String[] args ) {
        // Gather input from command line parameters.
        CensusDataController ctl = new CensusDataController();
        // Split states by comma delimiter.
        String[] states = args[0].split(",");
        ArrayList<String> stateIds = new ArrayList<String>();
        // Lookup all state FIPS IDs.
        for (String s : states){
            // Replace any spaces with the URL-friendly space character.
            s = s.replace(" ", "%20");
            stateIds.add(ctl.StateIdLookup(s));
        }
        String[] stringArray = stateIds.toArray(new String[stateIds.size()]);
        // Select output format.
        if (args[1].equals("CSV")) {
            ctl.StateCSVOutput(stringArray);
        }
        else if (args[1].equals("averages")) {
            ctl.StateDataOutput(stringArray);
        }
        // Error if output format is invalid.
        else {
            System.out.println("Output format not recognized. The output options are \'CSV\' and \'averages\'.");
        }
    }

    /**
     * Lookup the US Census FIPS ID for the provided state name.
     *
     * @param stateName the name of the state to lookup for it's US Census FIPS ID.
     * @return US Census FIPS ID for the state.
     */
    private String StateIdLookup( String stateName ) {
        // URL of Census Data API with proper request options.
        String url = "https://www.broadbandmap.gov/broadbandmap/census/state/" + stateName + "?maxresults=1&all=false&format=json";
        // Get JSON output from request.
        StringBuffer response = retrieveJSON(url);
        // Unboxing the JSON data structure.
        JSONObject fullOutput = new JSONObject(response.toString());
        JSONObject results = fullOutput.getJSONObject("Results");
        JSONArray stateDataArray = results.getJSONArray("state");
        JSONObject stateData = (JSONObject) stateDataArray.get(0);
        // Return just the FIPS ID.
        return (String)stateData.get("fips");
    }

    private static StringBuffer retrieveJSON (String url) {
        // Set static User-Agent for all connections.
        String USER_AGENT = "Mozilla/5.0";
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
        return response;
    }

    /**
     * Display Weighted-Average demographic data for income below poverty for the states of the provided US Census FIPS IDs.
     *
     * @param fipsIds US Census FIPS IDs for the states.
     */
    private void StateDataOutput ( String[] fipsIds ) {
        double[] povertyIncomeValues = new double[fipsIds.length];
        for( int id = 0; id < fipsIds.length; id++){
            // URL of Census Data API with proper request options.
            String url = "https://www.broadbandmap.gov/broadbandmap/demographic/jun2014/state/ids/" + fipsIds[id] + "?format=json";
            // Get JSON output from request.
            StringBuffer response = retrieveJSON(url);
            // Unboxing the JSON data structure.
            JSONObject fullOutput = new JSONObject(response.toString());
            JSONArray results = fullOutput.getJSONArray("Results");
            JSONObject stateData = (JSONObject) results.get(0);
            povertyIncomeValues[id] = (Double) stateData.get("incomeBelowPoverty");
        }
        // Compute average of income below poverty values.
        double runningTotal = 0.0;
        for (double value: povertyIncomeValues){
            runningTotal += value;
        }
        double average = runningTotal / fipsIds.length;
        // Output Average
        System.out.println(average);
    }

    /**
     * Display CSV formatted demographic data for population, number of households, distribution of income below poverty, and the median income for each state.
     *
     * @param fipsIds US Census FIPS IDs for the states.
     */
    private void StateCSVOutput ( String[] fipsIds ) {
        ArrayList<String> stateCSVData = new ArrayList<String>();
        for( String id : fipsIds) {
            // URL of Census Data API with proper request options.
            String url = "https://www.broadbandmap.gov/broadbandmap/demographic/jun2014/state/ids/" + id + "?format=json";
            // Get JSON output from request.
            StringBuffer response = retrieveJSON(url);
            // Unboxing the JSON data structure.
            JSONObject fullOutput = new JSONObject(response.toString());
            JSONArray results = fullOutput.getJSONArray("Results");
            JSONObject stateData = (JSONObject) results.get(0);
            // Create each line of the CSV with the proper values included.
            String csvLine = "";
            csvLine = csvLine.concat(stateData.getString("geographyName") + ", ");
            csvLine = csvLine.concat(stateData.get("population") + ", ");
            csvLine = csvLine.concat(stateData.get("households") + ", ");
            csvLine = csvLine.concat(stateData.get("incomeBelowPoverty") + ", ");
            csvLine = csvLine.concat(stateData.get("medianIncome").toString());
            stateCSVData.add(csvLine);
        }
        // Sort the resulting ArrayList by state name alphabetically.
        Collections.sort(stateCSVData);
        // Output sorted ArrayList.
        for (String s : stateCSVData){
            System.out.println(s);
        }
    }
}
