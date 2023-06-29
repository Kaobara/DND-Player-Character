package characterCreatorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CharacterCreationEvents {
    private Scanner inputScanner = new Scanner(System.in);
    private CharacterEditingEvents PCEditEvents = new CharacterEditingEvents();

    public ProgramState bootUp() {
        ProgramState programState;
        while(true) {
            System.out.println("Would you like to create a character (1) or edit an existing character (2)?\n" +
                    "Please choose a number");
            try {
                int intState = inputScanner.nextInt();
                if(intState == 1) {
                    programState = ProgramState.CHARACTER_CREATION;
                } else if (intState == 2) {
                    programState = ProgramState.CHARACTER_EDITING_DIFFERENT;
                } else {
                    System.out.println("That's an invalid number. Please reenter");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                inputScanner.nextLine();
            }
        }
        inputScanner.nextLine();
        return programState;
    }

    public ProgramState choosePlayerCharacterState() {
        ProgramState programState;
        while(true) {
            System.out.println("Would you like to continue editing the current character (1), " +
                    "edit another existing character (2), create a brand new character (3), " +
                    "or close the program (0)?\n" +
                    "Please choose an option");

            try {
                int intState = inputScanner.nextInt();
                if (intState == 0 ){
                    programState = ProgramState.CLOSE_PROGRAM;
                } else if(intState == 1) {
                    programState = ProgramState.CHARACTER_EDITING_CURRENT;
                } else if (intState == 2) {
                    programState = ProgramState.CHARACTER_EDITING_DIFFERENT;
                    System.out.println("hm");
                } else if (intState == 3) {
                    programState = ProgramState.CHARACTER_CREATION;
                } else {
                    System.out.println("That's an invalid number. Please reenter");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                inputScanner.nextLine();
            }
        }
        inputScanner.nextLine();
        System.out.println("the current state is: " + programState.toString());
        return programState;
    }

    public PlayerCharacter buildNewCharacter() throws IOException {
        // Name
        System.out.println("Please choose a name");
        String name = inputScanner.nextLine();
        System.out.println("You chose the name: " + name);

        // Race
        Race race = PCEditEvents.buildRace();
        PlayerCharacter playerCharacter = new PlayerCharacter(name, race);

        // Class
        playerCharacter = PCEditEvents.buildBaseClass(playerCharacter);
        while(true) {
            System.out.println("Would you like to add more levels of another class? (Y/N)");
            String multiclassBool = inputScanner.nextLine();
            if(multiclassBool.equalsIgnoreCase("N")) {
                break;
            } else if (multiclassBool.equalsIgnoreCase("Y")) {
                playerCharacter = PCEditEvents.buildBaseClass(playerCharacter);
                inputScanner.nextLine();
            } else {
                System.out.println("Invalid Input. Please try again");
            }
        }

        playerCharacter.printCharacterDescriptionSummary();

        // Saving
        PCEditEvents.savePlayerCharacterEvent(playerCharacter);

        return playerCharacter;
    }

    public PlayerCharacter chooseSavedCharacter() throws IOException {
        ArrayList<PCJSONSkeleton> savedCharacters = PCEditEvents.loadSavedPlayerCharacters();
        if(savedCharacters.isEmpty()) {
            System.out.println("There are no characters currently saved. Please make and save a new character.");
            return null;
        }

        for(PCJSONSkeleton savedCharacter : savedCharacters) {
            System.out.print("(" + savedCharacters.indexOf(savedCharacter) + ") ");
            savedCharacter.printPCDescription();
        }

        int chosenCharacterIndex = -1;
        PlayerCharacter playerCharacter = null;

        while(true) {
            System.out.println("Please choose a character based on the index of the character");
            try {
                chosenCharacterIndex = inputScanner.nextInt();
                if (chosenCharacterIndex > -1 && chosenCharacterIndex < savedCharacters.size()) {
                    System.out.print("You chose: ");
                    savedCharacters.get(chosenCharacterIndex).printPCDescription();
                    System.out.println("Please wait a moment as we load up your character...");

                    playerCharacter = new PlayerCharacter(savedCharacters.get(chosenCharacterIndex));
                    break;
                }
                System.out.println("Sorry, that was an invalid option. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                inputScanner.nextLine();
            }
        }
        return playerCharacter;
    }





    public PlayerCharacter editSavedCharacter(PlayerCharacter playerCharacter) throws IOException {
        return PCEditEvents.editSavedCharacter(playerCharacter);
    }








}
