package savedDataService;

import characterCreatorService.PCJSONSkeleton;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONCharacterWriter {
    public JsonNode characterExists(File file, String playerCharacterID) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);

        JsonNode finalNode = null;

        for(JsonNode characterNode : rootNode) {
            if(characterNode.get("uniqueID").toString().equalsIgnoreCase(playerCharacterID)) {
                finalNode = characterNode;
            }
        }

        return finalNode;
    }

    public void addCharacterToFile(File file, JsonNode rootNode, PCJSONSkeleton pcJSONSkeleton) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode editedNode = rootNode.deepCopy();
        editedNode.addPOJO(pcJSONSkeleton);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }

    public ArrayList<JsonNode> getCharacterNodes(File file) throws IOException {
        ArrayList<JsonNode> nodes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);

        JsonNode finalNode = null;

        for(JsonNode characterNode : rootNode) {
            nodes.add(characterNode);
        }

        return nodes;
    }

    public void removeChannelFromFile(File file, JsonNode rootNode, PCJSONSkeleton pcJSONSkeleton) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode editedNode = rootNode.deepCopy();
        for(int i = 0; i <editedNode.size(); i++) {
            if(editedNode.get("uniqueID").toString().equalsIgnoreCase(pcJSONSkeleton.getUniqueID())) {
                editedNode.remove(i);
                System.out.println("REMOVE SUCCESSFUL");
                break;
            }
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }
}
