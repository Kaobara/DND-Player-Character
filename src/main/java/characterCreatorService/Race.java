package characterCreatorService;

import staticScrapeService.RaceFeaturesSearch;

import java.util.ArrayList;

public class Race {
    private RaceFeaturesSearch raceFeaturesSearch = new RaceFeaturesSearch();
    private ArrayList<String> raceList = new ArrayList<>();

    public Race() {
        raceList = raceFeaturesSearch.getListOfRaces();
    }

    public void printRaceList() {
        for(String race : raceList) {
            System.out.println(race);
        }
    }
}
