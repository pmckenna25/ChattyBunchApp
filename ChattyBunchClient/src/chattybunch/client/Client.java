package chattybunch.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private boolean running;
    private String name;
    private static List<String> contacts;

    public Client(String name, String address, int port) {
        
        contacts = new ArrayList<>();
        this.name = name;
        try {
            this.address = InetAddress.getByName(address);
            this.port = port;
            
            socket = new DatagramSocket();
            
            running = true;
            listen();
            send("\\con:"+name);
        } catch (Exception e) {
            System.out.println("Failed to connect 1");
            e.printStackTrace();
        }
    }
    
    public void send(String message){
        try{
            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent Message To, "+address.getHostAddress()+":"+port);
            
        } catch(Exception e){
            System.out.println("Failed to connect 2");
            e.printStackTrace();
        }
        
    }
    private void listen(){
        Thread listenThread = new Thread("ChatProgram Listener"){
            public void run(){
                try{
                    while(running){
                        
                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);
                        
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));
                        
                        if(!isCommand(message, packet)){
                            //PRINT MESSAGE
                            ClientWindow.printToConsole(message);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Failed to connect 3");
                }   
            }
        }; listenThread.start();
    }
    private static boolean isCommand(String message, DatagramPacket packet){
        
        if(message.startsWith("\\con:")){
            //RUN CONNECTION CODE   
            
            return true;
        }else if(message.startsWith("\\list")){
            
            String list = message.substring(5);
            contacts = Arrays.asList(list.split(","));
            ClientWindow.printToContacts(contacts);
            
            return true;
        }else if(message.startsWith("\\s")){
            
            String msg = message.substring(2);
            ClientWindow.printAsServer(msg);
            
            return true;
        }
        
        return false;
    }

    public String getName() {
        return name;
    }

    public static List<String> getContacts() {
        return contacts;
    }

    public void disconnect() {
        try{
            String message = "\\disconnect:"+name+"\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Disconnecting, "+address.getHostAddress()+":"+port);
            
        } catch(Exception e){
            System.out.println("Failed to disconnect");
            e.printStackTrace();
        }
    }
  
}
