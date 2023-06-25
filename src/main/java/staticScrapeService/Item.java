package staticScrapeService;

public class Item extends  Entity{
    private String typeRarity;
    public Item() {super(); }

    // The only field that is unique to items is an item's type and rarity.
    public Item(String name, String source, String typeRarity, String description) {
        super(name, source, description);
        this.typeRarity = typeRarity;
    }

    public String getTypeRarity() {
        return typeRarity;
    }
}
