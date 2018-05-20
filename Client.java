package Socket;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client
{

    Client() throws IOException {
        String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
        JList<String> a = new JList<>(strings);
        JButton button = new JButton("Send");
        JTextField textField = new JTextField(20);
        JTextArea textArea = new JTextArea(8,20);
        JFrame frame = new JFrame("Client");
        frame.setLayout(new FlowLayout());
        frame.add(textArea);
        frame.add(a);
        frame.add(textField);
        frame.add(button);
        frame.setSize(350,300);
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        InetAddress ip = InetAddress.getByName("localhost");
        Socket s = new Socket(ip, 1234);
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        Thread sendMessage = new Thread(() -> {
            button.addActionListener(e -> {
                try {
                    textArea.append(textField.getText()+"\n");
                    dos.writeUTF(textField.getText());
                    textField.setText("");
                } catch (IOException k) {
                    k.printStackTrace();
                }
            });
        });

        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    String msgs = dis.readUTF();
                    textArea.append(msgs+"\n");
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }
    public static void main(String[] args) throws IOException {
        new Client();
    }
}