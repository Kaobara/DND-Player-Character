package staticScrapeService;

import java.sql.Array;
import java.util.ArrayList;

public class Entity {
    final private int MAX_DESCRIPTION_LENGTH = 1023;
    private String name, source, description;
    private String URL;
    private boolean empty = false, descInitialized = false, hasTable = false;
    private ArrayList<ContentTable> tables = new ArrayList<>();
    private final ArrayList<String> descSections = new ArrayList<>();

    public Entity() {
        empty = true;
    }
    public boolean isEmpty() { return empty; }

    public Entity(String name, String source,String description) {
        this.name = name;
        this.source = source;
        this.description = description;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    // It is initially created with a long string of description
    // Due to Discord Embed fields having a max char size of 1024
    // This function splits the description into Description Section chunks
    // This allows embeds to fill fields based on sections
    private void buildDescriptionSections() {
        descInitialized = true;
        // Short description
        if(description.length() < MAX_DESCRIPTION_LENGTH) {
            descSections.add(description);
            return;
        }

        // Populate descSections
        String tempString = "";
        String[] descSplit = description.split("\n");
        String currentDescSection;
        int i = 0;
        for(String currentDescLine : descSplit) {
            currentDescSection = tempString;
            tempString += currentDescLine + "\n";

            // Check if a section has reached "maximum capacity"
            // If it has, add that section to the description arraylist
            // and start the next section
            if(tempString.length() > MAX_DESCRIPTION_LENGTH) {
                descSections.add(currentDescSection);
                tempString = currentDescLine + "\n";
            }

            // If it's the last line of the description, add it into the end.
            if (i == descSplit.length-1) {
                descSections.add(tempString);
            }
            i++;
        }
    }

    // Add tables into entity. If entity has no tables, set it. If it already has, add to collection.
    public void addTablesIntoEntity(ArrayList<ContentTable> tables) {
        if(hasTable == true) {
            this.tables.addAll(tables);
        } else {
            hasTable = true;
            this.tables = tables;
        }
    }

    public String getTableContent(int tableIndex){
        if(!hasTable) {
            return "";
        }
        return tables.get(tableIndex).getFullTable();
    }

    public ArrayList<ContentTable> getTables() {
        if(!hasTable) {
            return null;
        }
        return tables;
    }

    public String getDescription() { return description; }

    public boolean isHasTable() {
        return hasTable;
    }

    public ArrayList<String> getDescSections() {
        if(empty) {return new ArrayList<>(); }

        if (!descInitialized) {
            buildDescriptionSections();
        }
        return descSections;
    }
}
