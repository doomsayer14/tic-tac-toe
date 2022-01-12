package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Session extends Thread {
    private Socket socket;

    private final String PATH = "/Users/vyesman/IdeaProjects/JSON Database/JSON Database/task/src/server/data/db.json";

    //lockers for parallelizing
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private Gson gson;

    //keep client's request
    private Request request;

    //output for client
    private Response response;

    //constructor
    //-------------------------------
    Session(Socket socketForClient) throws IOException {
        this.socket = socketForClient;
        gson = new Gson();
    }
    //-------------------------------

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String inputString = input.readUTF();
            request = gson.fromJson(inputString, Request.class);// reading a message

            switch (request.getType()) {
                case ("set"):
                    setInfo(request.getKey(), request.getValue());
                    output.writeUTF(gson.toJson(new Response("OK")));
                    break;
                case ("get"):
                    String s = getInfo(request.getKey());
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
                    if (!dbJson.getInstance().containsKey(request.getKey())) {
                        output.writeUTF(gson.toJson(new Response("ERROR", "No such key")));
                    } else {
                        deleteInfo(request.getKey());
                        output.writeUTF(gson.toJson(new Response("OK")));
                    }
                    break;
                case ("exit"):
                    output.writeUTF("OK"); // resend it to the client
                    Main.work.set(false);
                    return;
                default:
                    System.out.println("ERROR");
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized private void deleteInfo(String key) throws IOException {
        writeLock.lock();
        dbJson.getInstance().remove(key);
        Files.write(Paths.get(PATH), "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        for (Map.Entry<String, String> set : dbJson.getInstance().entrySet()) {
            String str = gson.toJson(new dbJson(set.getKey(), set.getValue())) + System.lineSeparator();
            Files.write(Paths.get(PATH), str.getBytes(), StandardOpenOption.APPEND);
        }
        writeLock.unlock();
    }

    synchronized private String getInfo(String key) throws IOException {
        readLock.lock();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH));
        String str = bufferedReader.readLine();
        while (str != null) {
            if (str.contains(key)) {
                readLock.unlock();
                return dbJson.getInstance().get(key);
            }
            str = bufferedReader.readLine();
        }
        readLock.unlock();
        return "ERROR";

//        readLock.lock();
//        System.out.println(dataBase);
//        if (dataBase.containsKey(key)) {
//            readLock.unlock();
//            return dataBase.get(key);
//        }
//        readLock.unlock();
//        return "ERROR";
    }

    //set specified string in specified index
    synchronized private void setInfo(String key, String value) throws IOException {
        writeLock.lock();
        dbJson.getInstance().put(key, value);
        String str = gson.toJson(new dbJson(key, value)) + System.lineSeparator();
        Files.write(Paths.get(PATH), str.getBytes(), StandardOpenOption.APPEND);
        writeLock.unlock();
    }
}

