package Final;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Servidor extends Thread {

    public static void main(String[] args) throws IOException{
        Servidor server = new Servidor(12000);
        server.start();
    }

    final int port;
    final List<ClientThread> clients = new LinkedList<>();

    public Servidor(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Started server on port " + port);
            Socket clientSocket = serverSocket.accept();
            Socket clientSocket2 = serverSocket.accept();
           
            ClientThread clientThread = new ClientThread(clients, new Comunicaciones(clientSocket));
            clientThread.start();
            
            ClientThread clientThread2 = new ClientThread(clients, new Comunicaciones(clientSocket2));
            clientThread2.start();
            
            while (true) {   
                clientSocket = serverSocket.accept();
                clientSocket2 = serverSocket.accept();
                clientThread = new ClientThread(clients, new Comunicaciones(clientSocket));
                clientThread.start();
                clientThread2 = new ClientThread(clients, new Comunicaciones(clientSocket2));
                clientThread2.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class ClientThread extends Thread {

        final List<ClientThread> clients;
        final Comunicaciones con;

        public ClientThread(List<ClientThread> clients, Comunicaciones con) {
            this.clients = clients;
            this.con = con;
        }

        synchronized public void sendMsg(String msg) {
            con.sendMsg(msg);
        }

        @Override
        public void run() {
            try {
                System.out.println(con);

                synchronized (clients) {
                    clients.add(this);
                }
                
                    while (true) {
                        String line = con.receiveMsg();
                        if (line.equals("exit")){
                            this.sendMsg(line);
                            break;
                        }
                        
                        String fragmentacion[] = line.split(" ");
                            for (int i = 0; i < clients.size(); i++){
                                if (this == clients.get(i) && (i%2 == 0))
                                    if (fragmentacion[0].equals("Nombre")){ 
                                        clients.get(i+1).sendMsg("Nombre1 " + fragmentacion[1]);
                                    }
                                    else if (fragmentacion[0].equals("Mensaje")){
                                        clients.get(i+1).sendMsg(line);
                                    }
                                    else if (fragmentacion[0].equals("Boton")){
                                        clients.get(i+1).sendMsg(line);
                                    }
                                    else if (fragmentacion[0].equals("Gana")){
                                        clients.get(i+1).sendMsg(line);
                                        guardarGanadorFichero(fragmentacion[1]);
                                    }
                                    else if (fragmentacion[0].equals("Empate")){
                                        clients.get(i+1).sendMsg(line);
                                    }
                                    else if (line.equals("Revancha")){
                                        clients.get(i+1).sendMsg(line);
                                    }
                                    else if (line.equals("Revancha2")){
                                        clients.get(i+1).sendMsg(line);
                                    }
                                    else{
                                        this.sendMsg(line);
                                    }
                            
                                else if(this == clients.get(i) && (i%2 == 1)){
                                    if (fragmentacion[0].equals("Nombre")){
                                        clients.get(i-1).sendMsg("Nombre2 " + fragmentacion[1]);
                                    }
                                    else if (fragmentacion[0].equals("Mensaje")){
                                        clients.get(i-1).sendMsg(line);
                                    }
                                    else if (fragmentacion[0].equals("Boton")){
                                        clients.get(i-1).sendMsg(line);
                                    }
                                    else if (fragmentacion[0].equals("Gana")){
                                        clients.get(i-1).sendMsg(line);
                                        guardarGanadorFichero(fragmentacion[1]);
                                    }
                                    else if (fragmentacion[0].equals("Empate")){
                                        clients.get(i-1).sendMsg(line);
                                    }else if (line.equals("Revancha")){
                                        clients.get(i-1).sendMsg(line);
                                    }
                                    else if (line.equals("Revancha2")){
                                        clients.get(i-1).sendMsg(line);
                                    }
                                    else{
                                        this.sendMsg(line);
                                    }
                                }
                            }                            
                    }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    con.desconectar();
                } catch (Exception ex) {
                }
            }
        }

    }
    public void guardarGanadorFichero(String ganador) throws FileNotFoundException, IOException{
        FileWriter fichero = null;
        PrintWriter pw = null;
        File file = null;
        FileReader fileReader= null;
        BufferedReader bufferedReader = null;
        boolean repetido = false;
        String texto = "";
        try
        {
            file = new File("./ganadores.txt");
            if (file.exists()){
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String line = "";
                String fragmentacion[];
               
                
                while ((line = bufferedReader.readLine()) != null){
                    fragmentacion = line.split(" ");
                    if (fragmentacion[0].equals(ganador)){
                        texto += (ganador + " " + ((Integer.parseInt(fragmentacion[1])) + 1)) + "\n";
                        repetido = true;
                    }
                    else{
                        texto+= line + "\n";
                    }
                }
            }
            
            if (!repetido){
                texto+= (ganador + " 1 \n");
            }
            
            file = new File ("./ganadores.txt");
            fichero = new FileWriter(file);
            pw = new PrintWriter(fichero);
            
            pw.print(texto);
            
            


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           if (null != fileReader)
               fileReader.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        
        
    }


}
