package tcpclient;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.Socket;
import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;


public class TCPClient {
    public boolean shutdown;
    public Integer timeout;
    public Integer limit;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer (String hostname,int port, byte[] toServerBytes) throws IOException, SocketTimeoutException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Socket newSocket = new Socket(hostname, port);

        try {

            Integer myInf = Integer.MAX_VALUE;

            // if(this.timeout == null){
            //    this.timeout = 0;
            //}
            if(this.limit == null){
                this.limit = myInf;
            }

            byte[] fromServer = new byte[1024];
            //ByteArrayOutputStream out = new ByteArrayOutputStream();

            //Socket newSocket = new Socket(hostname, port);

            if(shutdown/* == true*/){
                newSocket.shutdownOutput();
                newSocket.close();
                return toServerBytes;
            }

            newSocket.getOutputStream().write(toServerBytes); //send bytes on socket

            int i = 0;

            // if(difference <= timeout) shutdown = true;

            //recieve bytes on socket
            if(timeout != null) {
                newSocket.setSoTimeout(timeout);
            }

            while (((i =newSocket.getInputStream().read(fromServer)) != -1) && shutdown == false) {
            //while ( shutdown == false) {
                System.out.println("ABOOOOOOOO");
             //   i = newSocket.getInputStream().read(fromServer);
               // System.out.println(String.format("i in shutdown false: %s", i));


                //if(out.toByteArray().length >= limit) {
                    // System.out.println(i);
                //System.out.println(i);
                    if(i > limit){
                        System.out.println("rup");

                        //shutdown = true;
                        System.out.println(String.format("limit: %s", limit));
                        out.write(fromServer,0, limit);
                        break;
                        //break;
                        //return out.toByteArray(); //istället för break
                    }
                out.write(fromServer,0, i); //limit?
                fromServer = new byte[1024];

                limit = limit - i;
              /*  if(i % 1024 != 0) {
                    System.out.println("breaked in modulous");
                    break;
                }*/
            }
            //fromServer = new byte[1024];


            newSocket.close();
            // System.out.println(out.toByteArray().length);
            return out.toByteArray();

        }
        catch(SocketTimeoutException ste){
            System.out.println("Timeout Exception");
            newSocket.close();
            return out.toByteArray();
        }
        catch (IOException e) {
            // Output expected UnknownHostExceptions.
            System.out.println("Exception IO");
            throw new IOException();
            //return out.toByteArray();
        }



    }
}
