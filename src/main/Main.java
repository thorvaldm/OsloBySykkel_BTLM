package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String stationsAvailability = getAvailability();
        System.out.println(stationsAvailability);
        String stations = getStations();
        System.out.println(stations);

        Map<Integer, Station> stationMap = parseStationsAvailability(stationsAvailability);
        Map<Integer, String> stationsNameMap = parseStations(stations);

        Set<Integer> keys = stationMap.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Integer next = (Integer)iterator.next();
            Station station =stationMap.get(next);
            stationMap.get(next).setTitle(stationsNameMap.get(next));
            System.out.println("Id: " + station.getId() + " title: " + station.getTitle() + " bikes: " + station.getAvailability().getBikes() + " locks: " + station.getAvailability().getLocks() + "\n");
        }
    }

    public static String getAvailability() {
        String stationsAvailability = "";
        try {
            URL availability_url = new URL("https://oslobysykkel.no/api/v1/stations/availability");
            HttpURLConnection conn = (HttpURLConnection) availability_url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Client-Identifier", "8bd26a8126b52b342989122b2b27dca2");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                stationsAvailability = stationsAvailability + output;
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stationsAvailability;
    }

    public static String getStations() {
        String stations = "";
        try {
            URL stations_url = new URL("https://oslobysykkel.no/api/v1/stations");
            HttpURLConnection conn = (HttpURLConnection) stations_url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Client-Identifier", "8bd26a8126b52b342989122b2b27dca2");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                stations = stations + output;
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stations;
    }

    public static Map<Integer, Station> parseStationsAvailability(String stationsAvailabilityJson) {
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
            e.printStackTrace();
        }
        return  stationsMap;
    }

    public static Map<Integer, String> parseStations(String stationsJson) {
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
            e.printStackTrace();
        }
        return  stationsNameMap;
    }

}
