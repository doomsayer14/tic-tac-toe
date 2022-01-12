package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main {

    private static final int PORT = 34522;

    //for exit
    public volatile static AtomicBoolean work = new AtomicBoolean(true);

    public static void main(String[] args) {
        new Main().runServer();
    }

    private void runServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            while (work.get()) {
                Session session = new Session(server.accept());
                session.start();
                session.interrupt();
                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!work.get()) {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
