package characterCreatorService;

import com.fasterxml.jackson.databind.ser.Serializers;
import staticScrapeService.ClassFeatureSearch;
import staticScrapeService.RaceFeaturesSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class MainController {
    private String characterName;



    public static void main(String args[] ) {
        CharacterCreationEvents characterCreationEvents = new CharacterCreationEvents();
        ProgramState currentState = ProgramState.BOOT_UP;

        currentState = characterCreationEvents.bootUp();
        if(currentState == ProgramState.CHARACTER_CREATION) {
            PlayerCharacter playerCharacter = characterCreationEvents.buildInitialCharacter();
        }





        return;
    }


}
