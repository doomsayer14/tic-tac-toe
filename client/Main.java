package client;

import java.io.*;
import java.net.Socket;

class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 34522;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String msg = bufferedReader.readLine();
            output.writeUTF(msg); // sending message to the server
            System.out.println("Sent: " + msg);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
