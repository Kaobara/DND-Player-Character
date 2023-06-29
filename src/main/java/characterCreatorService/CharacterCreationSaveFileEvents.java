package characterCreatorService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import savedDataService.JSONCharacterWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CharacterCreationSaveFileEvents {
    private final String channelDataDirectory = "DataStorage/savedCharacters.json";
    private final JSONCharacterWriter jsonCharacterWriter = new JSONCharacterWriter();

    // Save a player character into the save file
    public void savePlayerCharacter(PlayerCharacter playerCharacter) throws IOException {
        File file = new File(channelDataDirectory);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        PCJSONSkeleton pcjsonSkeleton = playerCharacter.createPCJSONSkeleton();

        if(jsonCharacterWriter.characterExists(file, playerCharacter.getUniqueID())) {
            System.out.println("yes");
            jsonCharacterWriter.overwriteCharacterInFile(file, rootNode, objectMapper, pcjsonSkeleton);
            return;
        }
        jsonCharacterWriter.addCharacterToFile(file, rootNode, objectMapper, pcjsonSkeleton);
    }

    // Load up all saved player character as JSON skeletons.
    public ArrayList<PCJSONSkeleton> loadSavedPlayerCharacters() throws IOException {
        ArrayList<PCJSONSkeleton> pcjsonSkeletons = new ArrayList<>();
        File file = new File(channelDataDirectory);
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<JsonNode> characterNodes = jsonCharacterWriter.getCharacterNodes(file);
        for(JsonNode characterNode : characterNodes) {
            pcjsonSkeletons.add(objectMapper.readValue(characterNode.toPrettyString(), PCJSONSkeleton.class));
        }

        return pcjsonSkeletons;
    }

}
