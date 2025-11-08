package com.gymmaster;

public class CounterOfTrainings {
    private final Coach coach;
    private final int count;

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() { return coach; }
    public int getCount() { return count; }

    @Override
    public String toString() {
        return coach.getLastN() + " " +
                (coach.getFirstN().isEmpty() ? "" : coach.getFirstN().charAt(0) + ".") +
                (coach.getMiddleN().isEmpty() ? "" : coach.getMiddleN().charAt(0) + ".") +
                " — " + count + " занятий";
    }
}