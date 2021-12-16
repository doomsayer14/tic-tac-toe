package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    //simulated database of strings of size 100
    private List<String> dataBase;

    //command, that user has to type in
    private String command;

    //index for command
    private int index;

    private BufferedReader consoleReader;

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
    //-------------------------------

    //constructor
    public Main() {
        command = "";
        dataBase = new ArrayList<>(100);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    //method that server should call
    public void start() throws IOException {
        initDataBase();
        while (!command.equals("exit")) {
            String str = consoleReader.readLine();
            String[] strings = str.split(" ", 3);
            if (strings[0] != null) {
                if (strings[0].equals("exit")) {
                    setCommand("exit");
                } else {
                    setCommand(strings[0]);
                    setIndex(Integer.parseInt(strings[1]));
                }
            }
            switch (command) {
                case ("set"):
                    setInfo(getIndex(), strings[2]);
                    break;
                case ("get"):
                    getInfo(getIndex());
                    break;
                case ("delete"):
                    deleteInfo(getIndex());
                    break;
                case ("exit"):
                    return;
                default:
                    System.out.println("ERROR");
                    break;
            }
        }
    }

    private void initDataBase() {
        for (int i = 0; i < 100; i++) {
            dataBase.add("");
        }
    }

    //delete information from "dataBase" by index
    private void deleteInfo(int index) {
        if (index >= 1 && index <= 100) {
            dataBase.set(index - 1, "");
            System.out.println("OK");
            return;
        }
        System.out.println("ERROR");

    }

    //get information from "dataBase" by index
    private void getInfo(int index) {
        if (index >= 1 && index <= 100) {
            if (!dataBase.get(index - 1).equals("")) {
                System.out.println(dataBase.get(index - 1));
                return;
            }
        }
        System.out.println("ERROR");
    }

    //set specified string in specified index
    private void setInfo(int index, String string) {
        if (index >= 1 && index <= 100) {
            dataBase.set(index - 1, string);
            System.out.println("OK");
            return;
        }
        System.out.println("ERROR");
    } 
}
