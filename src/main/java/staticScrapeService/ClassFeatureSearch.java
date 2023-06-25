package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassFeatureSearch extends FeatureSearch{
    //    Sets up the EntitySearch to be Item specific
    HashMap<Integer, String[]> levelFeatureMap = new HashMap<>();

    public ClassFeatureSearch() {
        super.ENTITY_URL_HREF = "/sorcerer";
        super.ENTITY_LIST_URL = "http://dnd5e.wikidot.com/sorcerer";
        super.entityList = getListOfClassFeatures(WIKIDOT_URL + ENTITY_URL_HREF);
//        super.entityFactory = new ItemFactory();
//        HtmlPage itemListPage = super.gotoPage(ENTITY_LIST_URL);
//        super.entityList = getListofEntities(itemListPage, "Item");



        // If a page has at least 1 table in it, grab all tables and contents of the tables as a list of ContentTables


    }

    private ArrayList<String> getListOfClassFeatures(String URL) {
        HtmlPage page = gotoPage(URL);
        List<HtmlTable> originalTables = page.getByXPath(super.CONTENT_TABLE_XPPATH);

        // There are no tables in this page
        if(originalTables.isEmpty()) { return null; }
        HtmlTable classTable =  originalTables.get(0);

        int numRows = classTable.getRowCount();
        int numColumns = 0;
        int featureColumn = -1;
        boolean firstLoop = true;
        int level = 0;

        ArrayList<ArrayList<String>> rowContents = new ArrayList<>();
        ArrayList<String> features = new ArrayList<>();

        for (final HtmlTableRow row : classTable.getRows()) {
            int currentColumn = 0;
            // Deal with Headers (might make into its own method
            DomNodeList<DomNode> nodes = row.querySelectorAll("th");
            if(!nodes.isEmpty()) {
                numColumns = 0;

                ArrayList<String> currentHeaderRow = new ArrayList<>();
                for (DomNode node : nodes) {
                    System.out.println("Current num COlumns: " + numColumns);
                    currentHeaderRow.add(node.getTextContent());
                    System.out.println(node.getTextContent());

                    if(node.getTextContent().equalsIgnoreCase("features")) featureColumn = numColumns;
                    numColumns++;
                }

                level = 1;
//                headers.add(currentHeaderRow);
                System.out.println("This happened");
                continue;
            }

            // Every other row
            ArrayList<String> currentRow = new ArrayList<>();
            System.out.println("feature Column is: " + featureColumn);

            // Cell per row
            for(final HtmlTableCell cell : row.getCells()) {
                // Count the number of columns of the table by only the first row

//                System.out.println("current column: " + currentColumn);
                if(featureColumn != -1 && !cell.getTextContent().isEmpty() && currentColumn == featureColumn) {
                    String[] featureNames = cell.getTextContent().split(", ");
                    for(String featureName : featureNames) {
                        features.add(featureName);
                        System.out.println("Current Level: "+level);
                        levelFeatureMap.put(level, featureNames);
                    }
                    System.out.println(cell.getTextContent());
                    currentRow.add(cell.getTextContent());

                }
                currentColumn++;
            }
            level ++;
            if(firstLoop) {
                firstLoop = false;
            }
            rowContents.add(currentRow);
        }

        for(int classLevel : levelFeatureMap.keySet()) {
            for(String featureName : levelFeatureMap.get(classLevel)) {
                System.out.println("feature: " + featureName + " level: "+ classLevel);
            }
        }
//        return classTable;
        return features;

    }
}
