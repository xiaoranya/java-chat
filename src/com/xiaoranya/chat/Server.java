package com.xiaoranya.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {

        Vector<UserThread> vector = new Vector<>();

        ExecutorService es = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(18569);
            while (true) {
                Socket socket = serverSocket.accept();
                UserThread user = new UserThread(socket,vector);
                es.execute(user);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

class UserThread implements Runnable {
    private Socket socket;
    private Vector<UserThread> users;
    private String name;
    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;
    private boolean flag = true;

    public UserThread(Socket socket, Vector<UserThread> users) {
        this.socket = socket;
        this.users = users;
        users.add(this);
    }

    @Override
    public void run() {
        try{
            System.out.println("客户端：" + socket.getInetAddress().getHostAddress());
            oIn = new ObjectInputStream(socket.getInputStream());
            oOut = new ObjectOutputStream(socket.getOutputStream());
            while(flag){
                Message message = (Message) oIn.readObject();
                int type = message.getType();
                switch(type){
                    case MessageType.TYPE_SEND :
                        UserThread ut;
                        int size = users.size();
                        for(int i = 0; i < size; i++) {
                            ut = users.get(i);
                            ut.oOut.writeObject(message);
                        }
                        break;
                    case MessageType.TYPE_LOGIN :
                        name = message.getFrom();
                        oOut.writeObject(message);
                        break;
                }
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}

