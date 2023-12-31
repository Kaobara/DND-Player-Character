package characterCreatorService;

import org.apache.commons.text.WordUtils;
import staticScrapeService.RaceFeatures;
import staticScrapeService.RaceFeaturesSearch;

import java.util.ArrayList;

public class Race {
    private RaceFeaturesSearch raceFeaturesSearch = new RaceFeaturesSearch();
    private ArrayList<String> raceList = new ArrayList<>();
    private String raceName;
    private RaceFeatures raceFeatures;
    private boolean isSet = false;

    public Race() {
        raceList = raceFeaturesSearch.getListOfRaces();
    }
    public Race(String raceName) {
        isSet = true;
        raceList = raceFeaturesSearch.getListOfRaces();
        this.raceName = WordUtils.capitalizeFully(raceName);
        raceFeatures = (RaceFeatures) raceFeaturesSearch.searchInfo(raceName);
    }

    public void printRaceList() {
        for(String race : raceList) {
            System.out.println(race);
        }
    }

    public boolean checkRace(String raceName) {
        if(raceList.contains(raceName.toLowerCase())) {
            return true;
        }
        return false;
    }

    public void printRaceFeatures() {
        System.out.println(raceName.toUpperCase());
        System.out.println(raceFeatures.getDescription());
    }

    public String getRaceName() {
        return raceName;
    }

    public RaceFeatures getRaceFeatures() {
        if(!isSet) return null;
        return raceFeatures;
    }
}
