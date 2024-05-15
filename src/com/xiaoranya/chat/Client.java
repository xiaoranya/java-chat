package com.xiaoranya.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ExecutorService es = Executors.newSingleThreadExecutor();
        try {

            Socket socket = new Socket("chat.xiaoranya.com", 18569);
            System.out.println("服务器连接成功！");

            ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream oIn = new ObjectInputStream(socket.getInputStream());

            System.out.println("请输入名称：");
            String name = input.nextLine();
            Message message = new Message(name, MessageType.TYPE_LOGIN, null);
            oOut.writeObject(message);

            es.execute(new readInfoThread(oIn));  //读取线程完成

            boolean flag = true;
            while(flag){

                message = new Message(name, MessageType.TYPE_LOGIN, null);

                message.setFrom(name);

                message.setType(MessageType.TYPE_SEND);

                message.setInfo(input.nextLine());

                oOut.writeObject(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class readInfoThread implements Runnable {
    private ObjectInputStream oIn; //输入流 用来读操作
    private boolean flag = true; //标记

    public readInfoThread(ObjectInputStream oIn) {
        this.oIn = oIn;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {

        try {

            while (flag) {

                Message message = (Message) oIn.readObject();

                System.out.println( message.getFrom() + "：" + message.getInfo());
            }

            if(oIn != null){
                oIn.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}