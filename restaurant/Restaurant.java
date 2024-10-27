package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Dish;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.Waiter;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private static final int ORDER_CREATING_INTERVAL = 100;
    private static final LinkedBlockingQueue<Order> ORDER_QUEUE = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        DirectorTablet directorTablet = new DirectorTablet();
        Cook cook1 = new Cook("Amigo");
        Cook cook2 = new Cook("Andrey");
        cook1.setQueue(ORDER_QUEUE);
        cook2.setQueue(ORDER_QUEUE);

        List<Tablet> tablets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tablets.add(new Tablet(i + 1));
            tablets.get(tablets.size() - 1).setQueue(ORDER_QUEUE);
        }

        Waiter waiter = new Waiter();
        cook1.addObserver(waiter);
        cook2.addObserver(waiter);

        Thread cook1Thread = new Thread(cook1); cook1Thread.setDaemon(true); cook1Thread.start();
        Thread cook2Thread = new Thread(cook2); cook2Thread.setDaemon(true); cook2Thread.start();

        Thread generator = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        generator.start();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        generator.interrupt();

//        directorTablet.printAdvertisementProfit();
//        directorTablet.printActiveVideoSet();
//        directorTablet.printArchivedVideoSet();
//        directorTablet.printCookWorkloading();
    }
}
