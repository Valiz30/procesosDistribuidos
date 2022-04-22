
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListaProcesos extends Thread{
    /**
    * Docs ListaProcesos 
    * @code Actualiza los procesos del cliente, pidiendole respuesta al servidor.
    */
    int TOTAL_PROCESOS = 30;
    boolean[] estadoProcesos;
    String[] procesosCliente;
    String nombreCliente;
    int[] contProcesosCliente;
    Datos datos;
    SimuladorInterfaz sInterfaz;
    /**
    * Docs ListaProcesos 
    * @code Constructor que necesita los datos del cliente, su interfaz y nombre, 
    * que previamente seran enviados al servidor para actualizar la lista de los estados de cada Proceso del cliente
    */
    public ListaProcesos(Datos datos, SimuladorInterfaz sInterfaz, String nombreCliente){
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
            usar = true;
            datos.setNombreCliente(nombreCliente);
            while(datos.getNombreCliente() != nombreCliente && usar == true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListaProcesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                estadoProcesos = sInterfaz.actualizarProceso(datos.getProcesosCliente(nombreCliente), datos.getContProcesosCliente(nombreCliente));
                procesosCliente = datos.getProcesosCliente(nombreCliente);
                for(int i = 0; i < estadoProcesos.length; i++){
                    if(estadoProcesos[i] == true){
                        System.out.println("|El proceso"+procesosCliente[i]+ "ha terminado de ejecutarse") ;
                        // Se elimina el proceso que marque el indice y se acomoda el registro
                        contProcesosCliente = datos.getContProcesosCliente(nombreCliente);
                        for(int j = i; j < contProcesosCliente[0]; j++){//se elimina del registro de procesos del cliente.
                            procesosCliente[j] = procesosCliente[j+1];
                            if(i == (TOTAL_PROCESOS - 1)){
                                procesosCliente[j] = "";
                            }
                        }
                        contProcesosCliente[0]--;
                        break;
                    }
                }
                datos.setProcesosCliente(procesosCliente, nombreCliente);
                datos.setContProcesosCliente(contProcesosCliente, nombreCliente);
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            usar = false;
        }
    }
}

//TERMINADO