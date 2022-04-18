import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//hola amigos como estan! si verdad!
public class SimuladorImpl extends UnicastRemoteObject
   implements SimuladorInterfaz {
      final int TOTAL_CLIENTES = 5, TOTAL_PAQUETES = 30, TOTAL_PROCESOS = 30; //variables constantes
      int contClientAct = 0, contPaquetesAct = 0; //contadores
      int[] clientes = new int[TOTAL_CLIENTES], procesosRegistrados; //registro de clientes en ejecucion, 
      Paquete[] paquetesPendientes = new Paquete[TOTAL_PAQUETES];// registro de paquetes/procesos que esperan a ser ejecutados
      Procesos[] procesosExistentes = new Procesos[TOTAL_PROCESOS];

   public SimuladorImpl() throws RemoteException{
      super();

   }
   public int registrar(String nombre){//registra CLIENTE, cuando es creado.
      int indice = 0;
     // clientes[indice] = contClientAct; //se añade el indice del cliente al registro
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
         paquetesPendientes[contPaquetesAct] = paquete; //guarda el proceso/paquete que el cliente envia como proceso listo a ejecutar
         procesosExistentes[contPaquetesAct] = paquete.proceso;
      }else{
         
      }
   }
   public void enviarProcesoTerminado(Procesos procesoTerminado){

   }
   public void administrarProceso(){//se encarga de administrar el destino de los procesos a ejecutar

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
class Procesos{ /*Entradas de usuario*/
   String nombre;  //nombre proceso
   int totalPaginas;  //total de paginas que tendra el proceso (diferente al total de invocaciones)
   String orden;    // orden de las invocaciones 
   int n_inv;		//numero de invocaciones (solo usada en una funcion)
   public Procesos(String nombre, int totalPaginas, String orden, int n_inv){
       this.nombre = nombre;
       this.totalPaginas = totalPaginas;
       this.orden = orden;
       this.n_inv = n_inv;
   }
}