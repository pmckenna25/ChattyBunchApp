package chattybunch.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    
    private static DatagramSocket socket;
    private static boolean running;
    
    private static ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
    private static int ClientId;
    
    public static void start(int port){
        try{
            
            socket = new DatagramSocket(port);
            running = true;
            listen();
            System.out.println("Server Started on Port, "+port);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static void broadcast(String message) throws InterruptedException{
        for(ClientInfo info : clients){
            send(message, info.getAddres(), info.getPort());
            contactList(info.getAddres(), info.getPort());
        }
    }
    
    private static void send(String message, InetAddress address, int port){
        try{
            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent Message To, "+address.getHostAddress()+":"+port);
            
        } catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    private static void listen(){
        Thread listenThread = new Thread("ChatProgram Listener"){
            public void run(){
                try{
                    while(running){
                        
                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);
                        
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));
                        
                        //MANAGE MESSAGE
                        if(!isCommand(message, packet)){
                            broadcast(message);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }   
            }
        }; listenThread.start();
    }
    /**
     * 
     * SERVER COMMAND LIST
     * \con:[name] -> Connects Client To Server
     * \dis:[id] -> Disconnects Client from Server
     */
    private static boolean isCommand(String message, DatagramPacket packet) throws InterruptedException{
        
        if(message.startsWith("\\con:")){
            //RUN CONNECTION CODE
            
            String name = message.substring(message.indexOf(":")+1);
            clients.add(new ClientInfo(name, ClientId++, packet.getAddress(), packet.getPort()));
            broadcast("\\sUser "+name+" has Connected!");
            
            return true;
        }else if(message.startsWith("\\list")){
            return true;
        }else if(message.startsWith("\\disconnect:")){
            
            String name = message.substring(message.indexOf(":")+1);
            for (Iterator<ClientInfo> iterator = clients.iterator(); iterator.hasNext(); ) {
                ClientInfo client = iterator.next();
                if (client.getName().equals(name)) {
                    iterator.remove();
                }
            }
            broadcast("\\sUser "+name+" has Disconnected");
            return true;
        }else if(message.startsWith("\\s")){
            return true;
        }
        
        return false;
    }
    
    public static void stop(){
        running = false;
    }

    private static void contactList(InetAddress address, int port) {
        try{
            String message = "\\list";
            for(ClientInfo client: clients){
                message += ","+client.getName();
            }
            message += "\\e";
            System.out.println(message);
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Contacts sent to, "+address.getHostAddress()+":"+port);
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
