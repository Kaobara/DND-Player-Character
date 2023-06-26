package staticScrapeService;

public class RaceFeatures extends  Entity{
    public RaceFeatures() {super(); }

    // The only field that is unique to items is an item's type and rarity.
    public RaceFeatures(String name, String source, String additionalStrInfo, int additionalIntInfo, String description) {
        super(name, source, description);
    }
}
