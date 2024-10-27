package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager instance = new StatisticAdvertisementManager();
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();

    private StatisticAdvertisementManager() {}

    public List<Advertisement> getVideoSet(boolean isActive) {
        List<Advertisement> adData = new ArrayList<>();
        for (Advertisement ad : storage.list()) {
            if (isActive && ad.getHits() > 0) adData.add(ad);
            if (!isActive && ad.getHits() == 0) adData.add(ad);
        }
        return adData;
    }

    public static StatisticAdvertisementManager getInstance() {
//        if (instance == null) { instance = new StatisticAdvertisementManager(); }
        return instance;
    }
}
