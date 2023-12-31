package staticScrapeService;

import java.util.ArrayList;

public class ClassFeatureFactory extends EntityFactory {

    public ClassFeature createEmptyEntity() { return new ClassFeature(); }

    public ClassFeature createEntity(String classFeatureName, ArrayList<String> fullContent, ArrayList<ContentTable> tables,
                                     String baseClass, int classLevel) {
        String description = getDescription(fullContent);
        ClassFeature classFeature = new ClassFeature(classFeatureName, "", baseClass, classLevel, description);
        return classFeature;
    }

}
