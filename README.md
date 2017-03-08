# State Census Data API Query Challenge

#### Description:
The CensusDataController class is an API query tool to gather US Census data for a list of states and format the data for output. This output will either provide an aggregate average of the percentage of the population of those states below the poverty line, or provide a CSV table of the data gathered for each state. 

#### Building:
This application is built using Maven, and once you have cloned the repository, you can build the application with the command:  
`mvn clean package`  

Once the application is built there will be an api-query-challenge.jar file in the target directory containing the application and any dependencies.  

### Usage:
`java -cp <builtJarPackage> com.mindbeta.javachallenge.CensusDataController <commaDelimitedStateList> <outputFormat>`  

The list of states is all the states to gather the demographic data for, e.g. `alaska,ohio,texas`

The two options for outputFormat are: CSV, averages  
  
* CSV output will provide all state demographic data in CSV format sorted alphabetically by state name.  
Example: `<state name>, <population>, <households>, <income below poverty>, <median income>`
* averages output will provide the weighted average of each state's population below the poverty line.  

#### Assumptions: 
Java version 8 was used for this project.

For the weighted average output, each state had their average number of people in poverty calculated by multiplying the percentage of the population below poverty by total state population. This number of people in poverty in each state is then multiplied by the proportion of the total state population divided by the total population of all states being analyzed. Once completed for each state these values are summed and then returned as an integer value to give the population-weighted average number of people in poverty in all the states provided in the list. 

These values are computed from the US National Broadband Map API using the June 2014 dataset. 

The US National Broadband Map API allowed for multiple inline FIPS IDs to be included for querying in each request. Given that this option was limited to only 10 IDs maximum, it was not used as this would restrict arbitrarily the number of states that could be included in the list. Each state is looked up individually to allow an unlimited number of states to be added to the list.  
 
However this inline lookup could possibly be more efficient than serially looking up each FIPS ID one by one. A further implementation could potentially calculate the number of states being queried initially to allow for forming subsets of no more than 10 IDs, but this would only be useful if the inline lookups did indeed save resources.

The specifications for this challenge stated the two output format options which were shown with specific casing on 'CSV' and 'averages' which was kept specific in the matching of which option was selected. Using different casing on the two options would cause an error for no proper output format selected. This could be improved if the equals method was exchanged for the case-insensitive version of the method.

The ordering of the two input parameters was also kept in the same order as the specifications with the state list followed by the output format selection. This could be improved if there was an option flag added to the two parameters (-s for state list, -f for output format) they could be entered in any order. 
