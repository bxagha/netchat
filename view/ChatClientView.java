package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class ChatClientView extends JFrame implements Runnable {

    protected Socket socket;
    protected DataInputStream inStream;
    protected DataOutputStream outStream;
    protected JTextArea outTextArea;
    protected JTextField inTextField;
    protected JTextField name;
    protected boolean isOn;

    public DataOutputStream getOutStream() {
        return outStream;
    }

    public ChatClientView(String title, Socket s, DataInputStream dis, DataOutputStream dos) {
        super(title);
        socket = s;
        inStream = dis;
        outStream = dos;


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(BorderLayout.CENTER, outTextArea = new JTextArea());
        outTextArea.setEditable(false);
        cp.add(BorderLayout.SOUTH, inTextField = new JTextField());
        cp.add(BorderLayout.NORTH, name = new JTextField());

        name.setText("Введите свое имя. Здесь. Вместо этого текста. :)");
        name.setForeground(Color.RED);
        inTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    outStream.writeUTF(name.getText() + ": " + inTextField.getText());
                    outStream.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    isOn = false;
                }
                inTextField.setText("");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    outStream.writeUTF(name.getText().concat(" вышел"));
                    outStream.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                super.windowClosed(e); //todo
                try {
                    outStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                isOn = false;
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setVisible(true);
        inTextField.requestFocus();
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        isOn = true;
        try {
            while (isOn) {
                String line = inStream.readUTF();
                outTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inTextField.setVisible(false);
            validate();
        }
    }
}
