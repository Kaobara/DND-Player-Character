package savedDataService;

import java.io.IOException;

public class TimeController {
    final JSONTimeWriter jsonTimeWriter = new JSONTimeWriter();
    final TimeMessageSplitter timeMessageSplitter = new TimeMessageSplitter();

    public String getChannelTime(Long channelID) throws IOException {
        return jsonTimeWriter.getChannelTime(channelID);
    }

    private int[] getTimeValues(String input) {
        return timeMessageSplitter.getTimeValues(input);
    }

    public String setChannelTime(Long channelID, String input) throws IOException {
        int[] timeValues = getTimeValues(input);
        jsonTimeWriter.editChannelTime(channelID, timeValues[0], timeValues[1], timeValues[2], "set");
        return getChannelTime(channelID);
    }

    public String forwardChannelTime(Long channelID, String input) throws IOException {
        int[] timeValues = getTimeValues(input);
        jsonTimeWriter.editChannelTime(channelID, timeValues[0], timeValues[1], timeValues[2], "add");
        return getChannelTime(channelID);
    }
}
