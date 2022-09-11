package com.socketproject.qqclient.view;

import com.socketproject.qqclient.service.FileClientService;
import com.socketproject.qqclient.service.MessageClientService;
import com.socketproject.qqclient.service.UserClientService;
import com.socketproject.qqclient.utlis.Utility;
import com.socketproject.qqcommon.Message;

import java.net.Socket;

/**
 * 项目名：    QQClient
 * 文件名：    QQView
 * 创建时间：   2022/9/8 15:43
 *
 * @author crazy Chen
 * 描述：  客户端的登录界面    TODO
 */
public class QQView {
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统！");
    }

    //显示主菜单
    private boolean loop = true;
    String key = "";
    private UserClientService userClientService = new UserClientService();//这个对象用于登录服务器
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();

    private void mainMenu() {
        while (loop) {
            System.out.println("==================欢迎登录网络通讯系统=================");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 登录系统");
            System.out.print("请输入你的选择: ");
            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.print("请输入用户号: ");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密码: ");
                    String userPwd = Utility.readString(50);
                    //需要到服务器端验证是否合法

                    if (userClientService.checkUser(userId, userPwd)) {
                        //进入二级菜单
                        while (loop) {
                            System.out.println("\n=============网络通讯系统二级菜单(用户 " + userId + " )===============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择: ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "2":
                                    System.out.println("请输入想对所有人说的话： ");
                                    String contentAll = Utility.readString(100);
                                    //编写一个方法，将消息发送
                                    messageClientService.sendMessageToAll(contentAll, userId);
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的用户号(在线): ");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话： ");
                                    String content = Utility.readString(100);
                                    //编写一个方法，将消息发送
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "4":
                                    System.out.println("请输入你想发文件给哪个用户(在线):");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入你想发送文件的路径:");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入你想发送到对方电脑的目标路径:");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src, dest, userId, getterId);
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "9":
                                    System.out.println("退出系统");
                                    loop = false;
                                    userClientService.logOut();
                                    break;
                                default:
                                    System.out.println("输入有误");
                            }
                        }
                    } else { //登录服务器失败
                        System.out.println("==========登录失败==========");
                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("退出系统");
                    break;
                default:
                    break;
            }
        }
    }
}
