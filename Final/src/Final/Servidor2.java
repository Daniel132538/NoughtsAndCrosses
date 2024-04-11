package Final;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Servidor2 extends Thread {

    public static void main(String[] args) {
        Servidor2 server = new Servidor2(12001);
        server.start();
    }

    final int port;
    final List<ClientThread> clients = new LinkedList<>();

    public Servidor2(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Started server on port " + port);
            while (!interrupted()) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clients,
                        new Comunicaciones(clientSocket));
                clientThread.start();
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

                for (String line; (line = con.receiveMsg()) != null;) {
                    if (line.equals("exit")){
                        this.sendMsg(line);
                        break;
                    }
                    else if (line.equals("Puntuaciones")){
                        this.sendMsg(leerFichero());
                        break;
                    }
                    else{
                        this.sendMsg(line);
                    }
                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    con.desconectar();
                } catch (Exception ex) {
                }
                synchronized (clients) {
                    clients.remove(this);
                }
            }
        }

    }
    private String leerFichero(){
        File file = null;
        FileReader fileReader= null;
        BufferedReader bufferedReader = null;
        String texto = "";
        try
        {
            file = new File("./ganadores.txt");
            if (file.exists()){
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    texto+= line + "\n";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return texto;
    }

}
