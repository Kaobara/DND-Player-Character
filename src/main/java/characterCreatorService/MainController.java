package characterCreatorService;

import com.fasterxml.jackson.databind.ser.Serializers;
import staticScrapeService.ClassFeatureSearch;
import staticScrapeService.RaceFeaturesSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainController {
    public String characterName;

    public static void main(String args[] ) {
        Race race = new Race();
        BaseClassInfo baseClassInfo = new BaseClassInfo();
        RaceFeaturesSearch raceFeaturesSearch = new RaceFeaturesSearch();
        Scanner in = new Scanner(System.in);

        System.out.println("Please choose a name");
        String name = in.nextLine();
        System.out.println("You chose the name: " + name);

        System.out.println("Please choose a race. The available races are: ");
        race.printRaceList();
        String raceName = in.nextLine();
        System.out.println("You chose the race: " + raceName);

        System.out.println("Please choose a Class. The available classes are: ");
        baseClassInfo.printClassList();
        String className;
        while(true) {
            className = in.nextLine();
            if(baseClassInfo.checkClass(className)) {
                break;
            }
            System.out.println("Sorry, the class " + className + " is unavailable. Please try again.");
        }
        System.out.println("You chose the class: " + className);

        System.out.println("How many " + className + " levels does " + name + " have? Maximum of 20 levels.");
        int classLevel = -1;
        while(true) {
            try {
                classLevel = in.nextInt();
                if (classLevel > 0 && classLevel <= 20) {
                    break;
                }
                System.out.println("Sorry, " + classLevel + " is an invalid level. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                in.nextLine();
            }
        }

        System.out.println(name + " is a level " + classLevel + " " + className);

        BaseClass baseClass = new BaseClass(className, classLevel);




        return;
    }
}
