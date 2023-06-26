package savedDataService;

public class Clock {
    private int hr = 0;
    private int min = 0;
    private int sec = 0;

    public Clock() {
    }

    public Clock(int hr, int min, int sec) {
        this.hr = hr;
        this.min = min;
        this.sec = sec;
    }
    // Note: moved time conversion from the start of process (adding and setting) to end of processing (when time is called)
    // convert time only needs to be called at the end instead of constantly through the process
    public void addSeconds(int seconds) {
        sec += seconds;
//        convertTime();
    }

    public void addMinutes(int minutes) {
        min += minutes;
//        convertTime();
    }

    public void addHr(int hr) {
        this.hr += hr;
//        convertTime();
    }

    // TODO Make sec-min-hr into a single method to remove duplicates
    private void convertSecMin() {
        if(sec < 60) {
            return;
        }
        min += sec/60;
        sec = sec%60;
        return;
    }

    private void convertMinHr() {
        if(min < 60) {
            return;
        }
        hr += min/60;
        min = min%60;
        return;
    }

    private void convertHr() {
        if(hr < 24) {
            return;
        }
        hr = hr%24;
    }

    public void convertTime() {
        convertSecMin();
        convertMinHr();
        convertHr();
    }

    public int getSec() { convertTime(); return sec; }
    public int getMin() { convertTime(); return min; }
    public int getHr() { convertTime(); return hr; }

    public void setHr(int Hr) {
        this.hr = Hr;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public String stringGetCurrentTime() {
        return String.format("%02d:%02d:%02d", hr, min,sec);
    }
}
