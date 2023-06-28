package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;

public class SpellSearch extends MagicEntitySearchService {

    // Sets up the EntitySearch to be Spell specific
    public SpellSearch() {
        super.ENTITY_URL_HREF = "/spell:";
        super.ENTITY_LIST_URL = "http://dnd5e.wikidot.com/spells";
        super.entityFactory = new SpellFactory();
        HtmlPage spellListPage = super.gotoPage(ENTITY_LIST_URL);
        super.entityList = this.getListOfEntities(spellListPage, "Spell");
    }

    public ArrayList<String> getSpellList() { return super.entityList; }
}
