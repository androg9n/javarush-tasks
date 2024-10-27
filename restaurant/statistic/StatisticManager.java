package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;

import java.util.*;

public class StatisticManager {
    private static StatisticManager instance;
    private StatisticStorage statisticStorage = new StatisticStorage();
//    private Set<Cook> cooks = new HashSet<>();

    private StatisticManager() {};

    public static StatisticManager getInstance() {
        if (instance == null) instance = new StatisticManager();
        return instance;
    }

    public void register(EventDataRow data) { statisticStorage.put(data); }

//    public void register(Cook cook) { cooks.add(cook); }

    public List<EventDataRow> getAdData() { return statisticStorage.get(EventType.SELECTED_VIDEOS); }

    public List<EventDataRow> getCookData() { return statisticStorage.get(EventType.COOKED_ORDER); }

//    public Set<Cook> getCooks() { return cooks; }

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage = new HashMap<>();

        public StatisticStorage() {
            for (EventType e : EventType.values()) storage.put(e, new ArrayList<EventDataRow>());
        }

        private void put(EventDataRow data) {
            storage.get(data.getType()).add(data);
        }

        private List<EventDataRow> get(EventType eventType) { return storage.get(eventType); }
    }
}
