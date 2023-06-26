package savedDataService;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeMessageSplitter {
    final private Pattern hourPattern = Pattern.compile("(\\d+\\s*ho*u*rs*)");
    final private Pattern minPattern = Pattern.compile("(\\d+\\s*minu*t*e*s*)");
    final private Pattern secPattern = Pattern.compile("(\\d+\\s*seco*n*d*s*)");
    final private Pattern[] timePatterns = {hourPattern, minPattern, secPattern};
    final private Pattern digitWordPattern = Pattern.compile("(\\d+\\s*\\w*)");

    public int[] getTimeValues(String input) {
        Matcher matcher = digitWordPattern.matcher(input);
        ArrayList<String> timeSets = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            if(matcher.find()) {
                timeSets.add(matcher.group());
            }
        }

        // Time value array in order of {hr; min; sec}
        int[] timeValues = new int[3];
        for(int i = 0; i<timePatterns.length; i++) {
            timeValues[i] = timeValue(timeSets, timePatterns[i]);
            System.out.println(timeValues[i]);
        }

        return timeValues;
    }

    private static int timeValue(ArrayList<String> timeSets, Pattern timePattern) {

        String timeString = "";

        for(String time:timeSets) {
            if(time.matches(timePattern.pattern())) {
                timeString = time;
                break;
            }
        }
        if(timeString.isEmpty()) {
            return 0;
        }
        return(Integer.parseInt(timeString.replaceAll("\\D*", "")));
    }
}
