package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    private static final int PORT = 34522;

    private ServerSocket server;

    //simulated database of strings of size 1000
    private final Map<String, String> dataBase;

    private final String PATH = "/Users/vyesman/IdeaProjects/JSON Database/JSON Database/task/src/server/data folder/db.json";
    private final File dbJson;
    private final BufferedWriter bufferedWriter;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Gson gson;

    //keep client's request
    private Request request;

    //output for client
    private Response response;

    //command, that user has to type in
    private String type;

    //key of dataBase
    private String key;

    //value to save in the database
    private String value;

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

    //constructor
    //-------------------------------
    private Main() throws IOException {
        type = "";
        key = "";
        value = "";
        dataBase = new HashMap<>();
        gson = new Gson();
        dbJson = new File(PATH);

        FileWriter fileWriter = new FileWriter(dbJson, true);
        bufferedWriter = new BufferedWriter(fileWriter);

    }
    //-------------------------------

    public static void main(String[] args) throws IOException {

        new Main().runServer();
    }

    private void runServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            Session session = new Session(server.accept());
            while (true) {
                try (
                        Socket socket = server.accept(); // accepting a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String inputString = input.readUTF();
                    String str = inputString.replace("\\", "");


                    request = gson.fromJson(str, Request.class);// reading a message
                    setType(request.getType());
                    setKey(request.getKey());
                    setValue(request.getValue());

                    switch (request.getType()) {
                        case ("set"):
                            setInformation(getKey(), getValue());
                            output.writeUTF(gson.toJson(new Response("OK")));
                            break;
                        case ("get"):
                            String s = getInfo(getKey());
                            if (s.equals("ERROR")) {
                                output.writeUTF(gson.toJson(new Response(s, "No such key")));
                            } else {
                                response = new Response();
                                response.setResponse("OK");
                                response.setValue(s);
                                output.writeUTF(gson.toJson(response));
                            }
                            break;
                        case ("delete"):
                            if (!dataBase.containsKey(getKey())) {
                                output.writeUTF(gson.toJson(new Response("ERROR", "No such key")));
                            } else {
                                deleteInfo(getKey());
                                output.writeUTF(gson.toJson(new Response("OK")));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized private void deleteInfo(String key) throws IOException {
        writeLock.lock();
        dataBase.remove(key);
        bufferedWriter.write(gson.toJson(dataBase));
        writeLock.unlock();
    }

    synchronized private String getInfo(String key) throws IOException {
/*
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH));
        String str = bufferedReader.readLine();
        while (str != null) {
            if (str.contains(key)) {
                readLock.unlock();
                return str;
            }
            str = bufferedReader.readLine();
        }
        readLock.unlock();
        return "ERROR";
*/
        readLock.lock();
        if (dataBase.containsKey(key)) {
            readLock.unlock();
            return dataBase.get(key);
        }
        readLock.unlock();
        return "ERROR";
    }

    //set specified string in specified index
    synchronized private void setInformation(String key, String value) throws IOException {
        writeLock.lock();
        dataBase.put(key, value);
        String str = gson.toJson(new dbJson(key, value)) + System.lineSeparator();
        Files.write(Paths.get("/server/data folder/db.json"), str.getBytes(), StandardOpenOption.APPEND);
        writeLock.unlock();
    }

    //three classes below made for convenience to work with Gson

    private class dbJson {
        private String key;
        private String value;

        public dbJson(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    //class for output to client
    private class Response {
        private String response;
        private String reason;
        private String value;

        //Constructors
        //-------------------------------
        public Response() {
        }

        public Response(String response) {
            this.response = response;
        }

        public Response(String response, String reason) {
            this.response = response;
            this.reason = reason;
        }

        //-------------------------------

        //getters, setters
        //-------------------------------
        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        //-------------------------------
    }

    //class for client's input
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

    class Session extends Thread {
        private final Socket socket;

        public Session(Socket socketForClient) {
            this.socket = socketForClient;
        }

        public void run() {
            try (
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                for (int i = 0; i < 5; i++) {
                    String msg = input.readUTF();
                    output.writeUTF(msg);
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
