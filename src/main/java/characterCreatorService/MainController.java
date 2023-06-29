package characterCreatorService;

import java.io.IOException;

public class MainController {
    public static void main(String[] args ) throws IOException {
        CharacterCreationEvents characterCreationEvents = new CharacterCreationEvents();
        PlayerCharacter playerCharacter = null;
        ProgramState currentState = ProgramState.BOOT_UP;
        boolean programLive = true;

        // Change and execute events depending on the current state.
        while(programLive) {
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
        }
    }


}
