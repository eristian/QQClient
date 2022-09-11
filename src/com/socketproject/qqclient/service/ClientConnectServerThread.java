package com.socketproject.qqclient.service;

import com.socketproject.qqcommon.Message;
import com.socketproject.qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 项目名：    QQClient
 * 文件名：    ClientConnectServerThread
 * 创建时间：   2022/9/8 16:45
 *
 * @author crazy Chen
 * 描述：      TODO
 */
public class ClientConnectServerThread extends Thread {
    //该线程需要持有Socket
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通讯，所以做成无限循环
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象，线程会阻塞在这里
                Message message = (Message) ois.readObject();
                //判断message的类型,如果是MESSAGE_RET_ONLINE_FRIEND
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息并显示
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n=========当前在线用户列表=========");
                    for (String onlineUser : onlineUsers) {
                        System.out.println("用户: " + onlineUser + " 正在线上");
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    if (message.getReceiver().equals("all")) {
                        System.out.println("\n" + message.getSender() + " 对所有人说: "
                                + "{" + message.getContent() + "}");
                    } else {
                        System.out.println("\n" + message.getSender() + " 对 " + message.getReceiver() + " 说: "
                                + "{" + message.getContent() + "}");
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    System.out.println("\n" + message.getSender() + " 给 " + message.getReceiver() + " 发送了文件" + message.getSrc() + "到" + message.getDest());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBiBytes());
                    fileOutputStream.close();
                    System.out.println("已接收文件");
                } else {
                    System.out.println("是其它类型的message，暂时不处理。。。");
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
