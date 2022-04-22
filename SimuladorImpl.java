import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorImpl extends UnicastRemoteObject implements SimuladorInterfaz { 
   /**
   * Docs SimuladorImpl 
   * @code Creacion de la clase remota que comunica el objeto remoto
   * Se implementan la interfaz SimuladorInterfaz
   * paquetesPendientes Guarda el proceso/paquete que el cliente envia como proceso listo a ejecuta
   */
   final int TOTAL_CLIENTES = 5, TOTAL_PAQUETES = 30, TOTAL_PROCESOS = 30, UMBRAL = 10; //variables constantes
   int contClientAct = 0, contPaquetesPendientes = 0, contProcesosAct = 0, contProcesosPendientes = 0; //contadores
   int[] procesosRegistrados; //registro de clientes en ejecucion, 
   Paquete[] paquetesPendientes = new Paquete[TOTAL_PAQUETES], paquetesTerminados = new Paquete[TOTAL_PAQUETES];// registro de paquetes/procesos que esperan a ser ejecutados
   Procesos[] procesosExistentes = new Procesos[TOTAL_PROCESOS];
   String[] clientes = new String[TOTAL_CLIENTES]; 
   boolean[] estadoProcesos = new boolean[TOTAL_PROCESOS];
   boolean procesoPermitido = false;

   /**
   * Docs SimuladorImpl 
   * @code Constructor del objeto remoto
   */
   public SimuladorImpl() throws RemoteException{ 
      super();
   }
   /**
   * Docs registrar 
   * @code registra CLIENTE, cuando es creado
   * Se anade el indice del cliente al registro
   */
   public void registrar(String nombre){
      clientes[contClientAct] = nombre; 
      contClientAct++;
   }
   /**
   * Docs eliminarCliente 
   * @code Elimina el cliente del registro
   */
   public void eliminarCliente(String nombre){
      for(int i = 0; i < contClientAct; i++){
         if(clientes[i] == nombre){
            for(int j = i; j < contClientAct; j++){//se elimina del registro de procesos del cliente.
               clientes[j] = clientes[j+1];
               if(i == (TOTAL_CLIENTES - 1)){
                  clientes[j] = "";
               }
            }
         } 
      }
      contClientAct--;
   }
   /**
   * Docs recibir 
   * @code Si un proceso llega a un cliente, le avisa al servidor mediante esta funcion.
   * La cantidad de procesos maxima a recibir de forma general queda en 30, si este
   * no es posible, el procesoPermitido sera negado.
   */
   public boolean recibir(Paquete paquete){//
      
      if(contPaquetesPendientes < 30){ 
         this.paquetesPendientes[contPaquetesPendientes] = paquete; 
         this.contPaquetesPendientes++;
         this.procesosExistentes[contProcesosPendientes] = paquete.proceso;
         this.contProcesosPendientes++;
         this.estadoProcesos[contProcesosAct] = false;
         return this.procesoPermitido = true;
      }else{
         return this.procesoPermitido = false;
      }
   }
   /**
   * Docs actualizar 
   * @code actualiza el paquete dependiendo del umbral del programa.
   * si el umbral se pasa, no se podra enviar el paquete
   * los clientes solicitaran procesos, en el caso de que la carga del cliente la soporte,
   * el servidor regresa un proceso y ademas regresa los proceso que ha finalizado.
   * @return paqueteEnviar - Paquete 
   */
   public Paquete actualizar(int contRefTotales, String[] procesosFinalizados, int[] contProcesosFinalizados){
      Paquete paqueteEnviar = new Paquete(null, false);
      if(contRefTotales < UMBRAL){
         paqueteEnviar = this.paquetesPendientes[this.contPaquetesPendientes];
         paqueteEnviar.procesoExiste = true;
         this.paquetesPendientes[this.contPaquetesPendientes].proceso = null;
         this.paquetesPendientes[this.contPaquetesPendientes].procesoExiste = false;
         this.contPaquetesPendientes--;
         this.procesosExistentes[this.contProcesosPendientes] = null;
         this.contProcesosPendientes--;
      }else{
         paqueteEnviar.proceso = null;
         paqueteEnviar.procesoExiste = false;
      }
      for(int i = 0; i < contProcesosFinalizados[0]; i++){
         for(int j = 0; j < this.contProcesosAct; j++){
            if(procesosFinalizados[i] == procesosExistentes[j].nombre){
               this.estadoProcesos[j] = true;
               break;
            }
         }
         
      }
      return paqueteEnviar;
   }
   /**
   * Docs actualizarProceso 
   * @code verificar si los proceso pendientes del cliente han terminado de ejecutarse
   * verProceso arreglo que indica si el proceso ya se ejecuto, respetando los indices en el arreglo idProcesos
   * @return verProceso - booleann[]
   */
   public boolean[] actualizarProceso(String[] nombreProcesos, int[] contProcesosCliente){
      boolean[] verProceso = new boolean[nombreProcesos.length];//
      for(int i = 0; i < contProcesosCliente[0]; i++){
         for(int j = 0; j < this.contProcesosPendientes; j++){
            if(nombreProcesos[i] == this.procesosExistentes[j].nombre){
               verProceso[i] = this.estadoProcesos[j];
               break;
            }
         }
      }
      return verProceso;
   }
}


/**
* Docs Paquete Servidor
* @code proceso contiene el proceso del paquete
* procesoExiste Bandera para saber si un proceso existe o no
*/
class Paquete {
   Procesos proceso;
   boolean procesoExiste;//banderas
   public Paquete(Procesos proceso, boolean procesoExiste){
      this.proceso = proceso;
      this.procesoExiste = procesoExiste;
   }
}

/**
* Docs Clase Proceso 
* @code Contiene las variables que le dan informacion a un Proceso
* Tambien podemos tomar Procesos como las entradas del usuario
*
* nombre nombre proceso
* totalPaginas  total de paginas que tendra el proceso (diferente al total de invocaciones)
* orden  orden de las invocaciones 
* n_inv numero de invocaciones (solo usada en una funcion)
*/
class Procesos{ 
   String nombre;  
   int totalPaginas; 
   String orden;     
   int n_inv;		
   public Procesos(String nombre, int totalPaginas, String orden, int n_inv){
      this.nombre = nombre;
      this.totalPaginas = totalPaginas;
      this.orden = orden;
      this.n_inv = n_inv;
   }
}

//TERMINADO