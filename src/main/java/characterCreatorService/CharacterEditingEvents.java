package characterCreatorService;

import org.apache.commons.text.WordUtils;
import staticScrapeService.ItemSearch;
import staticScrapeService.SpellSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CharacterEditingEvents {
    private final BaseClassInfo baseClassInfo = new BaseClassInfo();
    private final Scanner inputScanner = new Scanner(System.in);
    private final CharacterCreationSaveFileEvents PCSaveFileEvents = new CharacterCreationSaveFileEvents();
    private final SpellSearch spellSearch = new SpellSearch();
    private final ItemSearch itemSearch = new ItemSearch();

    // Edit a saved character based on options
    public PlayerCharacter editSavedCharacter(PlayerCharacter playerCharacter) throws IOException {
        int editOption;
        boolean saveOption = false;
        while(true) {
            editOption = chooseEditOption(playerCharacter);
            switch (editOption) {
                case 1:
                    // Edit Name
                    playerCharacter = editPCName(playerCharacter);
                    saveOption = true;
                    break;
                case 2:
                    // Print Race Features
                    playerCharacter.printRaceFeatures();
                    break;
                case 3:
                    // Edit Race
                    playerCharacter = editPCRace(playerCharacter);
                    saveOption = true;
                    break;
                case 4:
                    // Print class features
                    playerCharacter.printClassFeatures();
                    break;
                case 5:
                    // Level up a class
                    String classToLevelUp = chooseLevelUpClass(playerCharacter);
                    playerCharacter = levelUpClass(playerCharacter, classToLevelUp);
                    saveOption = true;
                    break;
                case 6:
                    // Add a new class
                    playerCharacter = buildBaseClass(playerCharacter);
                    saveOption = true;
                    break;
                case 7:
                    // See spell descriptions
                    playerCharacter.printSpellDescriptions();
                    break;
                case 8:
                    // Add spell to character
                    playerCharacter = addSpellToCharacter(playerCharacter);
                    saveOption = true;
                    break;
                case 9:
                    // See item descriptions
                    playerCharacter.printItemDescrptions();
                    break;
                case 10:
                    // Add item to character
                    addItemToCharacter(playerCharacter);
                    saveOption = true;
                    break;
            }

            if(editOption == 0) break;
        }
        if(saveOption) {
            inputScanner.nextLine();
            savePlayerCharacterEvent(playerCharacter);
        }

        return playerCharacter;
    }

    // Command line method for choosing an option to edit a character
    private int chooseEditOption(PlayerCharacter playerCharacter) {
        int editOption;
        while(true) {
            playerCharacter.printCharacterDescriptionSummary();
            System.out.println("How would you like to edit " + playerCharacter.getName() + "? Please choose from " +
                    "the following options:\n" +
                    "(0) Stop Editing\n" +
                    "(1) Change Name\n" +
                    "(2) See Race Feature\n" +
                    "(3) Change Race\n" +
                    "(4) See Class Features\n" +
                    "(5) Level up a Class\n" +
                    "(6) Add a new Class\n" +
                    "(7) See Spells\n" +
                    "(8) Add a Spell\n" +
                    "(9) See Magic Items\n" +
                    "(10) Add a Magic item");
            try {
                editOption = inputScanner.nextInt();
                if (editOption > -1 && editOption < 13 ) {
                    break;
                }
                System.out.println("Sorry, that was an invalid option. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                inputScanner.nextLine();
            }
        }
        return editOption;
    }

    // Command line event to edit the name of a player character
    public PlayerCharacter editPCName(PlayerCharacter playerCharacter) {
        System.out.println("The character's name is currently " + playerCharacter.getName() + "\nPlease enter a new name: ");
        inputScanner.nextLine();
        String newName = inputScanner.nextLine();
        System.out.println("Named changed from " + playerCharacter.getName() + " to " + newName);
        playerCharacter.setName(newName);
        return playerCharacter;
    }

    // Command line event to build a race
    public Race buildRace() {
        System.out.println("Please choose a race. The available races are: ");
        Race race = new Race();
        race.printRaceList();
        String raceName;
        while(true) {
            raceName = inputScanner.nextLine();
            if(race.checkRace(raceName)) break;
            System.out.println("Sorry, the race " + raceName + " is unavailable. Please try again.");
        }

        System.out.println("You chose the race: " + raceName);
        race = new Race(raceName);
        return race;
    }

    // Command line event to change the race of a PC
    public PlayerCharacter editPCRace(PlayerCharacter playerCharacter) {
        System.out.println(playerCharacter.getName() + "'s race is currently " + playerCharacter.getRaceName());
        inputScanner.nextLine();
        Race newRace = buildRace();
        System.out.println("Race changed from " + playerCharacter.getRaceName() + " to " + newRace.getRaceName());
        playerCharacter.setRace(newRace);
        return playerCharacter;
    }

    // Command line event to build a player character class
    public PlayerCharacter buildBaseClass(PlayerCharacter playerCharacter) {
        System.out.println("Please choose a Class. The available classes are: ");
        baseClassInfo.printClassList();
        String className;
        while(true) {
            className = inputScanner.nextLine();
            if(baseClassInfo.checkClass(className)) break;
            System.out.println("Sorry, the class " + className + " is unavailable. Please try again.");
        }
        className = WordUtils.capitalizeFully(className);
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
        int classLevel;
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
        playerCharacter.giveCharacterBaseClass(new BaseClass(className, classLevel));

        inputScanner.nextLine();

        return playerCharacter;
    }

    // Command line event for choosing a class to level up
    public String chooseLevelUpClass(PlayerCharacter playerCharacter) {
        System.out.println(playerCharacter.getName() + " currently has levels in: ");
        ArrayList<String> classNames = playerCharacter.getClassNames();
        for(String className : classNames) {
            System.out.print(className + " ");
        }
        String classToLevelUp;
        System.out.print("\nPlease choose a class to level up in.\n");
        inputScanner.nextLine();
        while(true) {
            classToLevelUp = inputScanner.nextLine();
            classToLevelUp = WordUtils.capitalizeFully(classToLevelUp);
            if(classNames.contains(classToLevelUp)) {
                System.out.println("Leveling up in " + classToLevelUp);
                break;
            }
            System.out.println("That is not a valid class. Please try again.");
        }

        return classToLevelUp;
    }

    // Command line event to level up a class
    public PlayerCharacter levelUpClass(PlayerCharacter playerCharacter, String className) {
        System.out.println(playerCharacter.getName() + " is currently level " + playerCharacter.getTotalLevels()
                + " and has " + playerCharacter.getLevelInClass(className) + " in " + className);
        System.out.println("How many levels of " + className + " would you like to level up?");
        int classLevelIncrease;
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

    // Command line event to add a spell to a character
    private PlayerCharacter addSpellToCharacter(PlayerCharacter playerCharacter) {
        while(true) {
            System.out.println("What spell would you like to add? Some spells include:" +
                    "\n- Eldritch Blast\n- Fire Bolt\n- Bless\n- Vicious Mockery\n- Fireball" +
                    "\n- Mage Hand\n- Speak with Animals\n- etc.");
            inputScanner.nextLine();
            String spellName = inputScanner.nextLine();
            spellName = spellName.toLowerCase();
            spellName = WordUtils.capitalizeFully(spellName);

            if(spellSearch.getSpellList().contains(spellName)) {
                playerCharacter.addSpell(spellName);
                break;
            } else {
                System.out.println("Invalid Input. Please try again");
            }
        }
        return playerCharacter;
    }

    // Command line event to add an item to a player character
    private PlayerCharacter addItemToCharacter(PlayerCharacter playerCharacter) {
        while(true) {
            System.out.println("What magic item would you like to add? Some items include:" +
                    "\n- Potion of Healing\n- Bead of Nourishment\n- Masquerade Tattoo\n- Cloak of Elvenkind\n- Bag of Holding" +
                    "\n- Potion of Invisibility\n- Moonblade\n -etc.");
            inputScanner.nextLine();
            String itemName = inputScanner.nextLine();
            itemName = itemName.toLowerCase();
            itemName = WordUtils.capitalizeFully(itemName);

            if(itemSearch.getItemList().contains(itemName)) {
                playerCharacter.addItem(itemName);
                break;
            } else {
                System.out.println("Invalid Input. Please try again");
            }
        }
        return playerCharacter;
    }

    // Command line event to save a player character to the save file
    public void savePlayerCharacterEvent(PlayerCharacter playerCharacter) throws IOException {
        while(true) {
            System.out.println("Would you like to save changes? (Y/N)");
            String multiclassBool = inputScanner.nextLine();
            if(multiclassBool.equalsIgnoreCase("N")) {
                System.out.println("Changes discarded");
                break;
            } else if (multiclassBool.equalsIgnoreCase("Y")) {
                PCSaveFileEvents.savePlayerCharacter(playerCharacter);
                System.out.println(playerCharacter.getName() + " saved.");
                break;
            } else {
                System.out.println("Invalid Input. Please try again");
            }
        }
    }

    // Command line event to fully load up a player character
    public ArrayList<PCJSONSkeleton> loadSavedPlayerCharacters() throws IOException {
        return PCSaveFileEvents.loadSavedPlayerCharacters();
    }


}
