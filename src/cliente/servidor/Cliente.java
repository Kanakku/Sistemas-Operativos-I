package cliente.servidor;

import UI_CliSer.UI_Cliente;
import UI_CliSer.UI_Servidor;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Cliente extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private UI_Cliente ventana; 
    private String identificador;
    private boolean test;
    private final String host;
    private final int puerto;
        
    public Cliente(UI_Cliente ventana, String host, Integer puerto, String nombre) {
        this.ventana=ventana;        
        this.host=host;
        this.puerto=puerto;
        this.identificador=nombre;
        test=true;
        this.start();
    }
    
    //Hilo de comunicacion.
    public void run(){
        try {
            socket=new Socket(host, puerto);
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());
            System.out.println("Se establecio comunicacion");
            this.enviarSolicitudConexion(identificador);
            this.conectado();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventana, "ERROR en la conexion");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventana, "ERROR en la conexion");
            System.exit(0);
        }

    }
    
    //Desconecta el socket.
    public void desconectar(){
        try {
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();  
            test=false;
        } catch (Exception e) {
            System.err.println("Error al terminar comunicacion de cliente.");
        }
    }
    
    //Envia mensaje al servidor.
    public void enviarMensaje(String receptor, String mensaje){
        LinkedList<String> lista=new LinkedList<>();
        lista.add("Mensaje");
        lista.add(identificador);
        lista.add(receptor);
        lista.add(mensaje);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje al servidor.");
        }
    }
    
    //Metodo para mantener conexion con el servidor.
    public void conectado() {
        try {
            while (test) {
                Object aux = objectInputStream.readObject();
                if (aux != null) {
                    if (aux instanceof LinkedList) {
                        ejecutar((LinkedList<String>)aux);
                    } else {
                        System.err.println("Se recibio algo desconocido al socket");
                    }
                } else {
                    System.err.println("Se recibio NULL en el socket");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "Se perdio la conexion del servidor");
            System.exit(0);
        }
    }
    
    //Capta un mensaje del servidor y hace la tarea indicada.
    public void ejecutar(LinkedList<String> lista){
        String tipo=lista.get(0);
        switch (tipo) {
            case "Se acepto conexion":
                identificador=lista.get(1);
                ventana.sesionIniciada(identificador);
                for(int i=2;i<lista.size();i++){
                    ventana.addContacto(lista.get(i));
                }
                break;
            case "Usuario conectado":
                ventana.addContacto(lista.get(1));
                break;
            case "Usuario desconectado":
                ventana.eliminarContacto(lista.get(1));
                break;
            case "Mensaje":
                ventana.addMensaje(lista.get(1), lista.get(3));
                break;
            default:
                break;
        }
    }
    
    //Para que el servidor lo agrega a la lista de usuarios/clintes.
    private void enviarSolicitudConexion(String identificador) {
        LinkedList<String> lista=new LinkedList<>();
        lista.add("Entrando conexion");
        lista.add(identificador);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }
    
    //Para eliminar contacto con el servidor y de la lista de clientes.
    public void confirmarDesconexion() {
        LinkedList<String> lista=new LinkedList<>();
        lista.add("Entrando desconexion");
        lista.add(identificador);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje al servidor.");
        }
    }
    
    //Id del cliente.
    String getIdentificador() {
        return identificador;
    }
}
