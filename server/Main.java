package server;

import java.io.*;
import java.net.*;

class Main {
    private static final int PORT = 34522;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            try (
                    Socket socket = server.accept(); // accepting a new client
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String msg = input.readUTF(); // reading a message
                System.out.println("Received: " + msg);
                String[] arr = msg.split(" ");
                msg = "A record # " + arr[arr.length - 1] + " was sent!";
                output.writeUTF(msg); // resend it to the client
                System.out.println("Sent: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
