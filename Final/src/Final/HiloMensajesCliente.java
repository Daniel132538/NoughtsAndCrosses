/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Final;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danim
 */
public class HiloMensajesCliente extends Thread{
    private Comunicaciones con;
    private Juego juego;
    public HiloMensajesCliente(Comunicaciones con, Juego juego){
        this.con = con;
        this.juego = juego;              
    }
    @Override
    public void run(){
        String recv = null;
        
        
        for(;;){
            try {  
                recv = con.receiveMsg();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] fragmentacion = recv.split(" ");
            if (fragmentacion[0].equals("Mensaje")){
                juego.escribirChat(recv.replace("Mensaje", ""));
            }
            else if(fragmentacion[0].equals("Boton")){
                juego.presionar(Integer.parseInt(fragmentacion[1]));
                juego.setTuturno(true);
            }
            else if (recv.equals("exit")){
                break;
            }    
            else if (fragmentacion[0].equals("Gana") || (recv.equals("Empate"))){
                juego.declararGanador(recv);
                juego.setTuturno(false);
            } 
            else if (recv.equals("Revancha")){
                juego.JLabel6SetText();
            } 
            else if (recv.equals("Revancha2")){
                juego.setTuturno(true);
                juego.reiniciar();
                
            }
            
        }
    }      
}
