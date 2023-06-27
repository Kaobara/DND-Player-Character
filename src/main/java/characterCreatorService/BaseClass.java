package characterCreatorService;

import org.apache.commons.text.WordUtils;
import staticScrapeService.ClassFeature;
import staticScrapeService.ClassFeatureSearch;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseClass {
    private final BaseClassInfo baseClassInfo = new BaseClassInfo();

    private String className;
    private ClassFeatureSearch classFeatureSearch;
    private int classLevel;
    private boolean isSpellCaster = false;
    private ArrayList<ClassFeature> features = new ArrayList<>();

    public BaseClass(String className, int classLevel) {
        this.className = className;
        this.classLevel = classLevel;
        this.classFeatureSearch = new ClassFeatureSearch(className);

        for(String spellCastFeat : baseClassInfo.getSpellcastFeats()) {
            if(classFeatureSearch.getAllFeatures().contains(spellCastFeat)) {
                isSpellCaster = true;
            }
        }

        addLevelFeaturesToClass(0);
    }

    private void addLevelFeaturesToClass(int currentLevel) {
        HashMap<Integer, String[]> levelFeatureMap =  classFeatureSearch.getLevelFeatureMap();
        for(int i = currentLevel+1; i<=classLevel; i++) {
            String[] featuresInLevel = levelFeatureMap.get(i);
            for(String feature : featuresInLevel) {
                features.add((ClassFeature) classFeatureSearch.searchInfo(feature));
            }
        }
    }

    public void printAllCurrentClassFeatures() {
        System.out.println("Level " + classLevel + " " + WordUtils.capitalizeFully(className) + "\n");
        for(ClassFeature classFeature : features) {
            System.out.println(classFeature.getName().toUpperCase());
            System.out.println(classFeature.getDescription() + "\n");
        }
    }

    public void levelUpClass(int classLevelIncrease) {
        int tempCurrentLevel = this.classLevel;
        this.classLevel += classLevelIncrease;
        addLevelFeaturesToClass(tempCurrentLevel);
    }

    public int getClassLevel() {return classLevel; }
    public String getClassName() {return className; }
    private ClassFeature getClassFeatureDetails(String classFeature) {return (ClassFeature) classFeatureSearch.searchInfo(classFeature); }


}
