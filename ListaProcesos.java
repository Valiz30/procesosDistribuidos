
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListaProcesos extends Thread{ // Clase hilo
    /**
    * Docs ListaProcesos  CLIENTE
    * @code Actualiza los procesos del cliente, pidiendole respuesta al servidor.
    * estadoProcesos - variable que va a contener el estado que el servirdor retorne de los procesos
    * procesosCLiente - contiene los nombre de los procesos del cliente
    * nombreHilo - es el nombre del hilo para solicitar el uso de los recursos comparitdos
    * contProcesosCliente - va a almacenar el total de procesos del cliente
    * datos - la intancia de la clase Datos que contiene todos los datos del cliente
    * sInterfaz - intancia de la interfaz de la aplicacion
    */
    int TOTAL_PROCESOS = 30;
    boolean[] estadoProcesos;
    String[] procesosCliente;
    String nombreHilo = "ListaProcesos";
    int contProcesosCliente = 0;
    Datos datos;
    SimuladorInterfaz sInterfaz;
    /**
    * Docs ListaProcesos 
    * @code Constructor
    */
    public ListaProcesos(Datos datos, SimuladorInterfaz sInterfaz){ // Constructor, recibe datos, interfaz y nombre del cliente
        this.datos = datos;
        this.sInterfaz = sInterfaz;
    }
    /**
    * run  
    * @code funcion que pone en ejecucion el hilo 
    */
    public void run(){
        boolean usar;
        while(true){
            //establece que quiere usar los recursos compartidos
            usar = true;
            datos.setNombreHilo(nombreHilo);
            while(datos.getNombreHilo() != nombreHilo && usar == true){//se duerme si no es su turno
                try {
                    Thread.sleep(1000);
                    datos.setNombreHilo(nombreHilo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListaProcesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListaProcesos.class.getName()).log(Level.SEVERE, null, ex);
                }
                estadoProcesos = sInterfaz.actualizarProceso(datos.getProcesosCliente(), datos.getContProcesosCliente());//le solicita al servidor el estado de los procesos del cliente
                procesosCliente = datos.getProcesosCliente();//obtiene la lista de procesos del cliente
                for(int i = 0; i < estadoProcesos.length; i++){//recorre la lista de estados
                    if(estadoProcesos[i] == true){//si encuentra que un proceso ha terminado de ejecutarse se lo indica al usuario
                        // Se elimina el proceso que marque el indice y se acomoda el registro
                        contProcesosCliente = datos.getContProcesosCliente();//obtiene el total de procesos del cliente
                        for(int j = i; j < contProcesosCliente; j++){//se elimina del registro de procesos del cliente.
                            if(i == (TOTAL_PROCESOS - 1)){
                                procesosCliente[j] = "";
                                break;
                            }
                            procesosCliente[j] = procesosCliente[j+1];
                        }
                        contProcesosCliente--;//decrementa los procesos del cliente
                        datos.setProcesosCliente(procesosCliente);
                        datos.setContProcesosCliente(contProcesosCliente);
                        break;
                    }
                }
                
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            if(datos.getSalir() == true){
                System.out.println("Terminar ListaProcesos");
                break;
            }
            usar = false;
            
        }
    }
}