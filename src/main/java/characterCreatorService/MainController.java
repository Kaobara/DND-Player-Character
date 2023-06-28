package characterCreatorService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import savedDataService.JSONCharacterWriter;

import java.io.File;
import java.io.IOException;

public class MainController {
    private String characterName;




    public static void main(String args[] ) throws IOException {
        CharacterCreationEvents characterCreationEvents = new CharacterCreationEvents();
        JSONCharacterWriter jsonCharacterWriter = new JSONCharacterWriter();
        PlayerCharacter playerCharacter = null;
        ProgramState currentState = ProgramState.BOOT_UP;
        boolean programLive = true;
        while(programLive) {
            System.out.println("Start of loop. Current state is now: " + currentState.toString());
            switch (currentState) {
                case CHOOSING_STATE:
                    currentState = characterCreationEvents.choosePlayerCharacterState();
                    break;
                case BOOT_UP:
                    currentState = characterCreationEvents.bootUp();
                    break;
                case CHARACTER_CREATION:
                    playerCharacter = characterCreationEvents.buildNewCharacter();
                    currentState = ProgramState.CHOOSING_STATE;
                    break;
                case CHARACTER_EDITING_CURRENT:
                    if(playerCharacter == null) {
                        System.out.println("You have not created or saved a new character yet. Please try again");
                    } else {
                        playerCharacter = characterCreationEvents.editSavedCharacter(playerCharacter);
                    }
                    currentState = ProgramState.CHOOSING_STATE;
                    break;
                case CHARACTER_EDITING_DIFFERENT:
                    playerCharacter = characterCreationEvents.chooseSavedCharacter();
                    playerCharacter = characterCreationEvents.editSavedCharacter(playerCharacter);
                    currentState = ProgramState.CHOOSING_STATE;
                    break;
                case CLOSE_PROGRAM:
                    programLive = false;
                    break;
            }
            System.out.println("The state is now: " + currentState.toString());
        }

//        if(currentState == ProgramState.CHARACTER_CREATION) {
//            playerCharacter = characterCreationEvents.buildNewCharacter();
//        }

//        String channelDataDirectory = "DataStorage/savedCharacters.json";
//
//        File file = new File(channelDataDirectory);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(file);
//
//        PCJSONSkeleton pcjsonSkeleton = playerCharacter.createPCJSONSkeleton();
//
//        jsonCharacterWriter.addCharacterToFile(file, rootNode, pcjsonSkeleton);







        return;
    }


}
