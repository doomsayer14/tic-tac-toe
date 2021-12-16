package server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //start server
        new client.Main().start();
    }
}
