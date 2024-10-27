package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    static private Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private static class Handler extends Thread {
        private Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message answer = connection.receive();
                if (answer.getType() == MessageType.USER_NAME) {
                    String name = answer.getData();
                    if ((name != null) && (!name.equals(""))) {
                        if (!connectionMap.containsKey(name)) {
                            connectionMap.put(name, connection);
                            connection.send(new Message(MessageType.NAME_ACCEPTED));
                            return name;
                        }
                    }
                }
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
                Message userAddedMessage = new Message(MessageType.USER_ADDED, entry.getKey());
                if (!entry.getKey().equals(userName)) connection.send(userAddedMessage);
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message incomingMessage = connection.receive();
                if (incomingMessage.getType() == MessageType.TEXT) {
                    String broadcastText = userName + ": " + incomingMessage.getData();
                    sendBroadcastMessage(new Message(MessageType.TEXT, broadcastText));
                } else ConsoleHelper.writeMessage("Ошибка. Сообщение не является текстом!");
            }
        }

        public void run() {
            String socketAddress = socket.getRemoteSocketAddress().toString();
            ConsoleHelper.writeMessage(String.format("Установлено новое соединение с удаленным адресом %s.", socketAddress));
            String userName = null;
            try {
                Connection connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);


            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage(String.format("Произошла ошибка при обмене данными с удаленным адресом %s.", socketAddress));
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (userName != null) {
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
            }
            ConsoleHelper.writeMessage(String.format("Соединение с удаленным адресом %s закрыто.", socketAddress));
        }
    }

    public static void main(String[] args) throws IOException {
        ConsoleHelper.writeMessage("Введите номер порта для запуска сервера:");
        int port = ConsoleHelper.readInt();
        ServerSocket srvSocket = new ServerSocket(port);
        System.out.println("Server started");
            try {
                while (true) {
                    Socket socket = srvSocket.accept();
                    new Handler(socket).start();
                }
            } catch (Exception e) {
                System.out.println("Exception");
                srvSocket.close();
            }
    }

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
            try {
                entry.getValue().send(message);
            } catch (IOException e) {
                System.out.printf("Не смогли отправить сообщение. Клиент - %s.%n", entry.getKey());
            }
        }
    }
}
