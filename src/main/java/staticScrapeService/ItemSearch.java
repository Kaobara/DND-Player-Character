package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;

public class ItemSearch extends MagicEntitySearchService {

//    Sets up the EntitySearch to be Item specific
    public ItemSearch() {
        super.ENTITY_URL_HREF = "/wondrous-items:";
        super.ENTITY_LIST_URL = "http://dnd5e.wikidot.com/wondrous-items";
        super.entityFactory = new ItemFactory();
        HtmlPage itemListPage = super.gotoPage(ENTITY_LIST_URL);
        super.entityList = this.getListOfEntities(itemListPage, "Item");
    }

    public ArrayList<String> getItemList() { return super.entityList; }
}
