import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorImpl extends UnicastRemoteObject
   implements SimuladorInterfaz {
      final int TOTAL_CLIENTES = 5, TOTAL_PAQUETES = 30; //variables constantes
      int contClientAct = 0, contPaquetesAct = 0; //contadores
      int[] clientes = new int[TOTAL_CLIENTES]; //registro de clientes en ejecucion
      Paquete[] paquetesPendientes = new Paquete[TOTAL_PAQUETES];// registro de paquetes/procesos que esperan a ser ejecutados
   public SimuladorImpl() throws RemoteException{
      super();

   }
   public int registrar(String name, ){//registra CLIENTE, cuando es creado.
      int indice = 0;
      clientes[indice] = contClientAct; //se añade el indice del cliente al registro
      contClientAct++;
      return clientes[indice];// regreso el identificador del cliente
   }
   public Paquete actualizar(int carga){//los clientes solicitaran procesos, en el caso de que la carga del cliente la soporte, el servidor regresa un proceso.
      Paquete paqueteEnviar;
      return paqueteEnviar;
   }
   public boolean[] actualizarProceso(int[] idProcesos){//verificar si los proceso pendientes del cliente han terminado de ejecutarse
      boolean[] verProceso = new boolean[idProcesos.length];//arreglo que indica si el proceso ya se ejecuto, respetando los indices en el arreglo idProcesos
      return verProceso;
   }
   public void recibir(Paquete paquete){//si un proceso llega a un cliente, le avisa al servidor mediante esta funcion.
      if(contPaquetesAct < 30){ //la cantidad de procesos maxima a recibir de forma general, es de 30.
         paquetesPendientes[contPaquetesAct] = paquete; //guarda el proceso/paquete que el cliente envia
      }else{
         
      }
   }
   public void administrarProceso(){//se encarga de administrar el destino de los procesos a ejecutar

   }
}