package model;

import command.ChatHandler;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static final String portServer = "8082";
    private static boolean onOf = true;
    private static int countClient = 0;
    private static int countCurrentClient = 0;

    public static boolean isOnOf() {
        return onOf;
    }

    public static void setOnOf(boolean onOf) {
        ChatServer.onOf = onOf;
    }

    public static void main(String[] args) {
        DataInputStream dis = null;
        try {
            new ChatServer(Integer.parseInt(portServer));
//            dis = new DataInputStream(new BufferedInputStream(service.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatServer(int port) throws IOException {
        ServerSocket service = new ServerSocket(port);
        new view.ChatServerView("Server");
        try {
            while (true) {
                Socket s = service.accept();
                String inetAddress = s.getInetAddress().toString();
                System.out.println("Accepted from " + inetAddress);
                ChatHandler handler = new ChatHandler(s);
                handler.start();
                countClient++;
                countCurrentClient++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            countCurrentClient--;
            service.close();
        }
    }
}
