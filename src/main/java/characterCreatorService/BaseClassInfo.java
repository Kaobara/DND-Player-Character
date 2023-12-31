package characterCreatorService;

import java.util.ArrayList;

public class BaseClassInfo {
    // TODO make this into a singleton
    private final ArrayList<String> classList = new ArrayList<String>() {
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

    private final ArrayList<String> SPELLCAST_FEAT = new ArrayList<String>() {
        {
            add("Spellcasting");
            add("Pact Magic");
        }
    };

    public void printClassList() {
        for(String className : classList) {
            System.out.println(className);
        }
    }

    public boolean checkClass(String className) {
        if(classList.contains(className.toLowerCase())) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getSpellcastFeats() {return SPELLCAST_FEAT; }
}
