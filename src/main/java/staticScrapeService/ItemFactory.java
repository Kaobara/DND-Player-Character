package staticScrapeService;

import java.util.ArrayList;

public class ItemFactory extends EntityFactory {

    public Item createEmptyEntity() { return new Item(); }

    public Item createEntity(String itemName, ArrayList<String> fullContent, ArrayList<ContentTable> tables,
                             String additionalStrInfo, int additionalIntInfo) {
        // Split source, typeRarity, and description into separate categories
        String source = fullContent.remove(TOP_I).replaceFirst("Source: ", "");;
        String typeRarity = fullContent.remove(TOP_I);

        String description = getDescription(fullContent);
        Item item = new Item(itemName, source, typeRarity, description);
        if(tables != null){
            item.addTablesIntoEntity(tables);
        }
        return item;
    }

}
