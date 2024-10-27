package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        System.out.printf("Список всех блюд: %s%n", Dish.allDishesToString());
        String s;
        List<Dish> result = new ArrayList<>();
        while (true) {
            System.out.print("Добавьте блюдо в заказ (exit - выход): ");
            s = readString();
            if (s.equals("exit")) break;
            try {
                result.add(Dish.valueOf(s));
            } catch (IllegalArgumentException e) {
                writeMessage("Такого блюда нет");
            }
        }
        return result;
    }

}
