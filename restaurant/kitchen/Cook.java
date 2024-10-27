package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private String name;
    private boolean busy = false;
    private LinkedBlockingQueue<Order> queue;

    public Cook(String name) {
        this.name = name;
    }

    public void setQueue(LinkedBlockingQueue queue) { this.queue = queue; }

    @Override
    public String toString() {
        return name;
    }

    public void startCookingOrder(Order order) {
        busy = true;
        ConsoleHelper.writeMessage("Start cooking - " + order);
        try { Thread.sleep(order.getTotalCookingTime() * 10); } catch (InterruptedException e) { e.printStackTrace(); }
        StatisticManager.getInstance().register(new CookedOrderEventDataRow(
                order.getTablet().toString(),
                name,
                order.getTotalCookingTime() * 60,
                order.dishes));
        setChanged();
        notifyObservers(order);
        busy = false;
    }

    public boolean isBusy() { return busy; }

    @Override
    public void run() {
        while (true) {
            try { Thread.sleep(10); } catch (InterruptedException e) {e.printStackTrace();}
            if (!queue.isEmpty() && !busy) {
                try { startCookingOrder(queue.take()); } catch (InterruptedException e) {e.printStackTrace();}
            }
        }
    }
}
