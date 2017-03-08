package com.mindbeta.javachallenge;

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

    }

    /**
     * Lookup the US Census FIPS ID for the provided state name.
     *
     * @param stateName the name of the state to lookup for it's US Census FIPS ID.
     * @return US Census FIPS ID for the state.
     */
    public int StateIdLookup( String stateName ) {
        return 0;
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
