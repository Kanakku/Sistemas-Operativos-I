package cliente.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class CliHilo extends Thread{
    private final Socket socket;
    private final Server server;
    private String idcliente;
    private boolean conectado;    
    
    //Bloque de memoria para el socket.
    private ObjectOutputStream objectOutputStream; 
    private ObjectInputStream objectInputStream;            
        
    public CliHilo(Socket socket,Server server) {
        this.server=server;
        this.socket = socket;
        
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error en la inicialización del ObjectOutputStream y el ObjectInputStream");
        }
    }
     
    public void desconectar() {
        try {
            socket.close();
            conectado=false;
        } catch (IOException ex) {
            System.err.println("Error al terminar comunicacion con el cliente.");
        }
    }
    
    public void conectado(){        
        conectado=true;
        while(conectado){
            try {
                Object aux=objectInputStream.readObject();
                if(aux instanceof LinkedList){
                    ejecutar((LinkedList<String>)aux);
                }
            } catch (Exception e) {                    
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }

    public void run() {
        try{
            conectado();
        } catch (Exception ex) {
            System.err.println("Error llamar el hilo del cliente.");
        }
        desconectar();
    }
    
    //Lo que recibe el socket para ejecutarlo.
    public void ejecutar(LinkedList<String> lista){
        String tipo=lista.get(0);
        switch (tipo) {
            case "Entrando conexion":
                confirmarConexion(lista.get(1));
                break;
            case "Entrando desconexion":
                confirmarDesConexion();
                break;                
            case "Mensaje":
                String destinatario=lista.get(2);
                server.clientes.stream().filter(h -> (destinatario.equals(h.getIdentificador())))
                    .forEach((h) -> h.enviarMensaje(lista));
                break;
            default:
                break;
        }
    }
    
    //Para enviar el mensaje a travez del socket.
    private void enviarMensaje(LinkedList<String> lista){
        try {
            objectOutputStream.writeObject(lista);            
        } catch (Exception e) {
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }    
    
    //Nuevo cliente conectado, agrega contacto.
    private void confirmarConexion(String identificador) {
        this.idcliente=identificador;
        LinkedList<String> lista=new LinkedList<>();
        lista.add("Se acepto conexion");
        lista.add(this.idcliente);
        lista.addAll(server.getUsuariosConectados());
        enviarMensaje(lista);
        server.agregarLog("\nNuevo cliente: "+this.idcliente);
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("Usuario conectado");
        auxLista.add(this.idcliente);
        server.clientes.stream().forEach(cliente -> cliente.enviarMensaje(auxLista));
        server.clientes.add(this);
    }
    
    //Cliente desconectador, se borra contacto.
    private void confirmarDesConexion() {
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("Usuario desconectado");
        auxLista.add(this.idcliente);
        server.agregarLog("\nEl cliente \""+this.idcliente+"\" se ha desconectado.");
        this.desconectar();
        for(int i=0;i<server.clientes.size();i++){
            if(server.clientes.get(i).equals(this)){
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes.stream().forEach(h -> h.enviarMensaje(auxLista));        
    }
    
    //Id del cliente en el chat
    public String getIdentificador() {
        return idcliente;
    }
    
}
