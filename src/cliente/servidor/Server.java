package cliente.servidor;
    
    import UI_CliSer.*;
    import java.net.*;
    import java.io.*;
    import java.util.*;
    import javax.swing.*;

public class Server extends Thread{    
    private ServerSocket serverSocket;
    
    //Lista enlazada.
    LinkedList<CliHilo> clientes;
    
    private final UI_Servidor test;
    private final String puerto;
    
    public Server(String puerto, UI_Servidor test) {
        this.puerto=puerto;
        this.test=test;
        clientes=new LinkedList<>();
        this.start();
    }
    
    //Mantiene el servidor abierto.
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.valueOf(puerto));
            test.addServidorIniciado();
            while (true) {
                CliHilo hilo;
                Socket socket;
                socket = serverSocket.accept();
                System.out.println("Nueva conexion: "+socket);
                hilo=new CliHilo(socket, this);               
                hilo.start();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(test, "ERROR al iniciar servidor.");
            System.exit(0);
        }                
    }        
    
    //Lista con los id de todos los clientes.
    LinkedList<String> getUsuariosConectados() {
        LinkedList<String>usuariosConectados=new LinkedList<>();
        clientes.stream().forEach(con -> usuariosConectados.add(con.getIdentificador()));
        return usuariosConectados;
    }
    
    void agregarLog(String texto) {
        test.agregarLog(texto);
    }
}
