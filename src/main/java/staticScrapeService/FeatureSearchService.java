package staticScrapeService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.text.WordUtils;

import java.io.IOException;
import java.util.*;

public abstract class FeatureSearchService extends SearchService {
    protected HashMap<Integer, String[]> levelFeatureMap = new HashMap<>();
    protected HashMap<String, Integer> featureLevelMap = new HashMap<>();

    // grabs all strings that has the id <p> (paragraph> or <li> (lists). Ignore any <br> (break) tags, as well as add
    // "  - " at the start of any bullet point lists.
    private ArrayList<String> getContentByID(String URL, String elementId, String featureName) {
        HtmlPage page = gotoPage(URL);
        DomElement element = page.getElementById(elementId);
        ArrayList<String> textContents = new ArrayList<>();
        DomNodeList<DomNode> nodes = element.querySelectorAll("h3, p, li");
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

    // If a page has at least 1 table in it, grab all tables and contents of the tables as a list of ContentTables
    private ArrayList<ContentTable> getTables(String URL) {
        HtmlPage page = gotoPage(URL);
        List<HtmlTable> originalTables = page.getByXPath(CONTENT_TABLE_XPPATH);

        // There are no tables in this page
        if(originalTables.isEmpty()) { return null; }

        ArrayList<ContentTable> tables = new ArrayList<>();
        for(HtmlTable table : originalTables) {
            ContentTable contentTable = new ContentTable(table);
            tables.add(contentTable);
        }
        return tables;
    }

    protected ArrayList<String> getListOfEntities(HtmlPage page, String entityFeatureType) {
        return getListOfClassFeatures(page);
    }

    // Some nodes has <em> and <strong> names to make content either italicized or bolded.
    // This function manually changes forms a string with the appropriate tags for markdown
    protected String formatParagraphNodes(String formattedText, DomNode node) {
        if(node.getNodeName().contains("em")) {
            formattedText = "_" + formattedText + "_";
        } else if(node.getNodeName().contains("strong")) {
            formattedText = "**" + formattedText + "**";
        }
        return formattedText;
    }

//    public Entity searchInfo(String featureName) { return null; }

    public Entity searchInfo( String featureName) {
        featureName = featureName.toLowerCase();
        String featureNameHref = featureName.toLowerCase().replace(" ", "-");
        featureName = WordUtils.capitalizeFully(featureName);
        if(!entityList.contains(featureName)){
            return entityFactory.createEmptyEntity();
        }
//        entityNameHref = entityNameHref.replace("'", "");
//        entityNameHref = entityNameHref.replace(":", "");

        String entityURL = WIKIDOT_URL + ENTITY_URL_HREF;
        ArrayList<String> entityContent = getContentByID(entityURL, MAIN_CONTENT_ID, featureName);
        ArrayList<ContentTable> tables = getTables(entityURL);
//        getHTMLTable(entityURL);


        Entity entity = entityFactory.createEntity(featureName, entityContent, tables, ENTITY_URL_HREF,
                featureLevelMap.get(featureName.toLowerCase()));
//        entity.setURL(entityURL);
//        if(entity.isEmpty()) {
//            System.out.println("Damn");
//        }

//        for(String desct : entity.getDescSections()) {
//            System.out.println(desct);
//        }
////

        return entity;
    }

    protected abstract ArrayList<String> getListOfClassFeatures(HtmlPage page);



//    private String[] featureList(String features) {
//        String[] featureList = features.split(",");
//    }




}