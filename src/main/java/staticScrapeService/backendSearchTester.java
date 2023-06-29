package staticScrapeService;

import java.io.IOException;

public class backendSearchTester {
    // This class is primarily used to test searches
    public static void main(String[] args) throws IOException{
//        RaceFeaturesSearch featureSearch = new RaceFeaturesSearch();
//        RaceFeatures entity = (RaceFeatures) featureSearch.searchInfo("elf");

//        ClassFeatureSearch featureSearch = new ClassFeatureSearch("Barbarian");
//        ClassFeature entity = (ClassFeature) featureSearch.searchInfo("Rage");

//        ItemSearch itemSearch = new ItemSearch();
//        Item entity = (Item) itemSearch.searchInfo("Potion of Healing");

        SpellSearch spellSearch = new SpellSearch();
        Spell entity = (Spell) spellSearch.searchInfo("Eldritch Blast");

        System.out.println(entity.getDescription());
    }


}
