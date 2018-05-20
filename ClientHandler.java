package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

class ClientHandler implements Runnable {
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                received = dis.readUTF();
                System.out.println(received);
                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();
                for (ClientHandler mc : Server.ar) {
                    if (mc.name.equals(recipient) && mc.isloggedin) {
                        mc.dos.writeUTF(this.name+" : "+MsgToSend);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
