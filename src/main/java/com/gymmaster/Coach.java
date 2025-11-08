package com.gymmaster;

public class Coach {
    private final String lastN;
    private final String firstN;
    private final String middleN;

    public Coach(String lastN, String firstN, String middleN) {
        this.lastN = lastN;
        this.firstN = firstN;
        this.middleN = middleN;
    }

    public String getLastN() { return lastN; }
    public String getFirstN() { return firstN; }
    public String getMiddleN() { return middleN; }

    public String getFullName() {
        return lastN + " " +
                (firstN.isEmpty() ? "" : firstN.charAt(0) + ".") +
                (middleN.isEmpty() ? "" : middleN.charAt(0) + ".");
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coach)) return false;
        Coach coach = (Coach) o;
        return lastN.equals(coach.lastN) &&
                firstN.equals(coach.firstN) &&
                middleN.equals(coach.middleN);
    }

    @Override
    public int hashCode() {
        return 31 * (31 * lastN.hashCode() + firstN.hashCode()) + middleN.hashCode();
    }
}