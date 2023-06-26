package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.text.WordUtils;

import java.sql.Array;
import java.util.ArrayList;

public class RaceFeaturesSearch extends SearchService {
    public RaceFeaturesSearch() {
        super.entityList = getListOfEntities(null, "class feature");
        super.entityFactory = new RaceFeaturesFactory();
    }

    @Override
    protected ArrayList<String> getListOfEntities(HtmlPage page, String entityFeatureType) {
        ArrayList<String> listOfRaces = new ArrayList<String>() {
            {
                add("elf");
                add("dwarf");
                add("tiefling");
                add("human");
                add("half-elf");
                add("half-orc");
                add("gnome");
                add("halfling");
                add("dragonborn");
            }
        };

        return listOfRaces;
    }

    public ArrayList<String> getListOfRaces() {
        return entityList;
    }

    private ArrayList<String> getRaceContent(String URL, String elementId, String featureName) {
        DomNodeList<DomNode> nodes = super.getPageContentNodes(URL, elementId, "h1, h3, p, li");
        ArrayList<String> textContents = new ArrayList<>();
        boolean correctSpace = false;

        for(DomNode node : nodes) {
            if(node.getNodeName().equalsIgnoreCase("h1")) {
                correctSpace = true;
            } else if (node.getNodeName().equalsIgnoreCase("h3")) {
                correctSpace = false;
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

    @Override
    public Entity searchInfo(String raceName) {
        raceName = raceName.toLowerCase();
        super.ENTITY_URL_HREF = "/" + raceName;
        if(!entityList.contains(raceName)){
            return entityFactory.createEmptyEntity();
        }

        String entityURL = WIKIDOT_URL + ENTITY_URL_HREF;
        ArrayList<String> entityContent = getRaceContent(entityURL, MAIN_CONTENT_ID, raceName);
        ArrayList<ContentTable> tables = getTables(entityURL);


        Entity entity = entityFactory.createEntity(raceName, entityContent, tables, "", -1);
        return entity;
    }
}
