package com.gymmaster;

public class TimeOfDay {
    private final int hours;
    private final int minutes;

    public TimeOfDay(int hours, int minutes) {
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid time: " + hours + ":" + minutes);
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeOfDay)) return false;
        TimeOfDay that = (TimeOfDay) o;
        return hours == that.hours && minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return 31 * hours + minutes;
    }
}
