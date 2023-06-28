package staticScrapeService;

import java.sql.Array;
import java.util.ArrayList;

public class Entity {
    private String name, source, description;
    private String URL;
    private boolean empty = false, descInitialized = false, hasTable = false;
    private ArrayList<ContentTable> tables = new ArrayList<>();

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
}
