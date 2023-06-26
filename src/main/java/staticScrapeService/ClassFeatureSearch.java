package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassFeatureSearch extends SearchService {
    protected String FEATURE_SOURCE_NAME;
    protected HashMap<Integer, String[]> levelFeatureMap = new HashMap<>();
    protected HashMap<String, Integer> featureLevelMap = new HashMap<>();

    public ClassFeatureSearch(String className) {
        FEATURE_SOURCE_NAME = className.toLowerCase();
        super.ENTITY_URL_HREF = "/" + className.toLowerCase();
        HtmlPage classFeatureListPage = super.gotoPage(WIKIDOT_URL + ENTITY_URL_HREF);
        super.entityList = getListOfEntities(classFeatureListPage, "class feature");
        super.entityFactory = new ClassFeatureFactory();
    }

    protected ArrayList<String> getListOfFeatures(HtmlPage page) {
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
                    String[] featureNames = cell.getTextContent().toLowerCase().split(", ");
                    levelFeatureMap.put(level, featureNames);
                    for(String featureName : featureNames) {
                        features.add( WordUtils.capitalizeFully(featureName));
                        System.out.println("Current Level: "+level);
                        featureLevelMap.put(featureName.toLowerCase(), level);
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

    private ArrayList<String> getClassContent(String URL, String elementId, String featureName) {
        DomNodeList<DomNode> nodes = super.getPageContentNodes(URL, elementId, "h3, p, li");
        ArrayList<String> textContents = new ArrayList<>();
        boolean correctSpace = false;

        for(DomNode node : nodes) {
            if(node.getNodeName().equalsIgnoreCase("h3")){
                if(node.getTextContent().equalsIgnoreCase(featureName)) {
                    correctSpace = true;
                } else correctSpace = false;
                continue;
            }

            if(correctSpace == false) continue;
            StringBuilder paragraphContent = new StringBuilder();

            // Some nodes contain child nodes that usually includes data of its text format (bold or italicized)
            // This for block allows those paragraph to be in the proper format
            // Note: This does not work on anything that is both bolded or italicized
            for(DomNode childNode : node.getChildNodes()) {
                if(childNode.getNodeName().equalsIgnoreCase("br")){
                    continue;
                }
                String formattedText = formatParagraphNodes(childNode.getTextContent(), childNode);
                paragraphContent.append(formattedText);
            }

            if(node.getNodeName().contains("li")) { paragraphContent.insert(0, "   - "); }
            textContents.add(paragraphContent.toString());
        }

        return textContents;
    }

    protected ArrayList<String> getListOfEntities(HtmlPage page, String entityFeatureType) {
        return getListOfFeatures(page);
    }

//    public Entity searchInfo(String featureName) { return null; }

    public Entity searchInfo( String featureName) {
        featureName = featureName.toLowerCase();
        featureName = WordUtils.capitalizeFully(featureName);
        if(!entityList.contains(featureName)){
            return entityFactory.createEmptyEntity();
        }

        String entityURL = WIKIDOT_URL + ENTITY_URL_HREF;
        ArrayList<String> entityContent = getClassContent(entityURL, MAIN_CONTENT_ID, featureName);
        ArrayList<ContentTable> tables = getTables(entityURL);


        Entity entity = entityFactory.createEntity(featureName, entityContent, tables, FEATURE_SOURCE_NAME,
                featureLevelMap.get(featureName.toLowerCase()));

        return entity;
    }

}
