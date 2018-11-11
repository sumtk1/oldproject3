package com.gloiot.chatsdk.socket;

import android.util.Log;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 创建者 zengming.
 * 功能：Socket服务,专用操作Socket连接。断开连接
 */
public class SocketServer {

    public static Socket mSocket = LinkServer.getSocket();

    public SocketServer() {

    }

    /**
     * socket连接服务器
     */
    public static void socketOnConnect() {
        mSocket.on(Socket.EVENT_CONNECT, SocketListener.getInstance().onConnect);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, SocketListener.getInstance().onConnectError);
        mSocket.on(Socket.EVENT_ERROR, SocketListener.getInstance().onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, SocketListener.getInstance().onConnectError);
        mSocket.connect();
    }

    /**
     * 打开指定的socket连接
     *
     * @param event    连接名
     * @param listener 回调事件
     */
    public static void socketOn(String event, Emitter.Listener listener) {
        mSocket.on(event, listener);
        mSocket.connect();
    }

    /**
     * 发送心跳消息
     *
     * @param ack 回调事件
     */
    public static void socketHeartbeat(Ack ack) {
        mSocket.emit("time", "", ack);
    }

    /**
     * 关闭指定的socket连接
     *
     * @param event    连接名
     * @param listener 回调事件
     */
    public static void socketOff(String event, Emitter.Listener listener) {
        mSocket.off(event, listener);
    }

    public static void socketOff(String event) {
        mSocket.off(event);
    }

    /**
     * 断开重连
     */
    public static void disReconnection() {
        Log.e("chat", "disReconnection");
        mSocket.off("connectionResult");
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
        mSocket.off(Socket.EVENT_ERROR);
        mSocket.off(Socket.EVENT_CONNECT_ERROR);
        mSocket.disconnect();
        socketOnConnect();
    }

    /**
     * 断开socket连接
     */
    public static void disconnect() {
        mSocket.disconnect();
    }

    /**
     * 返回socket连接状态
     */
    public static boolean connected() {
        return mSocket.connected();
    }
}
