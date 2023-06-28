package characterCreatorService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.WordUtils;
import savedDataService.JSONCharacterWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CharacterCreationEvents {
    private BaseClassInfo baseClassInfo = new BaseClassInfo();
    private Scanner inputScanner = new Scanner(System.in);
    private final String channelDataDirectory = "DataStorage/savedCharacters.json";
    private final JSONCharacterWriter jsonCharacterWriter = new JSONCharacterWriter();

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
//        inputScanner.close();
        inputScanner.nextLine();
        return programState;
    }

    public ProgramState choosePlayerCharacterState() {
        ProgramState programState;
        while(true) {
            System.out.println("Would you like to continue editing the current character (1), " +
                    "or edit another existing character (2), or create a brand new character (3)?\n" +
                    "Please choose a number");
            try {
                int intState = inputScanner.nextInt();
                if(intState == 1) {
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
        Race race = new Race();

        System.out.println("Please choose a name");
        String name = inputScanner.nextLine();
        System.out.println("You chose the name: " + name);

        System.out.println("Please choose a race. The available races are: ");
        race.printRaceList();
        String raceName;
        while(true) {
            raceName = inputScanner.nextLine();
            if(race.checkRace(raceName)) break;
            System.out.println("Sorry, the race " + raceName + " is unavailable. Please try again.");
        }

        System.out.println("You chose the race: " + raceName);

        PlayerCharacter playerCharacter = new PlayerCharacter(name, new Race(raceName));

        playerCharacter = buildBaseClass(playerCharacter);


        while(true) {
            System.out.println("Would you like to add more levels of another class? (Y/N)");
            String multiclassBool = inputScanner.nextLine();
            if(multiclassBool.equalsIgnoreCase("N")) {
                break;
            } else if (multiclassBool.equalsIgnoreCase("Y")) {
                playerCharacter = buildBaseClass(playerCharacter);
                inputScanner.nextLine();
            } else {
                System.out.println("Invalid Input. Please try again");
            }
        }

        playerCharacter.printCharacterDescriptionSummary();

        while(true) {
            System.out.println("Would you like to save the character? (Y/N)");
            String multiclassBool = inputScanner.nextLine();
            if(multiclassBool.equalsIgnoreCase("N")) {
                break;
            } else if (multiclassBool.equalsIgnoreCase("Y")) {
                savePlayerCharacter(playerCharacter);
                System.out.println(playerCharacter.getName() + " saved.");
                break;
            } else {
                System.out.println("Invalid Input. Please try again");
            }
        }
        return playerCharacter;
    }

    public PlayerCharacter chooseSavedCharacter() throws IOException {
        ArrayList<PCJSONSkeleton> savedCharacters = loadSavedPlayerCharacters();
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
                    System.out.println("Please wait a moment as we are loading up your character...");

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

    public PlayerCharacter editSavedCharacter() throws IOException {
        PlayerCharacter playerCharacter = chooseSavedCharacter();
        System.out.println("This works");
        return playerCharacter;
    }

    public PlayerCharacter buildBaseClass(PlayerCharacter playerCharacter) {
        System.out.println("Please choose a Class. The available classes are: ");
        baseClassInfo.printClassList();
        String className;
        while(true) {
            className = inputScanner.nextLine();
            if(baseClassInfo.checkClass(className)) break;
            System.out.println("Sorry, the class " + className + " is unavailable. Please try again.");
        }
        System.out.println("You chose the class: " + className);

        if(playerCharacter.getClassNames().contains(className)) {
            System.out.println(playerCharacter.getName() + " already has levels in " + className + ".\n" +
                    "Would you like to level up the class? (Y/N)");
            while(true) {
                String levelUpStrBool = inputScanner.nextLine();
                if(levelUpStrBool.equalsIgnoreCase("N")) {
                    System.out.println(playerCharacter.getName() + " does not level up.");
                    return playerCharacter;
                } else if (levelUpStrBool.equalsIgnoreCase("Y")) {
                    playerCharacter = levelUpClass(playerCharacter, className);
                    return playerCharacter;
                } else {
                    System.out.println("Invalid Input. Please try again");
                }
            }
        }

        System.out.println("How many " + className + " levels does " + playerCharacter.getName() + " have? Maximum of 20 levels.");
        System.out.println(playerCharacter.getName() + " currently has " + playerCharacter.getTotalLevels() + " levels");
        int classLevel = -1;
        while(true) {
            try {
                classLevel = inputScanner.nextInt();
                if (classLevel > 0 && playerCharacter.getTotalLevels() + classLevel <= 20) {
                    break;
                }
                System.out.println("Sorry, " + classLevel + " is an invalid level. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                inputScanner.nextLine();
            }
        }
        System.out.println("Please be patient as we are currently building your class...");

//        System.out.println(playerCharacter.getName() + " is a level " + classLevel + " " + className);

        playerCharacter.giveCharacterBaseClass(new BaseClass(className, classLevel));


        inputScanner.nextLine();

        return playerCharacter;
    };

    public PlayerCharacter levelUpClass(PlayerCharacter playerCharacter, String className) {
        System.out.println(playerCharacter.getName() + " is currently level " + playerCharacter.getTotalLevels()
                + " and has " + playerCharacter.getLevelInClass(className) + " in " + className);
        System.out.println("How many levels of " + className + " would you like to level up?");
        int classLevelIncrease = 0;
        while(true) {
            try {
                classLevelIncrease = inputScanner.nextInt();
                if (classLevelIncrease > 0 && playerCharacter.getTotalLevels() + classLevelIncrease <= 20) {
                    playerCharacter.levelUpCharacter(className, classLevelIncrease);
                    System.out.println(playerCharacter.getName() + " levelled up by " + classLevelIncrease);
                    break;
                } else if (classLevelIncrease == 0) {
                    System.out.println(playerCharacter.getName() + " did not level up.");
                }
                System.out.println("Sorry, " + classLevelIncrease + " is an invalid level. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                inputScanner.nextLine();
            }
        }

        return playerCharacter;
    }

    public void savePlayerCharacter(PlayerCharacter playerCharacter) throws IOException {
        File file = new File(channelDataDirectory);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        PCJSONSkeleton pcjsonSkeleton = playerCharacter.createPCJSONSkeleton();
        jsonCharacterWriter.addCharacterToFile(file, rootNode, pcjsonSkeleton);
    }

    public ArrayList<PCJSONSkeleton> loadSavedPlayerCharacters() throws IOException {
        ArrayList<PCJSONSkeleton> pcjsonSkeletons = new ArrayList<>();
        File file = new File(channelDataDirectory);
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<JsonNode> characterNodes = jsonCharacterWriter.getCharacterNodes(file);
        for(JsonNode characterNode : characterNodes) {
            pcjsonSkeletons.add(objectMapper.readValue(characterNode.toPrettyString(), PCJSONSkeleton.class));
            System.out.println(characterNode.toPrettyString());
        }

        return pcjsonSkeletons;
    }
}