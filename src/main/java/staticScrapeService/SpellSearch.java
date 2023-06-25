package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SpellSearch extends EntitySearch {

    // Sets up the EntitySearch to be Spell specific
    public SpellSearch() {
        super.ENTITY_URL_HREF = "/spell:";
        super.ENTITY_LIST_URL = "http://dnd5e.wikidot.com/spells";
        super.entityFactory = new SpellFactory();
        HtmlPage spellListPage = super.gotoPage(ENTITY_LIST_URL);
        super.entityList = getListofEntities(spellListPage, "Spell");
    }
}
