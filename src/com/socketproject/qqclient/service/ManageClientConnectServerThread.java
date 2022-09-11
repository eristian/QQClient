package com.socketproject.qqclient.service;

import java.util.HashMap;

/**
 * 项目名：    QQClient
 * 文件名：    ManageClientConnectServerThread
 * 创建时间：   2022/9/8 16:57
 *
 * @author crazy Chen
 * 描述：   该类管理客户端连接到服务器端线程的类   TODO
 */
public class ManageClientConnectServerThread {
    //把多个线程放入一个HashMap集合，key就是id，value就是线程
    private static HashMap<String, ClientConnectServerThread> hashMap = new HashMap<>();
    //将某个线程加入到集合中
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread){
        hashMap.put(userId, clientConnectServerThread);
    }
    //通过usrId 可以得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hashMap.get(userId);
    }

}
