package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Адрес сервера:");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        ConsoleHelper.writeMessage("Порт сервера:");
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        ConsoleHelper.writeMessage("Имя пользователя:");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() { return true; }

    protected SocketThread getSocketThread() { return new SocketThread(); }

    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Произошло исключение IOException");
            clientConnected = false;
        }
    }

    public class SocketThread extends Thread {

        protected void processIncomingMessage(String message) { ConsoleHelper.writeMessage(message); }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(String.format("Участник с именем %s присоединился к чату", userName));
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(String.format("Участник с именем %s покинул чат", userName));
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            synchronized (Client.this) {
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message receiveMessage = connection.receive();
                if (receiveMessage.getType() == MessageType.NAME_REQUEST) {
                    connection.send(new Message(MessageType.USER_NAME, getUserName()));
                } else if (receiveMessage.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    break;
                } else throw new IOException("Unexpected MessageType");
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message receiveMessage = connection.receive();
                if (receiveMessage.getType() == MessageType.TEXT) {
                    processIncomingMessage(receiveMessage.getData());
                } else if (receiveMessage.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(receiveMessage.getData());
                } else if (receiveMessage.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(receiveMessage.getData());
                } else throw new IOException("Unexpected MessageType");
            }   
        }

        public void run() {
            try {
                Socket socket = new Socket(getServerAddress(), getServerPort());
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }
    }

    public void run() {
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
            try {
                wait();
                if (clientConnected) ConsoleHelper.writeMessage("Соединение установлено.\n" +
                        "Для выхода наберите команду 'exit'.");
                else ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
                while (clientConnected) {
                    String s = ConsoleHelper.readString();
                    if (s.equals("exit")) break;
                    if (shouldSendTextFromConsole()) sendTextMessage(s);
                }
            } catch (InterruptedException exception) {
                ConsoleHelper.writeMessage("Во время ожидания возникло исключение");
            }
        }
    }

    public static void main(String[] args) {
        new Client().run();
    }

}
