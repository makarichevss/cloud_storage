package ru.geekbrains.cloud.server.io;

import ru.geekbrains.cloud.common.files.FileSerializable;
import ru.geekbrains.cloud.server.io.clienthandler.ClientHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final String SERVER_PORT = "server.port";

    private static Properties prop = new Properties();

    static {
        try {
            prop.load(new FileInputStream("D:/Cloud/cloud_server/src/main/resources/conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(prop.getProperty(SERVER_PORT)))) {
            while (true) {
                //Ожидаем подключение пользователей
                System.out.println("Wait client connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client is connect...");

                //Создаем объект клиентского подключения
                ClientHandler clientHandler = new ClientHandler(socket, this);
                //Добавляем и запускаем клиентский поток
                executorService.execute(clientHandler);
                //Записываем в список нового подключившегося клиента
                clientHandlers.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //Если на сервере произошла ошибка гасим все клиентские потоки
            executorService.shutdownNow();

        }
    }

    //Метод сохранения файлов на диск
    public synchronized void saveFileToServer(FileSerializable file, ClientHandler clientHandler) throws IOException {
        Path path = Paths.get(clientHandler.getLogin());
        path.resolve(file.getPath());

        System.out.println(path);
        System.out.println(file);

        if (Files.notExists(path)) {
            Files.write(path, file.getArr(), StandardOpenOption.CREATE_NEW);
        } else {
            Files.write(path, file.getArr(), StandardOpenOption.APPEND);
        }
    }
}
