package staticScrapeService;

public class ClassFeature extends  Entity{
    private String baseClass;
    private int classLevel;
    public ClassFeature() {super(); }

    // The only field that is unique to items is an item's type and rarity.
    public ClassFeature(String name, String source, String baseClass, int classLevel, String description) {
        super(name, source, description);
        this.baseClass = baseClass;
        this.classLevel = classLevel;
    }

    public String getBaseClass() {
        return baseClass;
    }
    public int getClassLevel() { return classLevel; }
}
