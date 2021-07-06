package ru.geekbrains.cloud.client.netty;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public void start() {
        try {
            Socket socket = new Socket("localhost", 8189);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());
            out.write(new byte[]{115, 21, 31});
            String x = in.nextLine();
            System.out.println("A: " + x);
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
