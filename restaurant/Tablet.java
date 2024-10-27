package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet {
    private static List<Tablet> tablets = new ArrayList<>();
    private final int number;
    private LinkedBlockingQueue<Order> queue;
    public static Logger logger = Logger.getLogger(Tablet.class.getName());

    public Tablet(int number) {
        this.number = number;
        tablets.add(this);
    }

    public void setQueue(LinkedBlockingQueue queue) { this.queue = queue; }

    public Order createOrder() {
        try {
            Order order = new Order(this);
            extCreateOrder(order);
            return order;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        }
        return null;
    }

    public void createTestOrder() {
        try {
            Order order = new TestOrder(this);
            extCreateOrder(order);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        }
    }

    private void extCreateOrder(Order order) {
        ConsoleHelper.writeMessage(order.toString());
        if (!order.isEmpty()) {
            try {
                new AdvertisementManager(order.getTotalCookingTime() * 60).processVideos();
            } catch (NoVideoAvailableException e) {
                logger.log(Level.INFO, "No video is available for the order " + order);
            }
            try { queue.put(order); } catch (InterruptedException e) {e.printStackTrace();}

//            setChanged();
//            notifyObservers(order);
        }
    }

    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
