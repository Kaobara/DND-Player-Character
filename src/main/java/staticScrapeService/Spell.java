package staticScrapeService;

import java.util.ArrayList;

public class Spell extends Entity {
    private String levelSchool, metadata, spellList, upcast = "";
    private boolean ritual = false, concentration = false, canUpcastBool = false;
    private ArrayList<String> availableClasses = new ArrayList<>();

    public Spell() {
        super();
    }

    // A spell has the unique features of having a spell level, school, metadata (including components and duration)
    // as well as what class spell lists the spell belongs to
    public Spell(String name, String source, String levelSchool, String metadata, String description, String upcast, String spellList) {
        super(name, source, description);
        this.levelSchool = levelSchool;
        this.metadata = metadata;

        this.upcast = upcast;
        if(!this.upcast.isEmpty()) { canUpcastBool = true; }
        if(metadata.contains("ritual")) { ritual = true; }
        if(metadata.contains("concentration")) { concentration = true; }

        this.spellList = spellList;
        separateSpellList();
    }

    private void separateSpellList() {
        String availableClassesStr = spellList.replaceAll("Spell Lists. ", "");
        String[] availableClassesArr = availableClassesStr.split(", ");
        for(String className : availableClassesArr) {
            availableClasses.add(className);
        }
    }

    public String getMetadata() { return  metadata; }

    public String getUpcast() {
        return upcast;
    }

    public String getLevelSchool() { return levelSchool; }

    public String getSpellList() {
        return spellList;
    }

    public ArrayList<String> getAvailableClasses() { return availableClasses; }

    public boolean canUpcast() {
        return canUpcastBool;
    }

    // Ritual and Concentrations are integral parts of spell
    // In the program itself, they are not used in any way
    // But in the case of extending this program, they are included anyways
    public boolean isRitual() { return ritual; }
    public boolean isConcentration() { return concentration; }

    public void printFullSpellDescription() {
        System.out.println(super.getName().toUpperCase());
        System.out.println(getDescription());
    }

}
