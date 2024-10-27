package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.List;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        initDishes();
    }

    protected void initDishes() throws IOException {
        dishes = ConsoleHelper.getAllDishesForOrder();
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public Tablet getTablet() {
        return tablet;
    }

    public int getTotalCookingTime() {
        int result = 0;
        for (Dish dish : dishes) result += dish.getDuration();
        return result;
    }

    @Override
    public String toString() {
        if (dishes.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dishes.size(); i++) {
            sb.append(dishes.get(i));
            if (i + 1 < dishes.size()) sb.append(", ");
        }
        return String.format("Your order: [%s] of %s, cooking time %dmin",
                sb.toString(),
                tablet.toString(),
                getTotalCookingTime());
    }

    public boolean isEmpty() {return dishes.isEmpty();}
}
