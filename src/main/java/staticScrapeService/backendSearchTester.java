package staticScrapeService;

import java.io.IOException;

public class backendSearchTester {
    // This class is primarily used to test searches
    public static void main(String[] args) throws IOException{

//        ClassFeatureSearch featureSearch = new ClassFeatureSearch("Barbarian");
//        ClassFeature entity = (ClassFeature) featureSearch.searchInfo("Rage");
        RaceFeaturesSearch featureSearch = new RaceFeaturesSearch();
        RaceFeatures entity = (RaceFeatures) featureSearch.searchInfo("elf");
//        for(String text : classFeature) {
//            System.out.println(text);
//        }
//
//        EntitySearch service = new EntitySearch();
//
//        SpellSearch spellSearch = new SpellSearch();
//        ItemSearch itemSearch = new ItemSearch();
//        Item entity = (Item) itemSearch.searchInfo("Potion of Healing");
//        Spell entity = (Spell) spellSearch.searchInfo("Wish");
//
//        entity.getDescSections();
//        if(entity.isHasTable()){
//            System.out.println(entity.getTableContent(0));
//        }
    }


}
