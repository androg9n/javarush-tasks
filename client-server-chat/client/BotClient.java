package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client {

    protected String getUserName() {
        int x = (int) (Math.random() * 100);
        return "date_bot_" + x;
    }

    protected boolean shouldSendTextFromConsole() { return false; }

    protected SocketThread getSocketThread() { return new BotSocketThread(); }

    public class BotSocketThread extends SocketThread {
        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            String[] parts = message.split(": ");
            if (parts.length < 2) return;
            String name = parts[0];
            String text = parts[1];
            DateFormat df = null;
            switch (text) {
                case "дата":
                    df = new SimpleDateFormat("d.MM.YYYY");
                    break;
                case "день":
                    df = new SimpleDateFormat("d");
                    break;
                case "месяц":
                    df = new SimpleDateFormat("MMMM");
                    break;
                case "год":
                    df = new SimpleDateFormat("YYYY");
                    break;
                case "время":
                    df = new SimpleDateFormat("H:mm:ss");
                    break;
                case "час":
                    df = new SimpleDateFormat("H");
                    break;
                case "минуты":
                    df = new SimpleDateFormat("m");
                    break;
                case "секунды":
                    df = new SimpleDateFormat("s");
                    break;
            }
            if (df == null) return;
            String answer = df.format(Calendar.getInstance().getTime());
            sendTextMessage(String.format("Информация для %s: %s", name, answer));
        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }
    }

    public static void main(String[] args) { new BotClient().run(); }
}
