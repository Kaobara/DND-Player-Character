package savedDataService;

import characterCreatorService.PCJSONSkeleton;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONCharacterWriter {
    // If a character exists in the save file, return true
    public boolean characterExists(File file, String playerCharacterID) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);

        for(JsonNode characterNode : rootNode) {
            if(characterNode.get("uniqueID").toString().equalsIgnoreCase("\""+playerCharacterID+"\"")) {
                return true;
            }
        }

        return false;
    }

    // Add a newly made character into the save file
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

    // entirely remove a character from the save file
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

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }

    // Overwrite a character from a save file if they were modified
    public void overwriteCharacterInFile(File file, JsonNode rootNode, ObjectMapper mapper, PCJSONSkeleton pcJSONSkeleton) throws IOException {
        ArrayNode editedNode = rootNode.deepCopy();
        pcJSONSkeleton.printPCDescription();
        for(int i = 0; i <editedNode.size(); i++) {
            if(editedNode.get(i).get("uniqueID").toString()
                    .equalsIgnoreCase("\"" + pcJSONSkeleton.getUniqueID() + "\"")) {
                editedNode.remove(i);
                break;
            }
        }
        editedNode.addPOJO(pcJSONSkeleton);

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);

    }
}
