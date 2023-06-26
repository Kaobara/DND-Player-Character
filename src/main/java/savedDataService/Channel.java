package savedDataService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.math.BigInteger;

public class Channel {
    long id;
    @JsonSetter("current Time")
    Clock clock = new Clock();

    public Channel(long id) { this.id = id; }
    @JsonCreator
    public Channel(@JsonProperty("id")long id, @JsonProperty("clock")Clock clock) { this.id = id; this.clock = clock; }
    public Channel(long id, int hr, int min, int sec) { this.id = id; this.clock = new Clock(hr, min, sec); }

//    public String getClock() { return clock.getCurrentTime(); }
    public Clock getClock() { return clock; }
    public Clock uniqueGetClock() { return clock; }
    public void uniqueSetClock(Clock clock) { this.clock = clock; }

    public long getId() {
        return id;
    }
}
