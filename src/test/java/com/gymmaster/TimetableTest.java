package com.gymmaster;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable t = new Timetable();
        Group g = new Group("РђРєСЂРѕР±Р°С‚РёРєР° РґР»СЏ РґРµС‚РµР№", Age.CHILD, 60);
        Coach c = new Coach("Р’Р°СЃРёР»СЊРµРІ", "РќРёРєРѕР»Р°Р№", "РЎРµСЂРіРµРµРІРёС‡");
        TrainingSession s = new TrainingSession(g, c, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        t.addNewTrainingSession(s);

        List<TrainingSession> mon = t.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tue = t.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, mon.size());
        assertEquals(s, mon.get(0));
        assertTrue(tue.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable t = new Timetable();
        Coach c = new Coach("Р’Р°СЃРёР»СЊРµРІ", "РќРёРєРѕР»Р°Р№", "РЎРµСЂРіРµРµРІРёС‡");
        Group adult = new Group("РђРєСЂРѕР±Р°С‚РёРєР° РґР»СЏ РІР·СЂРѕСЃР»С‹С…", Age.ADULT, 90);
        Group child = new Group("РђРєСЂРѕР±Р°С‚РёРєР° РґР»СЏ РґРµС‚РµР№", Age.CHILD, 60);

        t. addNewTrainingSession(new TrainingSession(adult, c, DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        t. addNewTrainingSession(new TrainingSession(child, c, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        t. addNewTrainingSession(new TrainingSession(child, c, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)));
        t. addNewTrainingSession(new TrainingSession(child, c, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        List<TrainingSession> mon = t.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> thu = t.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        assertEquals(1, mon.size());
        assertEquals(2, thu.size());
        assertEquals(13, thu.get(0).getTimeOfDay().getHours());
        assertEquals(20, thu.get(1).getTimeOfDay().getHours());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable t = new Timetable();
        Group g = new Group("РђРєСЂРѕР±Р°С‚РёРєР° РґР»СЏ РґРµС‚РµР№", Age.CHILD, 60);
        Coach c = new Coach("Р’Р°СЃРёР»СЊРµРІ", "РќРёРєРѕР»Р°Р№", "РЎРµСЂРіРµРµРІРёС‡");
        TrainingSession s = new TrainingSession(g, c, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        t.addNewTrainingSession(s);

        assertEquals(1, t.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        assertTrue(t.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).isEmpty());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable t = new Timetable();
        Coach c1 = new Coach("РРІР°РЅРѕРІ", "Рђ.", "Р‘.");
        Coach c2 = new Coach("РџРµС‚СЂРѕРІ", "Р’.", "Р“.");
        Group g1 = new Group("Р™РѕРіР°", Age.ADULT, 60);
        Group g2 = new Group("РџРёР»Р°С‚РµСЃ", Age.ADULT, 60);

        t.addNewTrainingSession(new TrainingSession(g1, c1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        t.addNewTrainingSession(new TrainingSession(g2, c2, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));

        assertEquals(2, t.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0)).size());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable t = new Timetable();
        Coach c1 = new Coach("РЎРµРјС‘РЅРѕРІ", "Р’.", "Рљ.");
        Coach c2 = new Coach("РЁРјРѕС‚РєРѕРІ", "Р’.", "Р’.");
        Group g = new Group("РђРєСЂРѕР±Р°С‚РёРєР°", Age.CHILD, 60);

        t.addNewTrainingSession(new TrainingSession(g, c1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        t.addNewTrainingSession(new TrainingSession(g, c1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        t.addNewTrainingSession(new TrainingSession(g, c1, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        t.addNewTrainingSession(new TrainingSession(g, c2, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        t.addNewTrainingSession(new TrainingSession(g, c2, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> list = t.getCountByCoaches();
        assertEquals(3, list.get(0).getCount());
        assertEquals("РЎРµРјС‘РЅРѕРІ", list.get(0).getCoach().getLastN());
        assertEquals(2, list.get(1).getCount());
    }

    @Test
    void testGetCountByCoachesEmpty() {
        Timetable t = new Timetable();
        assertTrue(t.getCountByCoaches().isEmpty());
    }
}