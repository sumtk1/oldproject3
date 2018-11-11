package com.gloiot.chatsdk.socket;

import com.gloiot.chatsdk.utlis.Constant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * 创建者 zengming.
 * 功能：链接通讯服务器
 */
public class LinkServer {

    private static Socket mSocket;

    /**
     * 连接通讯服务器
     */
    public static void init() {

        try {
            IO.Options options = new IO.Options();
            String[] transport = {"websocket"};
            options.transports = transport;
            mSocket = IO.socket(Constant.CHAT_SERVER_URL, options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket getSocket() {
        return mSocket;
    }
}
