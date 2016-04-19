package model;

import command.ChatHandler;
import view.ChatServerView;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static final String portServer = "8082";
    private static int countClient = 0;
    private static int countCurrentClient = 0;
    private boolean onOf = true;
    private Socket s;
    private ServerSocket service;

    public boolean isOnOf() {
        return onOf;
    }

    public void setOnOf(boolean onOf) {
        this.onOf = onOf;
    }

    public static void main(String[] args) {
//        DataInputStream dis = null;
        try {
            new ChatServer(Integer.parseInt(portServer));

//            dis = new DataInputStream(new BufferedInputStream(service.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatServer(int port) throws IOException {
        service = new ServerSocket(port);
        ChatServerView csv = new view.ChatServerView("Server", this);
        try {
            while (onOf) {
                s = service.accept();
                countClient++;
                countCurrentClient++;
                String inetAddress = s.getInetAddress().toString();
                System.out.println("Accepted from " + inetAddress);
                ChatHandler handler = new ChatHandler(s, countClient);
                handler.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            countCurrentClient--;
            service.close();
        }
    }

    public void serverOf() throws IOException {
        setOnOf(false);
        service.close();
        countCurrentClient = 0;

    }

    public void serverOn() throws IOException {
        setOnOf(true);
        service=new ServerSocket(Integer.parseInt(portServer));

    }
}
