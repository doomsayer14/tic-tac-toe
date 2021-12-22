package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final int PORT = 34522;

    //simulated database of strings of size 1000
    private final Map<String, String> dataBase;

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
    private Main() {
        type = "";
        key = "";
        value = "";
        dataBase = new HashMap<>();
        gson = new Gson();
    }
    //-------------------------------

    public static void main(String[] args) {
        new Main().runServer();
    }

    private void runServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
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

    private void deleteInfo(String key) {
        dataBase.remove(key);
    }

    private String getInfo(String key) {
        if (dataBase.containsKey(key)) {
            return dataBase.get(key);
        }
        return "ERROR";
    }

    //set specified string in specified index
    private void setInformation(String key, String value) {
        dataBase.put(key, value);
    }

    //two classes below made for convenience to work with Gson

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
}
