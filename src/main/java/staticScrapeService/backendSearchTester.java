package staticScrapeService;

import java.io.IOException;

public class backendSearchTester {
    // This class is primarily used to test searches
    public static void main(String[] args) throws IOException{

        EntitySearch service = new EntitySearch();

        SpellSearch spellSearch = new SpellSearch();
        ItemSearch itemSearch = new ItemSearch();
//        Item entity = (Item) itemSearch.searchEntityInfo("Potion of Healing");
        Spell entity = (Spell) spellSearch.searchEntityInfo("Wish");

        for(String desct : entity.getDescSections()) {
            System.out.println(desct);
        }
        if(entity.isHasTable()){
            System.out.println(entity.getTableContent(0));
        }
    }


}
