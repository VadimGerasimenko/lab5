package Server;
import java.io.*;
import java.net.Socket;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageSender implements Runnable {
    private Socket s=new Socket();
    private HashMap<String,Socket> clientList= new HashMap();
    private String name;

    JSONParser parser = new JSONParser();
    public MessageSender(){

    }
    public MessageSender(Socket s){
        this.s=s;
    }
    public MessageSender(Socket s, HashMap<String,Socket> clientsList, String name){
        this.s=s;
        this.clientList=clientsList;
        this.name=name;
    }

    public void sendMessage(String message){
        try{
            for(String name:clientList.keySet()) {
                Socket clientSocket=clientList.get(name);
                if(clientSocket!=this.s){
                OutputStream sout = clientSocket.getOutputStream();
                DataOutputStream out = new DataOutputStream(sout);

                out.writeUTF(this.name+": "+message);
                out.flush();
                System.out.println(clientList.size());}
            }
        }
        catch(Exception x) { x.printStackTrace(); }
    }

    public String getMessage(Socket s){
        String line="";
        try{
            InputStream sin = this.s.getInputStream();
            DataInputStream in = new DataInputStream(sin);
            JSONObject object = (JSONObject) parser.parse(in.readUTF());
            //line = in.readUTF();
            System.out.println(object.get("name")+" just sent me this line : " + object.get("message"));
        }
        catch(Exception x) { x.printStackTrace(); }
        return line;
    }
    public String getName(Socket s){
        String name="";
        try{
            InputStream sin = this.s.getInputStream();
            DataInputStream in = new DataInputStream(sin);
            name = in.readUTF();
        }
        catch(Exception x) { x.printStackTrace(); }
        return name;
    }
    public void sendByName(String name){
        try{
            Socket clientSoc= clientList.get(name);
            OutputStream sout = clientSoc.getOutputStream();
            DataOutputStream out = new DataOutputStream(sout);

            out.writeUTF(getMessage(this.s));
            out.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try{
            while(true) {
                String message= getMessage(this.s);
                if(message.equals("Whisper")){
                    sendByName(getName(this.s));
                } else
                {
                    sendMessage(message);
                }
            }
        }
        catch(Exception x) { x.printStackTrace(); }
    }
}