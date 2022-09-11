package com.socketproject.qqclient.service;

import com.socketproject.qqcommon.Message;
import com.socketproject.qqcommon.MessageType;
import com.socketproject.qqcommon.User;

import javax.management.modelmbean.ModelMBeanAttributeInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 * 项目名：    QQClient
 * 文件名：    UserClientService
 * 创建时间：   2022/9/8 16:21
 *
 * @author crazy Chen
 * 描述：   该类完成用户登录验证和用户注册等功能   TODO
 */
public class UserClientService {
    private User u = new User();//因为可能在其它地方要使用user信息，因此设置为成员属性
    private Socket socket;

    //根据 userId 和pwd 到服务器端验证该用户是否合法
    public boolean checkUser(String userId, String userPwd) {
        boolean flag = false;
        //创建User对象
        u.setUserId(userId);
        u.setPassword(userPwd);
        //连接到服务端，发送u对象
        try {
            socket = new Socket(InetAddress.getByName("192.168.3.16"), 9999);
            // 发送u对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // 将u对象发送出去
            oos.writeObject(u);
            oos.flush();
            // 接收服务端的u对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 从服务端接收到Message对象
            Message msg = (Message) ois.readObject();
            // 判断u对象是否合法
            if (msg.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEEDED)) {
                //创建一个和服务器保持通讯的线程->创建一个线程类ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //启动线程
                clientConnectServerThread.start();
                //为了扩展，将线程放到集合中管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                flag = true;
            } else {
                //如果登录失败，就不能启动和服务器通讯的线程，关闭socket
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return flag;
    }

    //向服务器端请求在线用户列表
    public void onlineFriendList(){
        //发送一个message，类型为MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器
        //得到当前线程的socket对应的objectOutputStream对象
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            // 将message对象写入到socket对应的objectOutputStream中
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    //编写方法退出客户端
    public  void  logOut(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLINE_EXIT);
        message.setSender(u.getUserId());//指定谁退出
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            // 将message对象写入到socket对应的objectOutputStream中
            oos.writeObject(message);
            oos.flush();
            System.out.println(u.getUserId()+" 退出系统");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
