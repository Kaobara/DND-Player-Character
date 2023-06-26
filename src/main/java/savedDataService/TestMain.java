package savedDataService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {
    // This class is primarily used to test searches
    public static void main(String[] args) throws IOException {
        TimeController timeController = new TimeController();
        System.out.println(timeController.setChannelTime(1071815785107951697L, "!Droop Clock currently 9hours, 93min, 84sec"));
        System.out.println(timeController.getChannelTime(1071815785107951697L));
        System.out.println(timeController.setChannelTime(1071815785107951697L, "!Droop Clock currently 4min, 12hours"));
        System.out.println(timeController.forwardChannelTime(1071815785107951697L, "!Droop Clock currently 2 minutes"));


//        jsonWriter.getChannelTime(1071815785107951697L);
    }
}

