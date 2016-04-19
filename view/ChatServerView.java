package view;

import model.ChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatServerView extends JFrame implements Runnable {

    protected Socket socket;
    protected DataOutputStream outStream;
    protected JTextArea outTextArea;
    protected JTextField infoServerStatus;
    protected Button onOf;
//    protected boolean isOn;

    public ChatServerView(String title, ChatServer cs) {
        super(title);
        setSize(300, 200);
        setLocation(20, 20);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());

        add(BorderLayout.CENTER, outTextArea = new JTextArea());
        add(BorderLayout.SOUTH, jp);
        jp.add(BorderLayout.CENTER, infoServerStatus = new JTextField());
        jp.add(BorderLayout.EAST, onOf = new Button("Выключить"));
        onOf.setBackground(Color.pink);
        infoServerStatus.setHorizontalAlignment(JTextField.CENTER);
        infoServerStatus.setText("Сервер работает");
        infoServerStatus.setBackground(Color.GREEN);
        infoServerStatus.setEditable(false);
        onOf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cs.isOnOf()){
                    try {
                        cs.serverOf();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    onOf.setBackground(Color.GREEN);
                    onOf.setLabel("Включить");
                    infoServerStatus.setText("Сервер выключен");
                    infoServerStatus.setBackground(Color.pink);
                }else {
                    try {
                        cs.serverOn();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    onOf.setBackground(Color.pink);
                    onOf.setLabel("Выключить");
                    infoServerStatus.setText("Сервер работает");
                    infoServerStatus.setBackground(Color.GREEN);
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e); //todo
//                isOn = false;
                try {
                    outStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        (new Thread(this)).start();
    }

    @Override
    public void run() {
//        isOn = true;
    }
}
