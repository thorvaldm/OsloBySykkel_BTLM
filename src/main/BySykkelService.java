package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BySykkelService {
    private static final String URL_AVAILABILITY = "https://oslobysykkel.no/api/v1/stations/availability";
    private static final String URL_STATIONS = "https://oslobysykkel.no/api/v1/stations";
    String response = "";
    RestClient restClient;
    public BySykkelService() {
        restClient = new RestClient();
    }
    private String get(URL url) {
            return restClient.get(url);
    }

    public Map<Integer, Station> getStationsAvailability() {
        String stationsAvailabilityJson = "";
        try {
            stationsAvailabilityJson = restClient.get(new URL(URL_AVAILABILITY));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed: error message: " + e.getMessage());
        }
        Map<Integer, Station> stationsMap = new HashMap<>();
        try {
            JSONObject stationsObj = new JSONObject(stationsAvailabilityJson);
            JSONArray stationsArr = stationsObj.getJSONArray("stations");
            for (int i = 0; i < stationsArr.length(); i++) {
                Station station = new Station(stationsArr.getJSONObject(i).getInt("id"),
                        new Availability(stationsArr.getJSONObject(i).getJSONObject("availability").getInt("bikes"),
                                stationsArr.getJSONObject(i).getJSONObject("availability").getInt("locks")));
                Integer id = stationsArr.getJSONObject(i).getInt("id");
                stationsMap.put(id, station);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Failed when parsing JSON string: error message: " + e.getMessage());
        }
        return  stationsMap;
    }

    public Map<Integer, String> getStations() {
        String stationsJson = "";
        try {
            stationsJson = restClient.get(new URL(URL_STATIONS));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed: error message: " + e.getMessage());
        }
        Map<Integer, String> stationsNameMap = new HashMap<>();
        try {
            JSONObject stationsObj = new JSONObject(stationsJson);
            JSONArray stationsArr = stationsObj.getJSONArray("stations");
            for (int i = 0; i < stationsArr.length(); i++) {
                Integer id = stationsArr.getJSONObject(i).getInt("id");
                String title = stationsArr.getJSONObject(i).getString("title");
                stationsNameMap.put(id, title);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Failed when parsing JSON string: error message: " + e.getMessage());
        }
        return  stationsNameMap;
    }

    public void listBysykkelInfo(Map<Integer, Station> stationMap, Map<Integer, String> stationsNameMap) {
        Set<Integer> keys = stationMap.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Integer next = (Integer) iterator.next();
            Station station = stationMap.get(next);
            String name = stationsNameMap.get(next);
            if (name != null) {
                stationMap.get(next).setTitle(name);
            } else {
                stationMap.get(next).setTitle("Ingen navn tilgjengelig");
            }
            System.out.println("Stasjons-Id: " + station.getId() + " | Navn på stasjon: " + station.getTitle() + " | Antall ledige sykler: " + station.getAvailability().getBikes() + " | Antall ledige låser: " + station.getAvailability().getLocks() + "\n");
        }
    }
}
