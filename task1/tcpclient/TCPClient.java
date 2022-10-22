package tcpclient;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.ArrayList;



public class TCPClient {


    public TCPClient() {
    }

        public byte[] askServer (String hostname,int port, byte[] toServerBytes) throws IOException {
            try {

                byte[] fromServer = new byte[1024];

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                Socket newSocket = new Socket(hostname, port);

                newSocket.getOutputStream().write(toServerBytes); //send bytes on socket
                int i = 0;

                //recieve bytes on socket
                while ( (i =newSocket.getInputStream().read(fromServer)) != -1) {
                    out.write(fromServer,0, i);
                    fromServer = new byte[1024];
                }
                newSocket.close();
               // System.out.println(out.toByteArray().length);
                return out.toByteArray();

            } catch (IOException e) {
                // Output expected UnknownHostExceptions.
                System.out.println("Exception");
                throw new IOException();
            }

        }
}
