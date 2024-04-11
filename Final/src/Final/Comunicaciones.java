package Final;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Comunicaciones {
    
    private Socket socket;
        
    public Comunicaciones(Socket socket) {
        this.socket = socket;
    }
    
    public Comunicaciones(String hostAddr, int port) throws IOException {
        socket = new Socket(hostAddr, port);
        System.out.println("Socket connected to " + 
                socket.getInetAddress() + 
                ":" + socket.getPort());
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    public void sendMsg(String msg) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(msg);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String receiveMsg() throws ClassNotFoundException {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String line = in.readUTF();
            return(line);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(null);
    }
    
    public void desconectar() throws IOException{
        socket.close();
    }
    
    @Override
    public String toString() {
        return "Conexion desde " + 
                        socket.getInetAddress() + ":" + socket.getPort();
    }
    
    
}
