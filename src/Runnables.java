
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Giacomo Lorenzi
 */
public class Runnables implements Runnable {

    ArrayList<Socket> listaSocket = new ArrayList<>();
    ArrayList<String> user = new ArrayList<>();
    String nome = "";
    Socket mySock;

    Runnables(ArrayList<Socket> listaSocket, Socket clientSocket, ArrayList<String> user, String nome) {
        this.listaSocket = listaSocket;
        this.mySock = clientSocket;
        this.user = user;
        this.nome = nome;
    }

    @Override
    public void run() {
// creazione stream di input da clientSocket:
        InputStreamReader stringaIn;
        OutputStreamWriter stringaOut;
        BufferedWriter buffer;
        PrintWriter out;

        try {
            stringaIn = new InputStreamReader(mySock.getInputStream());
            BufferedReader in = new BufferedReader(stringaIn);
            int cont = 0;
            for(int i = 0; i < listaSocket.size(); i++){
                if (mySock.equals(listaSocket.get(i)))cont = i;
            }
// ciclo di ricezione dal client e invio di risposta
            while (true) {
                String str = in.readLine();
                if (str.equals("                                        " + user.get(cont) + " ha abbandonato la chat ")) {
                    listaSocket.remove(mySock);
                    user.remove(nome);
                    System.out.println("Chiudo la connesione con: " + mySock);
                    break;
                }
                else if (str.equals(user.get(cont) + ": /lista")){
                    for (int i =0; i < user.size(); i++){
                        stringaOut = new OutputStreamWriter(mySock.getOutputStream());
                        buffer = new BufferedWriter(stringaOut);
                        out = new PrintWriter(buffer, true);
                        out.println(i+1 + ")"+user.get(i));
                    }
                }else if(str.equals("                                        " + user.get(cont) + " si è unito alla chat")){
                    for (int i = 0; i < listaSocket.size(); i++) {
                        stringaOut = new OutputStreamWriter(listaSocket.get(i).getOutputStream());
                        buffer = new BufferedWriter(stringaOut);
                        out = new PrintWriter(buffer, true);
                        if(!mySock.equals(listaSocket.get(i)))out.println(str);
                    }
                }else{
                    for (int i = 0; i < listaSocket.size(); i++) {
                        stringaOut = new OutputStreamWriter(listaSocket.get(i).getOutputStream());
                        buffer = new BufferedWriter(stringaOut);
                        out = new PrintWriter(buffer, true);
                        out.println(str);
                    }   
                }
            }
            //listaSocket.remove(mySock);
            //user.remove(nome);
                       
// chiusura di stream e socket
        } catch (IOException ex) {
            System.out.println("EchoServer: chiudo...");
            Logger.getLogger(Runnables.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
