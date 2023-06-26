package staticScrapeService;

import java.util.ArrayList;

public class RaceFeaturesFactory extends EntityFactory {

    public RaceFeatures createEmptyEntity() { return new RaceFeatures(); }

    public RaceFeatures createEntity(String raceName, ArrayList<String> fullContent, ArrayList<ContentTable> tables,
                                     String additionalStrInfo, int additionalIntInfo) {
        String description = getDescription(fullContent);
        RaceFeatures raceFeature = new RaceFeatures(raceName, "", null, -1, description);
        return raceFeature;
    }

}
