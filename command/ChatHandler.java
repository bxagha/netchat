package command;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;

public class ChatHandler extends Thread {

    protected Socket socket;
    protected DataInputStream inStream;
    protected DataOutputStream outStream;
    protected boolean isOn;
    protected Integer numberClient;

    protected static List<ChatHandler> handlers = Collections.synchronizedList(new ArrayList<ChatHandler>());

    public ChatHandler(Socket s, Integer nc) throws IOException {
        numberClient = nc;
        socket = s;
        inStream = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        outStream = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
    }

    public void run() {
        isOn = true;
        try {
            handlers.add(this);
            broadcast("user #" + numberClient + " вошел в чат");
            while (isOn) {
                String msg = inStream.readUTF();
                broadcast(msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            handlers.remove(this);
            try {
                outStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected static void broadcast(String message) {
        synchronized (handlers) {
            Iterator<ChatHandler> it = handlers.iterator();
            while (it.hasNext()) {
                ChatHandler c = it.next();
                try {
                    synchronized (c.outStream) {
                        c.outStream.writeUTF(message);
                    }
                    c.outStream.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    c.isOn = false;
                }
            }
        }
    }
}