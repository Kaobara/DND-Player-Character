package characterCreatorService;

import org.apache.commons.text.WordUtils;
import staticScrapeService.Item;
import staticScrapeService.ItemSearch;
import staticScrapeService.Spell;
import staticScrapeService.SpellSearch;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerCharacter {
    private String uniqueID;
    private String name;
    private int totalLevels = 0;
    private Race race;
    private ArrayList<BaseClass> classes = new ArrayList<>();
    private ArrayList<String> classNames = new ArrayList<>();
    private HashMap<String, Integer> classLevels= new HashMap<>();
    private boolean isSpellCaster = false;
    private SpellSearch spellSearch = new SpellSearch();
    private ArrayList<Spell> spells = new ArrayList<>();
    private ArrayList<String> spellNames = new ArrayList<>();
    private ItemSearch itemSearch = new ItemSearch();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<String> itemNames = new ArrayList<>();


    public String getUniqueID () {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public String getRaceName() {
        return race.getRaceName();
    }

    public ArrayList<String> getClassNames() {
        return classNames;
    }

    public HashMap<String, Integer> getClassLevels() {
        return classLevels;
    }

    private Race getRace() {
        return race;
    }

    public int getLevelInClass(String className) {
        int baseClassIndex = getClassIndexInList(className);
        if (baseClassIndex == -1) return -1;
        return classes.get(baseClassIndex).getClassLevel();
    }

    public PlayerCharacter(String name, Race race) {
        this.name = WordUtils.capitalizeFully(name);
        this.race = race;
        this.uniqueID = UUID.randomUUID().toString();
    }

    public PlayerCharacter(PCJSONSkeleton pcjsonSkeleton) {
        this.uniqueID = pcjsonSkeleton.getUniqueID();
        this.name = pcjsonSkeleton.getName();
        this.totalLevels = pcjsonSkeleton.getTotalLevels();
        this.race = new Race(pcjsonSkeleton.getRaceName());
        this.classLevels = pcjsonSkeleton.getClassLevels();
        for(String className : classLevels.keySet()) {
            this.classNames.add(className);
            BaseClass baseClass = new BaseClass(className, classLevels.get(className));
            if(baseClass.getIsSpellCaster()) isSpellCaster = true;
            classes.add(baseClass);
        }
    }

    public PCJSONSkeleton createPCJSONSkeleton() {
        return new PCJSONSkeleton(uniqueID, name, totalLevels, race.getRaceName(), classLevels, spellNames, itemNames);
    }

    public void giveCharacterBaseClass(BaseClass baseClass) {
        if(classes.contains(baseClass)) {
            return;
        }
        classes.add(baseClass);
        if(baseClass.getIsSpellCaster()) isSpellCaster = true;
        classNames.add(baseClass.getClassName());
        setClassLevels(baseClass);
        totalLevels += baseClass.getClassLevel();
    }

    public void levelUpCharacter(String className, int classLevelIncrease) {
        int baseClassIndex = getClassIndexInList(className);
        if(baseClassIndex == -1) return;

        classes.get(baseClassIndex).levelUpClass(classLevelIncrease);
        setClassLevels(classes.get(baseClassIndex));

        totalLevels+=classLevelIncrease;
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

    private void setClassLevels(BaseClass baseClass) {
        classLevels.put(baseClass.getClassName(), baseClass.getClassLevel());
    }

    public void addSpell(String spellName) {

        if(isSpellCaster == false) {
            System.out.println(name + " is not a spellCaster.");
            return;
        }

        Spell spell = (Spell) spellSearch.searchInfo(spellName);
        boolean allowedSpell = false;
        for(String className : classNames) {
            if(spell.getAvailableClasses().contains(className)) {
                allowedSpell = true;
                break;
            }
        }
        if(allowedSpell == false) {
            System.out.println(spellName + " is not a spell that " + name + " can cast.");
            return;
        }

        System.out.println(spellName + " is added to " + name + "'s spell list.");
        spellNames.add(spellName);
        spells.add(spell);

    }

    public void printCharacterDescriptionSummary() {
        System.out.print(WordUtils.capitalizeFully(name) + " is a ");
        for(int i=0; i<classes.size(); i++) {
            BaseClass baseClass = classes.get(i);
            System.out.print("level " + baseClass.getClassLevel() + " " + WordUtils.capitalizeFully(baseClass.getClassName()));
            if(i+3 <= classes.size()) {
                System.out.print(", ");
            } else if(i+2 == classes.size()) {
                System.out.print(" and ");
            }
        }
        System.out.println(" " + WordUtils.capitalizeFully(race.getRaceName()) + " for a total of " + totalLevels + " levels");

    }

    public void printRaceFeatures() {
        race.printRaceFeatures();
    }

    public void printClassFeatures() {
        for(BaseClass baseClass : classes) {
            baseClass.printAllCurrentClassFeatures();
        }
    }

    public void printCharacterDescriptionFull() {
        System.out.println(WordUtils.capitalizeFully(name));
        race.printRaceFeatures();
        System.out.println("");
        for(BaseClass baseClass : classes) {
            baseClass.printAllCurrentClassFeatures();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
