package staticScrapeService;

public class Spell extends Entity {
    private String levelSchool, metadata, spellList, upcast = "";
    private boolean ritual = false, concentration = false, canUpcastBool = false;

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
    }

    public String getMetadata() { return  metadata; }

    public String getUpcast() {
        return upcast;
    }

    public String getLevelSchool() { return levelSchool; }

    public String getSpellList() {
        return spellList;
    }

    public boolean canUpcast() {
        return canUpcastBool;
    }

    // Ritual and Concentrations are integral parts of spell
    // In the program itself, they are not used in any way
    // But in the case of extending this program, they are included anyways
    public boolean isRitual() { return ritual; }
    public boolean isConcentration() { return concentration; }

}
