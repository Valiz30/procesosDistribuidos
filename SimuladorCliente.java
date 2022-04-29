import java.io.*; 
import java.rmi.*;
import java.util.*;

public class SimuladorCliente{
    /*
    * !! - Algunas variables Tipo Contador   CLIENTE
    * Son utilizadas como Arreglo. Esto para tener una variable de
    * Referencia, ya que java no acepta punteros en las variables primitivas, 
    * Por causa de la JVM. Example: int[] contador = {1};
    */
    public static void main(String argv[]) {
            /*
            * Variables utilizadas en el main
            * ejecutar para realizar las operaciones de los procesos
            * prompt como vista de la consola
            */
    
        if(argv.length != 1) { 
            System.out.println("Usage: java SimuladorCliente machineName");
            System.exit(0);
        }
        try {
            Registro[] tablaPaginas = new Registro[100]; //Creacion de la tabla de Paginas con su informacion.
            for(int i = 0; i < 100; i++){
                tablaPaginas[i] = new Registro(0,0,0,0,0);
            }
            int TOTAL_PROCESOS = 30, idCliente = 0; // El numero de procesos permitidos
            String[] listaProcesosDespachar = new String[TOTAL_PROCESOS]; //arreglo que contiene el orden de los procesos a despachar
            Memorias memorias = new Memorias(8,4194304,2097152); // Informacion de la memoria -> tamano de pagina,  virutal y fisica
            int[] contProcesosAct = {0},registroTablaPaginas = new int[TOTAL_PROCESOS]; //contadores
            //contProcesosActuales -> Procesos que tiene el cliente para ejecutar
            //registroTablaPaginas -> Registro que contiene el indice que contiene los procesos a despachar dentro e la tabla de paginas
            Datos datos = new Datos(contProcesosAct, registroTablaPaginas, listaProcesosDespachar, tablaPaginas); //Informacion que contiene el cliente pasandole lo que necesita 
            String nombreCliente = "//" + argv[0] + "/SimuladorServidor"; //Se obtiene el nombre desde el arv[] y se guarda en nombreCliente
          
          
            SimuladorInterfaz sInterfaz = (SimuladorInterfaz) Naming.lookup(nombreCliente);  //con la interfaz SimuladorInterfaz remoto en el registro con lookup
            SimuladorPrompt interfaz = new SimuladorPrompt(datos, nombreCliente, sInterfaz); //Se crea el simuladorPrompt con la informacion y el nombre del cliente
            Ejecutar ejProceso = new Ejecutar(memorias, datos, nombreCliente, sInterfaz); //Se pasa la informacion a la clase ejecutar para la ejecucion de los procesos
            ProcesoNuevo procesoNuevo = new ProcesoNuevo(datos, sInterfaz, nombreCliente); // Pasa la informacion para la actualizacioin de carga y procesosFinalizados con la ayuda de la implementacion SimuladorInterfaz
            ListaProcesos listaProcesos = new ListaProcesos(datos, sInterfaz, nombreCliente); //Pasa la informacion para tener una lista de de estados de los procesos. que seran consultados al servidor
            HiloImprimir hiloImprimir = new HiloImprimir(datos, sInterfaz);
           
            datos.setIdCliente(sInterfaz.registrar(nombreCliente)); //Se asigna al cliente en el registro con los demas clientes.
            // start() inicializa el hilo de cada clase para iniciar
            interfaz.start();
            ejProceso.start();
            procesoNuevo.start();
            listaProcesos.start();
            hiloImprimir.start();
            try{
                interfaz.join();
                ejProceso.join();
                procesoNuevo.join();
                listaProcesos.join();
                hiloImprimir.join();
            }catch(InterruptedException e){;}
            System.exit(0);

        }catch(Exception e) {
            //Capta alguna excepcion y manda el mensaje a la consola
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace(); 
        }
    }
}
//TERMINADO