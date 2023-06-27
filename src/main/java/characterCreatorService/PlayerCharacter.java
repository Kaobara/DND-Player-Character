package characterCreatorService;

import staticScrapeService.Item;
import staticScrapeService.Spell;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlayerCharacter {
    private ArrayList<BaseClass> classes = new ArrayList<>();
    private ArrayList<String> classNames = new ArrayList<>();
    private Race race;
    private ArrayList<Spell> spells;
    private ArrayList<Item> items;
    private int totalLevels = 0;
    private String name;

    public PlayerCharacter(String name, Race race) {
        this.name = name;
        this.race = race;
    }

    public void giveCharacterBaseClass(BaseClass baseClass) {
        if(classes.contains(baseClass)) {
            return;
        }
        classes.add(baseClass);
        classNames.add(baseClass.getClassName());
        totalLevels += baseClass.getClassLevel();
    }

    public void levelUpCharacter(String className, int classLevelIncrease) {
        int baseClassIndex = getClassIndexInList(className);
        if(baseClassIndex == -1) return;

        classes.get(baseClassIndex).levelUpClass(classLevelIncrease);

        totalLevels+=classLevelIncrease;
    }

    public Race getRace() {
        return race;
    }

    public ArrayList<String> getClassNames() {
        return classNames;
    }

    public int getLevelInClass(String className) {
        int baseClassIndex = getClassIndexInList(className);
        if (baseClassIndex == -1) return -1;
        return classes.get(baseClassIndex).getClassLevel();
    }

    private int getClassIndexInList(String className) {
        if(!classNames.contains(className)) {
            return -1;
        }

        int i = 0;
        for(BaseClass baseClass : classes) {
            if(baseClass.getClassName().equalsIgnoreCase(className)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public String getName() {
        return name;
    }

    public void printCharacterDescription() {
        System.out.println(name.toUpperCase());
        race.printRaceFeatures();
        System.out.println("");
        for(BaseClass baseClass : classes) {
            baseClass.printAllCurrentClassFeatures();
        }

    }
}
