package ru.geekbrains.cloud.server.io.clienthandler;

import ru.geekbrains.cloud.common.ObjectSerialization;
import ru.geekbrains.cloud.server.io.Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String login = "user";

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            //Запускаем авторизацию.
            Thread authorizationClient = new Thread(new Runnable() {
                @Override
                public void run() {
                }
            });
            authorizationClient.start();
            authorizationClient.join();

            //Запускаем поток по чтению данных из потока от клиента
            Thread readThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        readMess();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        closeConnection();
                    }
                }
            });
            readThread.setDaemon(true);
            readThread.start();
            readThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void writeMess(String mess) throws IOException {
        out.writeUTF(mess);
    }

    private void readMess() throws ClassNotFoundException, IOException {
        String mess = null;

        while (true) {
            mess = in.readUTF();
            System.out.println(mess);

            //Десериализуем обьект
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(mess.getBytes());
                 ObjectInputStream objIn = new ObjectInputStream(byteIn)) {

                ObjectSerialization objSer = (ObjectSerialization) objIn.readObject();

                //Если прилетел запрос на закачку файла на сервер
                switch (objSer.getOperations()) {
                    case UPLOAD: {
                        //Сохраняем файл на сервере
                        server.saveFileToServer(objSer.getFile(), this);
                        break;
                    }
                    case DIR: {

                    }
                }
            } catch (IOException e) {
                throw e;
            }
        }
    }

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLogin() {
        return login;
    }
}
