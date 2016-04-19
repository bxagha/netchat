package model;

import view.ChatClientView;

import java.io.*;
import java.net.Socket;

/**
 * Created by Сергей on 13.04.2016.
 */
public class ChatClient {
    private static final String serverIP = "localhost";
    private static final String portServer = "8082";

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(serverIP, Integer.parseInt(portServer));
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            new view.ChatClientView("Chat #" + serverIP + ":" + portServer, socket, dis, dos);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (dos != null) dos.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException ex3) {
                ex3.printStackTrace();
            }
        }finally {
        }
    }
}
