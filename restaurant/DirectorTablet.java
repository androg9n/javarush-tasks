package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.SimpleDateFormat;
import java.util.*;

public class DirectorTablet {

    public void printAdvertisementProfit() {
        List<EventDataRow> events = StatisticManager.getInstance().getAdData();
        events.sort(new Comparator<EventDataRow>() {
            @Override
            public int compare(EventDataRow o1, EventDataRow o2) {
                return (int) (o2.getDate().getTime() - o1.getDate().getTime());
            }
        });
        long amountOfDay = 0;
        long amountTotal = 0;
        Calendar calendarOfDay = null;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        for (EventDataRow event : events) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(event.getDate());
            if (calendarOfDay == null) calendarOfDay = calendar;
            if (calendar.get(Calendar.DATE) != calendarOfDay.get(Calendar.DATE)) {
                ConsoleHelper.writeMessage(String.format("%s - %.2f",
                        df.format(calendarOfDay.getTime()), ((float) amountOfDay) / 100));
                calendarOfDay = calendar;
                amountOfDay = 0;
            }
            amountOfDay += ((VideoSelectedEventDataRow)event).getAmount();
            amountTotal += ((VideoSelectedEventDataRow)event).getAmount();
        }
        ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "%s - %.2f",
                df.format(calendarOfDay.getTime()), ((float) amountOfDay) / 100));
        ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "Total - %.2f", ((float) amountTotal) / 100));
    };

    public void printCookWorkloading() {
        List<EventDataRow> events = StatisticManager.getInstance().getCookData();
        events.sort(new Comparator<EventDataRow>() {
            @Override
            public int compare(EventDataRow o1, EventDataRow o2) {
                return (int) (o2.getDate().getTime() - o1.getDate().getTime());
            }
        });
        Map<String, Integer> cooks = new TreeMap<>();
        Calendar calendarOfDay = null;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        for (EventDataRow event : events) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(event.getDate());
            if (calendarOfDay == null) calendarOfDay = calendar;
            if (calendar.get(Calendar.DATE) != calendarOfDay.get(Calendar.DATE)) {
                ConsoleHelper.writeMessage(df.format(calendarOfDay.getTime()));
                for (Map.Entry<String, Integer> entry : cooks.entrySet()) {
                    ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "%s - %d min",
                            entry.getKey(),
                            (int)Math.ceil(((double)entry.getValue()) / 60 )));
                    ConsoleHelper.writeMessage("");
                }
                calendarOfDay = calendar;
                cooks = new TreeMap<>();
            }
            String cookName = ((CookedOrderEventDataRow)event).getCookName();
            int cookingTimeSeconds = ((CookedOrderEventDataRow)event).getTime();
            if (!cooks.containsKey(cookName)) cooks.put(cookName, 0);
            cooks.replace(cookName, cooks.get(cookName) + cookingTimeSeconds);
        }
        ConsoleHelper.writeMessage(df.format(calendarOfDay.getTime()));
        for (Map.Entry<String, Integer> entry : cooks.entrySet()) {
            ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "%s - %d min",
                    entry.getKey(),
                    (int)Math.ceil(((double)entry.getValue()) / 60 )));
        }
    };

    public void printActiveVideoSet() {
        List<Advertisement> ads = StatisticAdvertisementManager.getInstance().getVideoSet(true);
        Collections.sort(ads, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        for (Advertisement ad : ads) {
            System.out.println(ad.getName() + " - " + ad.getHits());
        }

//            ConsoleHelper.writeMessage(String.format("%s - %d", ad.getName(), ad.getHits()));
    };

    public void printArchivedVideoSet() {
        List<Advertisement> ads = StatisticAdvertisementManager.getInstance().getVideoSet(false);
        ads.sort(new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                String s1 = o1.getName();
                String s2 = o2.getName();
                return s1.compareToIgnoreCase(s2);
            }
        });
        for (Advertisement ad : ads)
            ConsoleHelper.writeMessage(ad.getName());
    };

}
