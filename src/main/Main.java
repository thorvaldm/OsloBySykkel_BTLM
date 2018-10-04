package main;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BySykkelService bySykkelService = new BySykkelService();

        Map<Integer, Station> stationMap = bySykkelService.getStationsAvailability();
        Map<Integer, String> stationsNameMap = bySykkelService.getStations();
        bySykkelService.listBysykkelInfo(stationMap, stationsNameMap);
    }
}
