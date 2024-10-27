package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class TestOrder extends Order {

    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() {
        SecureRandom random = new SecureRandom();
        dishes = new ArrayList<>();
        for (Dish d : Dish.values()) if (random.nextBoolean()) dishes.add(d);
    }
}
