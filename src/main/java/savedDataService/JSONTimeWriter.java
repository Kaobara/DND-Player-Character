package savedDataService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public class JSONTimeWriter {
    final String channelDataDirectory = "DataStorage/channel.json";

    public JsonNode channelExists(File file, Long channelID) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);

        JsonNode finalNode = null;

        for(JsonNode channelNode : rootNode) {
            if(channelNode.get("id").longValue() == channelID) {
                finalNode = channelNode;
            }
        }

        return finalNode;
    }

    public void addChannelToFile(File file, JsonNode rootNode, Channel newChannel) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode editedNode = rootNode.deepCopy();
        editedNode.addPOJO(newChannel);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }
    public void removeChannelFromFile(File file, JsonNode rootNode, Channel newChannel) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode editedNode = rootNode.deepCopy();
        for(int i = 0; i <editedNode.size(); i++) {
            if(editedNode.get(i).get("id").asLong() == newChannel.getId()) {
                editedNode.remove(i);
                System.out.println("REMOVE SUCCESSFUL");
                break;
            }
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }

    public String getChannelTime(Long channelID) throws IOException {
        File file = new File(channelDataDirectory);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

//        System.out.println("Channel ID: " + channelID);

        JsonNode channelNode = channelExists(file, channelID);

        if(channelNode == null){
            Channel newChannel = new Channel(channelID);
            addChannelToFile(file, rootNode, newChannel);
            return "New Channel Detected. Initializing with time 00:00:00";
        }
        assert  channelNode != null;

        return getClockFromNode(channelNode).stringGetCurrentTime();

    }

    public Clock getClockFromNode(JsonNode node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode timeClock = node.get("clock");
//
//        System.out.println(node);
        // TODO figure out how to directly convert to channel class
        Clock clock1 = mapper.readValue(timeClock.toString(), Clock.class);

        return clock1;
    }

    public Clock addTime(Clock clock, int hr, int min, int sec) {
        clock.addSeconds(sec);
        clock.addMinutes(min);
        clock.addHr(hr);
        return clock;
    }

    public Clock setTime(Clock clock, int hr, int min, int sec) {
        clock.setSec(sec);
        clock.setMin(min);
        clock.setHr(hr);
        return clock;
    }

    // note: in the file is the old channel, its data is being replaced with the new channel
    public void editChannelFromFile(File file, JsonNode rootNode, Channel newChannel) throws IOException {
        // Remove old channel data from file
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode editedNode = rootNode.deepCopy();
        for(int i = 0; i <editedNode.size(); i++) {
            if(editedNode.get(i).get("id").asLong() == newChannel.getId()) {
                editedNode.remove(i);
                System.out.println("REMOVE SUCCESSFUL");
                break;
            }
        }

        editedNode.addPOJO(newChannel);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, editedNode);
    }

    public void editChannelTime(long channelID,int hr, int min, int sec, String editType) throws IOException {
        File file = new File(channelDataDirectory);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        JsonNode channelNode = channelExists(file, channelID);

        Clock clock = new Clock();

        if(channelNode != null) {
            if (editType.startsWith("add")) {
                clock = addTime(getClockFromNode(channelNode), hr, min, sec);
            } else if (editType.startsWith("set")) {
                clock = setTime(getClockFromNode(channelNode), hr, min, sec);
            }
        } else {
            clock = new Clock();
            System.out.println("New Channel Detected. Initializing with time 00:00:00");
        }
        assert  channelNode != null;

        Channel newChannel = new Channel(channelID, clock);
        System.out.println("channelID: " + channelID);
        System.out.println("new Time: " + clock.stringGetCurrentTime());

        editChannelFromFile(file, rootNode, newChannel);

        return;


    }
}
