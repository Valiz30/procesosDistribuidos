
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListaProcesos extends Thread{ // Clase hilo
    /**
    * Docs ListaProcesos  CLIENTE
    * @code Actualiza los procesos del cliente, pidiendole respuesta al servidor.
    */
    int TOTAL_PROCESOS = 30;
    boolean[] estadoProcesos;
    String[] procesosCliente;
    String nombreCliente, nombreHilo = "ListaProcesos";
    int contProcesosCliente = 0;
    Datos datos;
    SimuladorInterfaz sInterfaz;
    /**
    * Docs ListaProcesos 
    * @code Constructor que necesita los datos del cliente, su interfaz y nombre, 
    * que previamente seran enviados al servidor para actualizar la lista de los estados de cada Proceso del cliente
    */
    public ListaProcesos(Datos datos, SimuladorInterfaz sInterfaz, String nombreCliente){ // Constructor, recibe datos, interfaz y nombre del cliente
        this.datos = datos;
        this.sInterfaz = sInterfaz;
        this.nombreCliente = nombreCliente;
    }
    /**
    * run  
    * @code Se utiliza una funcion que actua como hilo para actuar sobre la lista de Procesos
    */
    public void run(){
        boolean usar;
        while(true){
            //System.out.println("ListaProcesos");
            usar = true;
            datos.setNombreHilo(nombreHilo);
            while(datos.getNombreHilo() != nombreHilo && usar == true){
                try {
                    Thread.sleep(1000);
                    datos.setNombreHilo(nombreHilo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListaProcesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListaProcesos.class.getName()).log(Level.SEVERE, null, ex);
                }
                estadoProcesos = sInterfaz.actualizarProceso(datos.getProcesosCliente(), datos.getContProcesosCliente());
                procesosCliente = datos.getProcesosCliente();
                for(int i = 0; i < estadoProcesos.length; i++){
                    //System.out.println("ESTADO PROCESOS: "+estadoProcesos[i]);
                    if(estadoProcesos[i] == true){
                        System.out.println("---------------------------------------------------------------------------------------------------");
                        System.out.println("El proceso"+procesosCliente[i]+ "ha terminado de ejecutarse") ;
                        System.out.println("---------------------------------------------------------------------------------------------------");
                        // Se elimina el proceso que marque el indice y se acomoda el registro
                        contProcesosCliente = datos.getContProcesosCliente();
                        for(int j = i; j < contProcesosCliente; j++){//se elimina del registro de procesos del cliente.
                            if(i == (TOTAL_PROCESOS - 1)){
                                procesosCliente[j] = "";
                                break;
                            }
                            procesosCliente[j] = procesosCliente[j+1];
                        }
                        //System.out.println("CONTADOR PROCESOS: "+contProcesosCliente);
                        contProcesosCliente--;
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
            //System.out.println("Fin ListaProcesos");
            usar = false;
            
        }
    }
}

//TERMINADO