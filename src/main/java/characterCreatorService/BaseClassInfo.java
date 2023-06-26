package characterCreatorService;

import staticScrapeService.ClassFeatureSearch;

import java.util.ArrayList;

public class BaseClassInfo {
    private final ArrayList<String> listOfClasses = new ArrayList<String>() {
        {
            add("artificer");
            add("barbarian");
            add("bard");
            add("cleric");
            add("druid");
            add("fighter");
            add("monk");
            add("paladin");
            add("rogue");
            add("sorcerer");
            add("wizard");
            add("warlock");
        }
    };
    public void printClassList() {
        for(String className : listOfClasses) {
            System.out.println(className);
        }
    }

    public boolean checkClass(String className) {
        if(listOfClasses.contains(className.toLowerCase())) {
            return true;
        }
        return false;
    }
}
