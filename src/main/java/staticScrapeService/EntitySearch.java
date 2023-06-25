package staticScrapeService;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.text.WordUtils;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntitySearch {
    protected final String WIKIDOT_URL = "http://dnd5e.wikidot.com";
    private final String CONTENT_TABLE_XPPATH = "//table[@class='wiki-content-table']";
    private final String ENTITY_LIST_XPPATH = "//div[@class='yui-navset']";
    protected String ENTITY_URL_HREF;
    protected String MAIN_CONTENT_ID = "page-content";
    protected String ENTITY_LIST_URL;
    protected EntityFactory entityFactory;

    protected ArrayList<String> entityList;

    // Create a web client to be used for scraping web pages
    private static WebClient createWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    // Go to a webpages based on a URL
    protected HtmlPage gotoPage(String URL) {
        WebClient webClient = createWebClient();
        HtmlPage page = null;

        try {
            page = webClient.getPage(URL);
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            System.exit(1);
        } catch (FailingHttpStatusCodeException e) {
            // Website sends failing http status exception
            // Possible reasoning: URL does not exist
            System.out.println("An error occurred : " + e);
            System.out.println("Please check that your input is correct");
            System.exit(0);
        }

        return page;
    }

    // Using an HtmlPage, this function returns a sorted list of all entities
    // This is primarily used to find a list of all spells or items for checking correct input
    protected ArrayList<String> getListofEntities(HtmlPage page, String entityType) {
        HtmlDivision magicItems = page.getFirstByXPath(ENTITY_LIST_XPPATH);
        List<HtmlTable> magicTables = magicItems.getByXPath(CONTENT_TABLE_XPPATH);

        ArrayList<String> entityStrings = new ArrayList<>();

        // As of now, spells and items webpages has tables in which the header cell is under "Spell Name" or "Item Name"
        // Because of this, scraping through filtering this is currently hardcoded into the code
        for(HtmlTable magicTable : magicTables) {
            for (final HtmlTableRow row : magicTable.getRows()) {
                if(row.getCell(0).getTextContent().compareTo(entityType + " Name") != 0) {
                    entityStrings.add(WordUtils.capitalizeFully(row.getCell(0).getTextContent()));
                }
            }
        }

        Collections.sort(entityStrings);
        return  entityStrings;
    }

    // grabs all strings that has the id <p> (paragraph> or <li> (lists). Ignore any <br> (break) tags, as well as add
    // "  - " at the start of any bullet point lists.
    private ArrayList<String> getContentByID(String URL, String elementId) {
        HtmlPage page = gotoPage(URL);
        DomElement element = page.getElementById(elementId);
        ArrayList<String> textContents = new ArrayList<>();
        DomNodeList<DomNode> nodes = element.querySelectorAll("p, li");

        for(DomNode node : nodes) {
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

    // Some nodes has <em> and <strong> names to make content either italicized or bolded.
    // This function manually changes forms a string with the appropriate tags for markdown
    private String formatParagraphNodes(String formattedText, DomNode node) {
        if(node.getNodeName().contains("em")) {
            formattedText = "_" + formattedText + "_";
        } else if(node.getNodeName().contains("strong")) {
            formattedText = "**" + formattedText + "**";
        }
        return formattedText;
    }

    // Search up an entity based on its name. Uses above methods
    public Entity searchEntityInfo(String entityName) {
        entityName = entityName.toLowerCase();
        String entityNameHref = entityName.toLowerCase().replace(" ", "-");
        entityName = WordUtils.capitalizeFully(entityName);
        if(!entityList.contains(entityName)){
            return entityFactory.createEmptyEntity();
        }
        entityNameHref = entityNameHref.replace("'", "");
        entityNameHref = entityNameHref.replace(":", "");

        String entityURL = WIKIDOT_URL + ENTITY_URL_HREF + entityNameHref;
        ArrayList<String> entityContent = getContentByID(entityURL, MAIN_CONTENT_ID);
        ArrayList<ContentTable> tables = getTables(entityURL);

        Entity entity = entityFactory.createEntity(entityName, entityContent, tables);
        entity.setURL(entityURL);

        return entity;
    }

    private void getAllLinks(HtmlPage page) {
        List<HtmlAnchor> links = page.getAnchors();
        for (HtmlAnchor link : links) {
            String href = link.getHrefAttribute();
            System.out.println("Link: " + href);
        }
    }

    public EntitySearch() {
    }


}