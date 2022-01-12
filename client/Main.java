package client;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

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

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
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
            String path = "/Users/vyesman/IdeaProjects/JSON Database/JSON Database/task/src/client/data/";
            setJsonFile(args[1]);
            sendFile(path + getJsonFile());
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

    private void sendFile(String jsonFile) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile));
            String writeString = bufferedReader.readLine();
            output.writeUTF(writeString);
            // sending message to the server
            System.out.println("Sent: " + writeString);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void connectToServer(String type, String key) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String writeString = gson.toJson(new server.Request(type, key));
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

            String writeString = gson.toJson(new server.Request(type, key, value));
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

            String writeString = gson.toJson(new server.Request(type));
            output.writeUTF(writeString);
            // sending message to the server
            System.out.println("Sent: " + writeString);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
