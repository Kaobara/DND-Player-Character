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
    // TODO make list of race entities something available for the entire project (for encapsulation and adding more races)
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

            for(DomNode childNode : node.getChildNodes()) {
                if(childNode.getNodeName().equalsIgnoreCase("br")){
                    continue;
                }
                paragraphContent.append(childNode.getTextContent());
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
