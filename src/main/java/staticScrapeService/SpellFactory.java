package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SpellFactory extends EntityFactory {

    public Spell createEmptyEntity() { return new Spell(); }


    public Spell createEntity(String spellName, ArrayList<String> fullContent, ArrayList<ContentTable> tables,
                              String additionalInfo, int additionalIntInfo) {
        // Split source, levelSchool, metadata, and spellList into separate categories
        String source = fullContent.remove(TOP_I).replaceFirst("Source: ", "");
        String levelSchool = fullContent.remove(TOP_I);
        String metaData = fullContent.remove(TOP_I);
        String spellList = fullContent.remove(fullContent.size() + BOTTOM_I).replaceFirst("..Spell Lists\\... ", "");

        // If the spell can be upcast, place it into its own category as well
        String upcast = "";
        if(fullContent.get(fullContent.size() + BOTTOM_I).contains("**At Higher Levels.** ")) {
            upcast = fullContent.remove(fullContent.size() + BOTTOM_I).replace("**At Higher Levels.** ", "");
        }

        // Combine the description into a single string
        String description = super.getDescription(fullContent);

        Spell spell = new Spell(spellName, source, levelSchool, metaData, description, upcast, spellList);

        if(tables != null){
            spell.addTablesIntoEntity(tables);
        }

        return spell;
    }

}
