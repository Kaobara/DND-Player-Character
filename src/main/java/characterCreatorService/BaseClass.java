package characterCreatorService;

import staticScrapeService.ClassFeatureSearch;

import java.util.ArrayList;

public class BaseClass {
    private String className;
    private ClassFeatureSearch classFeatureSearch;
    private int classLevel;

    public BaseClass(String className, int classLevel) {
        this.className = className;
        this.classLevel = classLevel;
        this.classFeatureSearch = new ClassFeatureSearch(className);
    }

    public void initializeClass(String className) {
        this.className = className;
        this.classFeatureSearch = new ClassFeatureSearch(className);
    }


}
