package client;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 34522;

    //command to request from a type
    @Parameter(names = "-in")
    private String jsonFile;

    //command, that user has to type in
    @Parameter(names = "-t")
    private String type;

    //key in dataBase
    @Parameter(names = "-k")
    private String key;

    //value to save in the database
    @Parameter(names = "-v")
    private String value;

    private final Gson gson;

    boolean check = true;

    //getter and setters
    //-------------------------------
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    //-------------------------------

    //constructor
    public Main() {
        type = "";
        key = "";
        value = "";
        gson = new Gson();
    }

    public static void main(String[] args) {
        new Main().start(args);
    }

    //method that server should call
    public void start(String[] args) {

        if (args[0].equals("-in")) {

        }

        setType(args[1]);
        switch (getType()) {
            case ("set"):
                setKey(args[3]);
                setValue(args[5]);
                connectToServer(getType(), getKey(), getValue());
                break;
            case ("get"):
                setKey(args[3]);
                connectToServer(getType(), getKey());
                break;
            case ("delete"):
                setKey(args[3]);
                connectToServer(getType(), getKey());
                break;
            case ("exit"):
                connectToServer(getType());
                return;
            default:
                System.out.println("ERROR");
                break;
        }
    }


    private void connectToServer(String type, String key) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String writeString = gson.toJson(new Request(type, key));
            output.writeUTF(writeString);
            // sending message to the server
            System.out.println("Sent: " + writeString);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //overload for setInformation()
    private void connectToServer(String type, String key, String value) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String writeString = gson.toJson(new Request(type, key, value));
            output.writeUTF(writeString);
            // sending message to the server
            System.out.println("Sent: " + writeString);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Overload for "exit"
    private void connectToServer(String type) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String writeString = gson.toJson(new Request(type));
            output.writeUTF(writeString);
            // sending message to the server
            System.out.println("Sent: " + writeString);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //made for Gson for convenience
    private class Request {
        private String type;
        private String key;
        private String value;

        //Constructors
        //-------------------------------
        private Request() {

        }

        public Request(String type) {
            this.type = type;
        }

        public Request(String type, String key) {
            this.type = type;
            this.key = key;
        }

        public Request(String type, String key, String value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }
        //-------------------------------

        //getters and setters
        //-------------------------------
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
        //-------------------------------
    }
}
