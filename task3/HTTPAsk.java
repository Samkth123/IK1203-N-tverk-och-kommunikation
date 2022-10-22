import tcpclient.TCPClient;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HTTPAsk {




    public static void main( String[] args) throws IOException {
        //Integer portnumber = Integer.parseInt(args[0]);
       // URL url = new URL(args[0]);


        try {
            final String syntax = "\r\n";

            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0])); //server socket används bara till accepten
            //System.out.println(portnumber);
             //read vad?
            byte[] byte1 = new byte[1024];
            char[] char1 = new char[1024];
            //write saker vi vill

            while(true) {
                boolean test = true;
                System.out.println("waiting for connection...");
                Socket socket = serverSocket.accept(); //väntar på connection, om inte connection så fastnar den här
                InputStream inputStream = socket.getInputStream(); // om vi läser från socket får vi input stream, här läser vi request från browser
                OutputStream outputStream = socket.getOutputStream(); // för att skriva till socket använder vi output stream

                //String html = "<html><head><title>what what </title></head> Hello din b <body></body></html>";
                int yeye = inputStream.read(byte1);
                //for(int i = 0; i < byte1.length; i++){
                //     System.out.print((char) byte1[i]);

                // }
                String s = new String(byte1, StandardCharsets.UTF_8);

                /*if(!s.contains("GET /ask")){
                    break;
                }*/

                String[] try1= Pattern.compile(" HTTP/1.1").split(s);
                if(!try1[0].contains("/ask")){
                    test = false;
                }
           //     System.out.println("-----AAAAAAAAA---------");
             //   System.out.println(try1[0]);
           //     System.out.println("-----BBBBBBBBB---------");
//SKA GET /ASK VARA HÄR UNDER ELLER ESKA JAG TA BORT????? vekrar funka iaf
           //     System.out.println("här är det fel");
                //DETTA ÄR ÄNDRINGEN!!!!!!!!!!!!!
                if (!s.contains("HTTP/1.1") || !s.contains("GET")) {
                    String htmlerror11 = "<html><head>" + "HTTP/1.1 400 Bad Request" + "</head></html>";

                    String responseerror = "HTTP/1.1 400 Bad request" + syntax
                            + "Content-Type: text/html" + syntax + syntax + htmlerror11 + syntax + syntax;
                    outputStream.write(responseerror.getBytes());
                   // socket.close();

                    test = false;
                    //return;

                }
                if (!s.contains("/ask") || !s.contains("hostname=") || !s.contains("port=") || !s.contains("HTTP")) {
                    String htmlerror = "<html><head>" + "HTTP/1.1 404 Page Not Found" + "</head></html>";

                    String response = "HTTP/1.1 404 Page Not Found" + syntax +  //status line : HTTPVERSION RESPONSECODE RESPONSEMEGGAE
                            "Content-Type: text/html" + syntax + syntax + htmlerror + syntax + syntax;
                    outputStream.write(response.getBytes());
                    System.out.println("EROEOREOREOROEOROE");
                  //  socket.close();

                    test = false;
                   // return;
                }

                if(test == true) {

                    //if(!s.contains("HTTP/1.1"))
                    // System.out.println("_----------");
                    //  System.out.println(s);
                    //   System.out.println("--ad-a-a-a--ad-a-");
                    // System.out.println("------------------");
                    String[] remove1 = Pattern.compile("HTTP").split(s);
                /*for(int i = 0; i <remove1.length; i++) {
                    System.out.println(remove1[i]);
                }*/
                    // System.out.println("------------------");
                    // System.out.println(remove1[0]);
                    // System.out.println("------------------");
                    String[] remove2 = Pattern.compile("/ask[?]").split(remove1[0]);
                    for (int i = 0; i < remove2.length; i++) {
                        System.out.println(remove2[i]);
                    }
                    // System.out.println("-----AAAAAAAAA---------");
                    // System.out.println(remove2[1]);
                    // System.out.println("-----BBBBBBBBB---------");

                    // System.out.println("------------------");
                    String[] remove3 = Pattern.compile("&").split(remove2[1]);
                    //System.out.println(remove3[1]);
                    for (int i = 0; i < remove3.length; i++) {
                        System.out.println(remove3[i]);
                    }

                    String hostname = null;
                    int limit = Integer.MAX_VALUE;
                    //Integer port = null;
                    int port = 0;
                    String string = null;
                    boolean shutdown = false;
                    Integer timeout = null;
                    //  if(test == true){
                    for (int i = 0; i < remove3.length; i++) {
                        //System.out.println("----0-0-0-0-0-0--0-0-0-0----");

                        if (remove3[i].contains("hostname")) {
                            hostname = remove3[i].split("=")[1].trim();
                        }
                        if (remove3[i].contains("limit")) {
                            limit = Integer.parseInt((remove3[i].split("=")[1]).trim());
                        }
                        if (remove3[i].contains("port")) {
                            port = Integer.parseInt((remove3[i].split("=")[1]).trim());
                        }
                        if (remove3[i].contains("string")) {
                            string = remove3[i].split("=")[1].trim();
                            System.out.println("ereoroeroe");
                        }
                        if (remove3[i].contains("shutdown")) {
                            shutdown = Boolean.valueOf(remove3[i].split("=")[1].trim());
                        }
                        if (remove3[i].contains("timeout")) {
                            timeout = Integer.parseInt((remove3[i].split("=")[1]).trim());
                        }
                    }
                    byte[] newstring;
                    if (string == null)
                        newstring = new byte[0];
                    else
                        newstring = (string.trim() + "\n").getBytes();

                    //    System.out.println("timeout " + timeout);
                    //    System.out.println("shutdown " + shutdown);
                    //    System.out.println("string " + string);
                    //    System.out.println("port " + port);
                    //    System.out.println("limit " + limit);
                    //    System.out.println("hostname " + hostname);
                    //    System.out.println(string + " ali ali");
                    TCPClient tcp = new TCPClient(shutdown, timeout, limit);
                    System.out.println("wassup1");
                    //System.out.println(tcp.askServer(hostname, port, string.getBytes()));
                    String returnedba = new String(tcp.askServer(hostname, port, newstring));
                    //System.out.println(returnedba);
                    System.out.println("wassup2");


                    String html = "<html><head>" + returnedba + "</head></html>";


                    String response = "HTTP/1.1 200 OK" + syntax +  //status line : HTTPVERSION RESPONSECODE RESPONSEMEGGAE
                            "Content-Type: text/html" + syntax + syntax + html + syntax + syntax;
                    //String test = returnedba;
                    outputStream.write(response.getBytes());
                }
                inputStream.close();
                outputStream.close();
                socket.close();
                //return;
                //serverSocket.close();

            }
        }

        catch (IOException e) {
            // Output expected UnknownHostExceptions.
            System.out.println("Exception");
          //  throw new IOException();
        }
    }

    }
