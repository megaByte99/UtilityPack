package it.pack.utility.timer;

import java.util.concurrent.TimeUnit;

public class Chronometer {

    // The start time and the
    private long start, elapsed;

    public Chronometer() { /* ... */ }

    public void start() {
        start = System.currentTimeMillis();
    }

    public Chronometer stop() {
        elapsed = System.currentTimeMillis() - start;
        return this;
    }

    public String getFormattedTime() {
        long    hours   = TimeUnit.MILLISECONDS.toHours(this.elapsed),
                minutes = TimeUnit.MILLISECONDS.toMinutes(this.elapsed) % TimeUnit.HOURS.toMinutes(1),
                seconds = TimeUnit.MILLISECONDS.toSeconds(this.elapsed) % TimeUnit.MINUTES.toSeconds(1);

        return "%02d:%02d:%02d".formatted(hours, minutes, seconds);
    }
}
