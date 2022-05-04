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
            * tablaPaginas - tabla que paginas que se usara en todo el programa (por cliente)
            * TOTAL_PROCESOS - total de procesos que manejara el cliente
            * listaProcesosDespachar - contiene el nombre de los procesos en orden
            * memorias - contiene las memorias que se usaran en todo el programa (por cliente), Informacion de la memoria -> tamano de pagina,  virutal y fisica
            * contProcesosAct - total de procesos a ejecutar por el cliente
            * registroTablaPaginas - contiene los indices que le corresponde a cada proceso en la tabla de paginas
            * datos - instancia de la clase Datos
            */
        if(argv.length != 1) { 
            System.out.println("Usage: java SimuladorCliente machineName");
            System.exit(0);
        }
        try {
            Registro[] tablaPaginas = new Registro[100];
            for(int i = 0; i < 100; i++){
                tablaPaginas[i] = new Registro(0,0,0,0,0);//inicializa la tabla de paginas
            }
            int TOTAL_PROCESOS = 30;
            String[] listaProcesosDespachar = new String[TOTAL_PROCESOS];
            Memorias memorias = new Memorias(8,4194304,2097152); 
            int[] contProcesosAct = {0}, registroTablaPaginas = new int[TOTAL_PROCESOS];
            Datos datos = new Datos(contProcesosAct, registroTablaPaginas, listaProcesosDespachar, tablaPaginas);  
            String nombreCliente = "//" + argv[0] + "/SimuladorServidor"; //Se obtiene el nombre desde el argv[]
          
          
            SimuladorInterfaz sInterfaz = (SimuladorInterfaz) Naming.lookup(nombreCliente);  //con la interfaz SimuladorInterfaz remoto en el registro con lookup
            SimuladorPrompt interfaz = new SimuladorPrompt(datos, sInterfaz); //Se crea una instancia del hilo que hara de interfaz para el usuario
            Ejecutar ejProceso = new Ejecutar(memorias, datos, sInterfaz); //Se crea una instancia del hilo que ejecutara los procesos
            ProcesoNuevo procesoNuevo = new ProcesoNuevo(datos, sInterfaz); // Se crea una instancia del hilo que solicitara procesos nuevo al servidor, ademas de mandar los que ya termino de ejecutar
            ListaProcesos listaProcesos = new ListaProcesos(datos, sInterfaz); //Se crea una instancia del hilo que pedira una actualiacion del estado de sus procesos
            HiloImprimir hiloImprimir = new HiloImprimir(datos, sInterfaz); // Se cre una instancia del hilo que imprimira las operaciones que se realizan sobre sus propios procesos
           
            datos.setIdCliente(sInterfaz.registrar()); //Se registra el cliente en el servidor
            System.out.println("idCliente: "+datos.getIdCliente());//Imprime el identificador del Hilo
            // start() inicia la ejecucion de cada uno de los hilos
            interfaz.start();
            ejProceso.start();
            procesoNuevo.start();
            listaProcesos.start();
            hiloImprimir.start();
            try{// join() espera a que terminen de ejecutarse todos lo hilos
                interfaz.join();
                ejProceso.join();
                procesoNuevo.join();
                listaProcesos.join();
                hiloImprimir.join();
            }catch(InterruptedException e){;}
            System.exit(0);//sale del programa

        }catch(Exception e) {
            //Capta alguna excepcion y manda el mensaje a la consola
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace(); 
        }
    }
}