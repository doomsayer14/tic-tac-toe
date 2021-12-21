package client;

import com.beust.jcommander.Parameter;

import java.io.*;
import java.net.Socket;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 34522;

    //command, that user has to type in
    @Parameter(names = "-t")
    private String command;

    //index for command
    @Parameter(names = "-i")
    private int index;

    //value to save in the database
    @Parameter(names = "-m")
    private String info;

    boolean check = true;

    //getter and setters
    //-------------------------------
    public String getCommand() {
        return command;
    }

    public int getIndex() {
        return index;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    //-------------------------------

    //constructor
    public Main() {
        command = "";
    }

    public static void main(String[] args) {
        new Main().start(args);
    }

    //method that server should call
    public void start(String[] args) {

            setCommand(args[1]);
            switch (getCommand()) {
                case ("set"):
                    setIndex(Integer.parseInt(args[3]));
                    setInfo(args[5]);
                    connectToServer(getCommand(), getIndex(), getInfo());
                    break;
                case ("get"):
                    setIndex(Integer.parseInt(args[3]));
                    connectToServer(getCommand(), getIndex());
                    break;
                case ("delete"):
                    setIndex(Integer.parseInt(args[3]));
                    connectToServer(getCommand(), getIndex());
                    break;
                case ("exit"):
                    connectToServer(getCommand());
                    return;
                default:
                    System.out.println("ERROR");
                    break;
            }
        }


    private void connectToServer(String command, int index) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            output.writeUTF(command + " " + index);
            // sending message to the server
            System.out.println("Sent: " + command + " " + index);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //overload for setInformation()
    private void connectToServer(String command, int index, String info) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            output.writeUTF(command + " " + index + " " + info);
            // sending message to the server
            System.out.println("Sent: " + command + " " + index + " " + info);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Overload for "exit"
    private void connectToServer(String command) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            output.writeUTF(command);
            // sending message to the server
            System.out.println("Sent: " + command);
            String receivedMsg = input.readUTF(); // response message
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
