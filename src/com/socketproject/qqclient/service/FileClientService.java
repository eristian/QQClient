package com.socketproject.qqclient.service;

import com.socketproject.qqcommon.Message;
import com.socketproject.qqcommon.MessageType;

import java.awt.*;
import java.io.*;

/**
 * 项目名：    QQClient
 * 文件名：    FileClientService
 * 创建时间：   2022/9/11 8:48
 *
 * @author crazy Chen
 * 描述：   该类完成文件传输服务   TODO
 */
public class FileClientService {
    /**
     * @param src      源文件路径
     * @param dest     目标路径
     * @param senderId 发送者
     * @param getterId 接收者
     */
    public void sendFileToOne(String src, String dest, String senderId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setReceiver(getterId);
        message.setDest(dest);
        message.setSrc(src);
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(src));
            byte[] buffer = new byte[(int) new File(src).length()];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                message.setFileBiBytes(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("\n" + getterId + "给" + getterId + "发送文件: " + src + "到对方电脑的目录: " + dest);
        //发送文件
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
