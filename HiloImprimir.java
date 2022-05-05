import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloImprimir extends Thread{ // Clase hilo
/**
    * Docs HiloImprimir  CLIENTE
    * @code Actualiza los procesos del cliente, pidiendole respuesta al servidor.
    * nombreHilo - es el nombre del hilo para solicitar el uso de los recursos comparitdos
    * datos - la intancia de la clase Datos que contiene todos los datos del cliente
    * sInterfaz - intancia de la interfaz de la aplicacion
    * cadenaImprimir - es la cadena que tiene que imprimir el cliente en el caso de que se haya realizado una operacion en su proceso
    * cadenaImprimirChar - es la misma cadena que cadenaImprimir, pero en caracteres
    */
    SimuladorInterfaz sInterfaz;
    String nombreHilo = "HiloImprimir", cadenaImprimir = "";
    char[] cadenaImprimirChar;
    Datos datos;
    /**
    * Docs HiloImprimir 
    * @code Constructor
    */
    public HiloImprimir(Datos datos, SimuladorInterfaz sInterfaz){
        this.sInterfaz = sInterfaz;
        this.datos = datos;
    }
    /**
    * run  
    * @code Se utiliza una funcion que actua como hilo para imprimir lo que le corresponde al cliente
    */
    public void run(){
        boolean usar;
        while(true){
            //se establece que el hilo quiere hacer uso de los recursos compartidos
            usar = true;
            datos.setNombreHilo(nombreHilo);
            while(datos.getNombreHilo() != nombreHilo && usar == true){//el hilo se duerme hasta que sea su turno
                try {
                    Thread.sleep(1000);
                    datos.setNombreHilo(nombreHilo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloImprimir.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloImprimir.class.getName()).log(Level.SEVERE, null, ex);
                }
                cadenaImprimir = sInterfaz.actualizarImprimir(datos.getIdCliente());//le pide al cliente las cadenas que tenga pendientes a imprimir respecto a sus procesos
                cadenaImprimirChar = cadenaImprimir.toCharArray(); //obtiene la cadena en un arreglo de caracteres
                if(cadenaImprimir.length() > 1){//si la cadena no esta vacia, imprime el contenido
                    for(int i = 0; i < cadenaImprimirChar.length; i++){//recorre la cadena, caracter por caracter
                        if(cadenaImprimirChar[i] == ','){//si encuentra una coma, quiere decir que debe haber un salto de linea
                            System.out.println("");
                        }else{
                            System.out.print(cadenaImprimirChar[i]);//imprime caracter por caracter
                        }
                    }
                    System.out.println();
                    System.out.print(">");
                }
                cadenaImprimir = "";
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            if(datos.getSalir() == true){
                System.out.println("Terminar HiloImprimir");
                break;
            }
            usar = false;
            
        }
    }
}