import java.io.*; 
import java.rmi.*;
import java.util.*;

public class SimuladorCliente{
    public static void main(String argv[]) {
        Ejecutar ejecutar;
        SimuladorPrompt prompt;
        if(argv.length != 1) {
            System.out.println("Usage: java SimuladorCliente machineName");
            System.exit(0);
        }
        try {
            Paquete paquete;
            Procesos[] procesosPendientes = new Procesos[30];
            int TOTAL_PROCESOS = 30;
            String[] listaProcesosDespachar = new String[TOTAL_PROCESOS]; //arreglo que contiene el orden de los procesos a despachar
            Memorias memorias = new Memorias(8,4194304,2097152);
            int idCliente = 0; //identificadores
            int[] contProcesosAct = {0}, contadorTabla = {0}; //contadores
            int[] registroTablaPaginas = new int[TOTAL_PROCESOS];
            String nombre = "//" + argv[0] + "/SimuladorServidor";
            SimuladorInterfaz sInterfaz = (SimuladorInterfaz) Naming.lookup(nombre);
            sInterfaz.registrar(nombre);
            SimuladorPrompt interfaz = new SimuladorPrompt(
                contProcesosAct,
                contadorTabla,
                listaProcesosDespachar, 
                tablaPaginas, 
                registroTablaPaginas, 
                sInterfaz
            ); //Creacion de Objeto e inicio del hilo
            interfaz.start();
            Ejecutar ejProceso = new Ejecutar(interfaz, memorias);
            interfaz.setEjProceso(ejProceso);
            ejProceso.start();
            
            idCliente = sInterfaz.registrar(nombre);
            
            sInterfaz.actualizar(carga);
            sInterfaz.actualizaProceso(carga);

        }catch(Exception e) {
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}
public class Paquete {
    int identificador;
    Procesos proceso;
    boolean procesoExiste;//banderas
    public Paquete(int identificador, Procesos proceso, boolean procesoExiste){
        this.identificador = identificador;
        this.proceso = proceso;
        this.procesoExiste = procesoExiste;
    }
}