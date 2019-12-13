
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class EchoServer {

    public static final int PORT = 5000; // porta al di fuori del range 1-4096 !

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("EchoServer: avviato ");
        System.out.println("Socket del server: " + serverSocket);
        ArrayList<Socket> listaSocket = new ArrayList<>();
        ArrayList<String> user = new ArrayList<>();
        
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // in attesa finchè non avviene una connessione
                System.out.println("EchoServer: connesso: " + clientSocket);
                listaSocket.add(clientSocket);
                
                InputStreamReader stringaIn = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader in = new BufferedReader(stringaIn);
                String nome = in.readLine();
                user.add(nome);
                //System.out.println(user.size());
                
                /*OutputStreamWriter stringaOut = new OutputStreamWriter(clientSocket.getOutputStream());
                BufferedWriter buffer = new BufferedWriter(stringaOut);
                PrintWriter out = new PrintWriter(buffer, true);
                for(int i = 0; i < user.size(); i++){
                    out.println(user.get(i));
                }
                out.println("finish");*/
                
                Runnables r = new Runnables(listaSocket, clientSocket, user, nome);
                Thread t1 = new Thread(r);
                t1.start();

            } catch (IOException e) {
                System.err.println("Ricezione fallita");
                System.exit(1);
            }
        }
    }
}
