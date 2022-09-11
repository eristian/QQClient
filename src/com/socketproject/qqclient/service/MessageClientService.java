package com.socketproject.qqclient.service;

import com.socketproject.qqcommon.Message;
import com.socketproject.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * 项目名：    QQClient
 * 文件名：    MessageClientService
 * 创建时间：   2022/9/9 20:51
 *
 * @author crazy Chen
 * 描述：  该类提供和消息相关的服务方法    TODO
 */
public class MessageClientService {
    /**
     * @param content  内容
     * @param senderId 发送者
     * @param getterId 接收者
     */
    public void sendMessageToOne(String content, String senderId, String getterId) {
        Message message = new Message();
        message.setSender(senderId);
        message.setReceiver(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        System.out.println(senderId + " 对 " + getterId + " 说 " + "{" + content + "}");
        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMessageToAll(String content, String senderId) {
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setReceiver("all");
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        System.out.println(senderId + " 对所有人说: " + "{" + content + "}");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
