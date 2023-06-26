package staticScrapeService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SearchService {
    protected final String WIKIDOT_URL = "http://dnd5e.wikidot.com";
    protected final String CONTENT_TABLE_XPPATH = "//table[@class='wiki-content-table']";
    protected final String ENTITY_LIST_XPPATH = "//div[@class='yui-navset']";
    protected String ENTITY_URL_HREF;
    protected String MAIN_CONTENT_ID = "page-content";
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
    protected abstract ArrayList<String> getListOfEntities(HtmlPage page, String entityFeatureType);

    // grabs all strings that has the id <p> (paragraph> or <li> (lists). Ignore any <br> (break) tags, as well as add
    // "  - " at the start of any bullet point lists.
    protected DomNodeList<DomNode> getPageContentNodes(String URL, String elementId, String querySelectors) {
        HtmlPage page = gotoPage(URL);
        DomElement element = page.getElementById(elementId);
        DomNodeList<DomNode> nodes = element.querySelectorAll(querySelectors);

        return nodes;
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

    // Search up an entity based on its name. Uses above methods
    public abstract Entity searchInfo(String entityName);

    // If a page has at least 1 table in it, grab all tables and contents of the tables as a list of ContentTables
    protected ArrayList<ContentTable> getTables(String URL) {
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

    private void getAllLinks(HtmlPage page) {
        List<HtmlAnchor> links = page.getAnchors();
        for (HtmlAnchor link : links) {
            String href = link.getHrefAttribute();
            System.out.println("Link: " + href);
        }
    }
}
