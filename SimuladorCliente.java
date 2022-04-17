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
            Memorias memorias = new Memorias(8,4194304,2097152);
            int idCliente = 0; //identificadores
            int contProcesosAct = 0; //contadores
            paquete.proceso = ejecutar.prompt(contProcesosAct,memorias);//añadir nuevo proceso
            paquete.identificador = -1;
            paquete.procesoExiste = false;
            String name = "//" + argv[0] + "/SimuladorServidor";
            SimuladorInterfaz sInterfaz = (SimuladorInterfaz) Naming.lookup(name);
            idCliente = sInterfaz.registrar(name);
            sInterfaz.recibir(paquete);
            sInterfaz.actualizar(carga);
            sInterfaz.administrarProceso();

        }catch(Exception e) {
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}