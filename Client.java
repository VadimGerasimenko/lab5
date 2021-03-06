package Client;
import java.io.*;
import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Client{


    public static void main(String[] ar) {
        int serverPort = 6666;
        String address = "127.0.0.1";
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            Socket socket = new Socket(ipAddress, serverPort);
            System.out.println("Yes! I just got hold of the program.");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String name = null;
            System.out.println("Enter your name: ");
            name = keyboard.readLine();
            out.writeUTF(name);
            out.flush();
            System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
            System.out.println();
            Listener listener = new Listener(socket);
            listener.start();
            String line;
            while (true) {
                JSONObject object = new JSONObject();
                line = keyboard.readLine();
                if (line.equals("Whisper"))
                {
                    out.writeUTF(line);
                    out.flush();
                    System.out.println("Who do you wanna send a message?");
                    line=keyboard.readLine();
                    out.writeUTF(line);
                    out.flush();
                    System.out.println("Type your message: ");
                    line=keyboard.readLine();
                    out.writeUTF(line);
                    out.flush();
                }
                else {
                    object.put("name", name);
                    object.put("message", line);

                    out.writeUTF(object.toJSONString());
                    out.flush();

                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

    }

}
