package savedDataService;

import characterCreatorService.PCJSONSkeleton;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONCharacterWriter {
    public boolean characterExists(File file, String playerCharacterID) throws IOException {
        System.out.println("Finding Saved Character with ID " + playerCharacterID);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);

        for(JsonNode characterNode : rootNode) {
            System.out.println("Unique ID for " + characterNode.get("name").toString() + " is " + characterNode.get("uniqueID").toString());
            if(characterNode.get("uniqueID").toString().equalsIgnoreCase("\""+playerCharacterID+"\"")) {
                return true;
            }
        }

        return false;
    }

    public void addCharacterToFile(File file, JsonNode rootNode, ObjectMapper mapper, PCJSONSkeleton pcJSONSkeleton) throws IOException {
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

    public void removeCharacterFromFile(File file, JsonNode rootNode, ObjectMapper mapper, PCJSONSkeleton pcJSONSkeleton) throws IOException {
        ArrayNode editedNode = rootNode.deepCopy();
        pcJSONSkeleton.printPCDescription();
        for(int i = 0; i <editedNode.size(); i++) {
            if(editedNode.get(i).get("uniqueID").toString()
                    .equalsIgnoreCase("\"" + pcJSONSkeleton.getUniqueID() + "\"")) {
                editedNode.remove(i);
                System.out.println("REMOVE SUCCESSFUL");
                break;
            }
        }
        for(int i = 0; i <editedNode.size(); i++) {
            System.out.println(editedNode.get(i).get("uniqueID").toString());
            System.out.println(editedNode.get(i).get("name").toString());
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }

    public void overwriteCharacterInFile(File file, JsonNode rootNode, ObjectMapper mapper, PCJSONSkeleton pcJSONSkeleton) throws IOException {
        ArrayNode editedNode = rootNode.deepCopy();
        pcJSONSkeleton.printPCDescription();
        for(int i = 0; i <editedNode.size(); i++) {
            if(editedNode.get(i).get("uniqueID").toString()
                    .equalsIgnoreCase("\"" + pcJSONSkeleton.getUniqueID() + "\"")) {
                editedNode.remove(i);
                System.out.println("REMOVE SUCCESSFUL");
                break;
            }
        }
        for(int i = 0; i <editedNode.size(); i++) {
            System.out.println(editedNode.get(i).get("uniqueID").toString());
            System.out.println(editedNode.get(i).get("name").toString());
        }
        editedNode.addPOJO(pcJSONSkeleton);

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);

    }
}
