import java.io.*; 
import java.rmi.*;
import java.util.*;

public class SimuladorCliente{
    /*
    * !! - Algunas variables Tipo Contador
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
            Registro[] tablaPaginas = new Registro[100];
            int TOTAL_PROCESOS = 30; //identificadores;
            String[] listaProcesosDespachar = new String[TOTAL_PROCESOS]; //arreglo que contiene el orden de los procesos a despachar
            Memorias memorias = new Memorias(8,4194304,2097152);
            int[] contProcesosAct = {0},registroTablaPaginas = new int[TOTAL_PROCESOS]; //contadores
            Datos datos = new Datos(contProcesosAct, registroTablaPaginas, listaProcesosDespachar, tablaPaginas);
            String nombreCliente = "//" + argv[0] + "/SimuladorServidor";
          
          
            SimuladorInterfaz sInterfaz = (SimuladorInterfaz) Naming.lookup(nombreCliente);
            SimuladorPrompt interfaz = new SimuladorPrompt(datos, nombreCliente); //Creacion de Objeto e inicio del hilo
            Ejecutar ejProceso = new Ejecutar(memorias,datos);
            ProcesoNuevo procesoNuevo = new ProcesoNuevo(datos, sInterfaz);
            ListaProcesos listaProcesos = new ListaProcesos(datos, sInterfaz, nombreCliente);

            // start() inicializa el hilo    
            sInterfaz.registrar(nombreCliente);
            interfaz.start();
            ejProceso.start();
            procesoNuevo.start();
            listaProcesos.start();
            sInterfaz.eliminarCliente(nombreCliente);

        }catch(Exception e) {
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}
//TERMINADO