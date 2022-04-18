import java.io.*; 
import java.rmi.*;
import java.util.*;

public class SimuladorCliente{
    public static void main(String argv[]) {
        Ejecutar ejecutar;
        
        if(argv.length != 1) {
            System.out.println("Usage: java SimuladorCliente machineName");
            System.exit(0);
        }
        try {
            Paquete paquete;
            String[] listaProcesosDespachar = new String[30]; //arreglo que contiene el orden de los procesos a despachar
            Memorias memorias = new Memorias(8,4194304,2097152);
            int idCliente = 0; //identificadores
            int[] contProcesosAct = {0}, contadorTabla = {0}; //contadores
            String nombre = "//" + argv[0] + "/SimuladorServidor";
            SimuladorInterfaz sInterfaz = (SimuladorInterfaz) Naming.lookup(nombre);
            sInterfaz.registrar(nombre);
            paquete.proceso = ejecutar.prompt(contProcesosAct, contadorTabla, memorias, listaProcesoDespachar);//añadir nuevo proceso
            paquete.identificador = -1;
            paquete.procesoExiste = false;
            sInterfaz.recibir(paquete);
            
            idCliente = sInterfaz.registrar(nombre);
            
            sInterfaz.actualizar(carga);
            sInterfaz.actualizaProceso(carga);

        }catch(Exception e) {
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}