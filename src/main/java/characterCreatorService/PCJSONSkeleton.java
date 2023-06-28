package characterCreatorService;

import org.apache.commons.text.WordUtils;
import staticScrapeService.Item;
import staticScrapeService.Spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PCJSONSkeleton {
    private String uniqueID;
    private String name;
    private int totalLevels = 0;
    private String raceName;
    private HashMap<String, Integer> classLevels= new HashMap<>();
    private ArrayList<String> spellNames;
    private ArrayList<String> itemNames;

    public PCJSONSkeleton() {}

    public PCJSONSkeleton(String uniqueID, String name, int totalLevels, String raceName, HashMap<String, Integer> classLevels) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.totalLevels = totalLevels;
        this.raceName = raceName;
        this.classLevels = classLevels;
    }

    public String getUniqueID () {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRace(String raceName) {
        this.raceName = raceName;
    }

    public HashMap<String, Integer> getClassLevels() {
        return classLevels;
    }

    public void setClassLevels(HashMap<String, Integer> classLevels) {
        this.classLevels = classLevels;
    }

    public void printPCDescription() {
        System.out.print(this.name + " " + this.raceName + " ");
        for(String className : classLevels.keySet()) {
            System.out.print("level " + classLevels.get(className) + " " + className + " ");
        }
        System.out.println("");
    }

}
