package com.javarush.task.task27.task2712.ad;

import java.util.*;

public class AdvertisementStorage {
    private static AdvertisementStorage instance;
    private final List<Advertisement> videos = new ArrayList<>();

    private AdvertisementStorage(){
        Object someContent = new Object();
        add(new Advertisement(someContent, "Первое видео", 200, 5, 11 * 60)); //11 min
        add(new Advertisement(someContent, "второе видео", 150, 8, 12 * 60)); //12 min
        add(new Advertisement(someContent, "second Video", 100, 10, 15 * 60)); //15 min
        add(new Advertisement(someContent, "Third Video", 400, 2, 10 * 60)); //10 min
        add(new Advertisement(someContent, "First Video", 5000, 150, 3 * 60)); // 3 min
    };

    public static AdvertisementStorage getInstance() {
        if (instance == null) { instance = new AdvertisementStorage(); }
        return instance;
    }

    public List<Advertisement> list() {
        return videos;
//        List<Advertisement> result = new ArrayList<>(videos);
//        Collections.reverse(result);
//        return result;
    }

    public void add(Advertisement advertisement) { videos.add(advertisement); }

}
