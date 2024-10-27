package com.javarush.task.task27.task2712.kitchen;

public enum Dish {
//    FISH, STEAK, SOUP, JUICE, WATER;
    FISH(25), STEAK(30), SOUP(15), JUICE(5), WATER(3);

    private int duration;

    Dish(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public static String allDishesToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Dish dish : Dish.values()) stringBuilder.append(dish.toString() + ", ");
        if (stringBuilder.length() > 1) stringBuilder.setLength(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }
}
