package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int PORT = 34522;

    //simulated database of strings of size 1000
    private final List<String> dataBase;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private Main() {
        dataBase = new ArrayList<>(1000);
    }

    public static void main(String[] args) {
        new Main().runServer();
    }

    private void runServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            initDataBase();
            while (true) {
                try (
                        Socket socket = server.accept(); // accepting a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msg = input.readUTF(); // reading a message
                    String[] arr = msg.split(" ", 3);
                    if (arr.length == 0) {
                        output.writeUTF("ERROR"); // resend it to the client
                    } else {
                        if (!arr[0].equals("exit")) {
                            setIndex(Integer.parseInt(arr[1]));
                        } else {
                            setIndex(-1);
                        }
                        switch (arr[0]) {
                            case ("set"):
                                setInformation(getIndex(), arr[2]);
                                if (dataBase.get(getIndex() - 1).equals("")) {
                                    output.writeUTF("ERROR");
                                } else {
                                    output.writeUTF("OK");
                                }
                                break;
                            case ("get"):
                                String s = getInfo(getIndex());
                                if (s.equals("") || s.equals("ERROR")) {
                                    output.writeUTF("ERROR");
                                } else {
                                    output.writeUTF(s);
                                }
                                break;
                            case ("delete"):
                                if (getIndex() < 1 || getIndex() > 1000) {
                                    output.writeUTF("ERROR");
                                } else {
                                    deleteInfo(getIndex());
                                    if (!dataBase.get(index - 1).equals("")) {
                                        output.writeUTF("ERROR");
                                    } else {
                                        output.writeUTF("OK");
                                    }
                                }
                                break;
                            case ("exit"):
                                output.writeUTF("OK"); // resend it to the client
                                return;
                            default:
                                System.out.println("ERROR");
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteInfo(int index) {
        dataBase.set(index - 1, "");
    }

    private String getInfo(int index) {
        if (index >= 1 && index <= 1000) {
            return dataBase.get(index - 1);
        }
        return "ERROR";
    }

    //set specified string in specified index
    private void setInformation(int index, String info) {
        if (index >= 1 && index <= 1000) {
            dataBase.set(index - 1, info);
        }
    }

    //called once to init dataBase with ""
    private void initDataBase() {
        for (int i = 0; i < 1000; i++) {
            dataBase.add("");
        }
    }
}
