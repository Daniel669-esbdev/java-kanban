package com.gymmaster;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> schedule;

    public Timetable() {
        schedule = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new TreeMap<>(new TimeOfDayComparator()));
        }
    }

    public void addNewTrainingSession(TrainingSession session) {
        DayOfWeek day = session.getDayOfWeek();
        TimeOfDay time = session.getTimeOfDay();
        schedule.get(day)
                .computeIfAbsent(time, k -> new ArrayList<>())
                .add(session);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> list : schedule.get(dayOfWeek).values()) {
            result.addAll(list);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        List<TrainingSession> list = schedule.get(dayOfWeek).get(timeOfDay);
        return list != null ? new ArrayList<>(list) : Collections.emptyList();
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countMap = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : schedule.values()) {
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession s : sessions) {
                    countMap.merge(s.getCoach(), 1, Integer::sum);
                }
            }
        }
        List<CounterOfTrainings> result = new ArrayList<>();
        countMap.forEach((coach, count) -> result.add(new CounterOfTrainings(coach, count)));
        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        return result;
    }

    private static class TimeOfDayComparator implements Comparator<TimeOfDay> {
        @Override
        public int compare(TimeOfDay t1, TimeOfDay t2) {
            int h = Integer.compare(t1.getHours(), t2.getHours());
            return h != 0 ? h : Integer.compare(t1.getMinutes(), t2.getMinutes());
        }
    }
}