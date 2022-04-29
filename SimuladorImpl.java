import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorImpl extends UnicastRemoteObject implements SimuladorInterfaz { 
   /**
   * Docs SimuladorImpl SERVIDOR
   * @code Creacion de la clase remota que comunica el objeto remoto
   * Se implementan la interfaz SimuladorInterfaz
   * paquetesPendientes Guarda el proceso/paquete que el cliente envia como proceso listo a ejecuta
   */
   final int TOTAL_CLIENTES = 5, TOTAL_PAQUETES = 30, TOTAL_PROCESOS = 30, UMBRAL = 20; //variables constantes
   int contClientAct = 0, contPaquetesPendientes = 0, contProcesosAct = 0, contProcesosPendientes = 0, contProcFinalizados = 0; //contadores
   int[] procesosRegistrados; //registro de clientes en ejecucion, 
   Paquete[] paquetesPendientes = new Paquete[TOTAL_PAQUETES], paquetesTerminados = new Paquete[TOTAL_PAQUETES];// registro de paquetes/procesos que esperan a ser ejecutados
   Procesos[] procesosExistentes = new Procesos[TOTAL_PROCESOS];
   Procesos procesoNull = new Procesos("nombre", 0, "orden", 0);
   String[] clientes = new String[TOTAL_CLIENTES], aux = new String[30], procFinalizadosNombre = new String[TOTAL_PROCESOS]; 
   String cadenaNull = "";
   boolean[] estadoProcesos = new boolean[TOTAL_PROCESOS];
   boolean procesoPermitido = false;
   RegistroClientes[] registroProcesosClientes = new RegistroClientes[5];
   /**
   * Docs SimuladorImpl 
   * @code Constructor del objeto remoto
   */
   public SimuladorImpl() throws RemoteException{ 
      super();
      for(int i = 0; i < 30; i++){
         paquetesPendientes[i] = new Paquete(procesoNull, false);
         paquetesTerminados[i] = new Paquete(procesoNull, false);
         procesosExistentes[i] = procesoNull;
         estadoProcesos[i] = false;
         aux[i] = "";
      }
      for(int i = 0; i < 5; i++){
         clientes[i] = "";
         registroProcesosClientes[i] = new RegistroClientes(i, aux, cadenaNull);
      }
   }
   /**
   * Docs registrar 
   * @code registra CLIENTE, cuando es creado
   * Se anade el indice del cliente al registro
   * al mismo tiempo se incrementa un contador de clientes actuales
   */
   public synchronized int registrar(String nombre){ // Recibe el nombre del cliente 
      int idCliente;
      clientes[contClientAct] = nombre; // Registro de nombre de clientes
      idCliente = contClientAct;
      contClientAct++; // Contador de clientes actuales
      return idCliente;
   }
   /**
   * Docs eliminarCliente 
   * @code Elimina el cliente del registro
   */
   public synchronized void eliminarCliente(String nombre){ // recibe el nombre del cliente a eliminar del registro
      for(int i = 0; i < contClientAct; i++){ // Busqueda linea para encontrar el cliente a eliminar
         if(clientes[i].equals(nombre)){ // Se compara el nombre a encontrar con los del registro
            for(int j = i; j < contClientAct; j++){//se elimina del registro de procesos del cliente.
               clientes[j] = clientes[j+1]; // Se mueven los registros anteriores al eliminado para no dejar espacios del registro sin datos
               if(i == (TOTAL_CLIENTES - 1)){ // Se eliminar los registros repetidos
                  clientes[j] = ""; // Se cambia por null
               }
            }
         } 
      }
      //System.out.println("         CLIENTE ELIMINADO");
      contClientAct--; // Se decrementa el valor del contador de clientes actuales
   }
   /**
   * Docs recibir 
   * @code Si un proceso llega a un cliente, le avisa al servidor mediante esta funcion.
   * La cantidad de procesos maxima a recibir de forma general queda en 30, si este
   * no es posible, el procesoPermitido sera negado.
   */
   public synchronized boolean recibir(Paquete paquete, int idCliente){// Recibe como entrata un 
      int contadorProcesos;
      if(contPaquetesPendientes < 30){ // 30 es el numero maximo de paquetes
         paquetesPendientes[contPaquetesPendientes] = paquete; // Se agrega un nuevo paquete pendiente
         contPaquetesPendientes++; // Incrementa el contador de paquetes pendientes
         procesosExistentes[contProcesosPendientes] = paquete.getProceso(); // Se agrega al registro de procesos existentes
         System.out.println("---Nombre proceso: "+this.procesosExistentes[contProcesosPendientes].nombre);
         System.out.println("---Total pag proceso: "+this.procesosExistentes[contProcesosPendientes].totalPaginas);
         System.out.println("---Orden proceso: "+this.procesosExistentes[contProcesosPendientes].orden);
         System.out.println("---#Invocaciones proceso: "+this.procesosExistentes[contProcesosPendientes].n_inv);
         procFinalizadosNombre[contProcFinalizados] = paquete.getProceso().getNombre();
         estadoProcesos[contProcFinalizados] = false; 
         contProcFinalizados++;
         contProcesosPendientes++; // Se incrementa el contador de procesos pendientes
         // Se coloca como falso el estado del proceso hasta que se procese 
         contadorProcesos = registroProcesosClientes[idCliente].getContadorProcesos();
         registroProcesosClientes[idCliente].getProcesos()[contadorProcesos] = paquete.getProceso().nombre;
         contadorProcesos++;
         registroProcesosClientes[idCliente].setContadorProcesos(contadorProcesos);
         return procesoPermitido = true; // mientras el contador de paquetes pendientes sea menor a 30 se permitira anadir mas procesos
      }else{
         return procesoPermitido = false; // Sino se cumple, no se permite
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
   public synchronized Paquete actualizar(int contRefTotales, String[] procesosFinalizados, int[] contProcesosFinalizados){ // ReciBe contador de referencias totales, un registro de procesos finalizados, y un arreglo de contadores de los procesos finalizados
      System.out.println("Entra a actualizar");
      Paquete paqueteEnviar = new Paquete(procesoNull,false); // Se crea una instacia de la clase Paquete 
      if(contProcesosFinalizados[0] > 0){
         for(int i = 0; i < contProcesosFinalizados[0]; i++){ // Ciclo for anidado que busca y compara el registro de procesos finalizados
            System.out.println("procesosFinalizados: "+procesosFinalizados[i]);
            // con el registro de procesos existentes y si estos coinciden el estado del proceso se establece como verdadero (se rompe el ciclo)
            for(int j = 0; j < contProcFinalizados ; j++){
               System.out.println("procesosFinalizadosServidor: "+procFinalizadosNombre[i]);
               if(procesosFinalizados[i].equals(procFinalizadosNombre[j])){
                  estadoProcesos[j] = true;
                  break;
               }
            }
            
         }
      }
      if(contRefTotales < UMBRAL && contPaquetesPendientes > 0){ // Entra si el contador de referncias totales es menor al Umbral
         paqueteEnviar.setProceso(paquetesPendientes[contPaquetesPendientes-1].getProceso()) ; // Se establece el paquete a enviar desde el registro de paquetes pendientes
         paqueteEnviar.setProcesoExiste(true);// Se estable como verdadero la variable proceso existente 
         System.out.println("Actualizar---Nombre proceso: "+paqueteEnviar.getProceso().getNombre());
         System.out.println("---Total pag proceso: "+paqueteEnviar.proceso.totalPaginas);
         System.out.println("---Orden proceso: "+paqueteEnviar.proceso.orden);
         System.out.println("---#Invocaciones proceso: "+paqueteEnviar.proceso.n_inv);
         paquetesPendientes[contPaquetesPendientes-1].setProceso(procesoNull); // Se borra el proceso del registro paquetes pendientes
         paquetesPendientes[contPaquetesPendientes-1].setProcesoExiste(false); // Se establece como falso el estado del prceso existente 
         contPaquetesPendientes--; // Se decrementa el contador paquetes pendientes
         procesosExistentes[contProcesosPendientes-1] = procesoNull; // Se establece en el registro procesos existentes null del contador de procesos pendientes actual
         contProcesosPendientes--; // se decrementa  el contador de paqutes pendientes
         return paqueteEnviar;
      }else{
         return paqueteEnviar;
      }
   }
   /**
   * Docs actualizarProceso 
   * @code verificar si los proceso pendientes del cliente han terminado de ejecutarse
   * verProceso arreglo que indica si el proceso ya se ejecuto, respetando los indices en el arreglo idProcesos
   * @return verProceso - booleann[]
   */
   public synchronized boolean[] actualizarProceso(String[] nombreProcesos, int contProcesosCliente){ // Recibe el registro de los nombres de los procesos
   // y el arreglo de contadores de procesos del cliente
      boolean[] verProceso; 
      System.out.println("ACTUALIZAR PROCESO ------- CONTADOR PROCESOS CLIENTE: "+contProcesosCliente);
      if(contProcesosCliente > 0){
         System.out.println("SERVER- El cliente tiene mas de un proceso pendiente");
         System.out.println("CONTADOR PROCESOS PENDIENTES: "+contProcesosCliente);
         verProceso = new boolean[nombreProcesos.length];
         for(int i = 0; i < contProcesosCliente; i++){ // Se establece el nuevo estado de cada proceso si este ha finalizado de ser procesado 
            for(int j = 0; j < contProcFinalizados; j++){ 
               if(nombreProcesos[i].equals(procFinalizadosNombre[j])){ // compara y busca el nombre del proceso en el registro de procesos existentes 
                  System.out.println("NOMBRE PROCESO PENDIENTE: "+nombreProcesos[i]);
                  System.out.println("Y ESTADO EN EL SERVER: "+estadoProcesos[j]);
                  // si lo encuentra establece el estado del arreglo estado de procesos a ver proceso y rompe el ciclo
                  verProceso[i] = estadoProcesos[j];
                  if(estadoProcesos[j] == true){
                     for(int k = j; k < contProcFinalizados; k++){
                        if(k == TOTAL_PROCESOS-1){
                           estadoProcesos[k] = false;
                           procFinalizadosNombre[k] = "";
                           break;
                        }
                        estadoProcesos[k] = estadoProcesos[k+1];
                        procFinalizadosNombre[k] = procFinalizadosNombre[k+1];
                     }
                     contProcFinalizados--;
                  }
                  break;
               }
            }
         }
         return verProceso; // retorna un valor booleano para el proceso de entrada
      }else{
         System.out.println("SERVER- El cliente no tiene mas de un proceso pendiente");
         verProceso = new boolean[1];
         verProceso[0] = false;
         return verProceso;
      }
      
      
   }
   public synchronized String actualizarImprimir(int idCliente){
      String imprimirFinal = "";
      imprimirFinal = registroProcesosClientes[idCliente].getCadena();
      registroProcesosClientes[idCliente].setCadena("");
      return imprimirFinal;
   }
   public synchronized void imprimir(String cadenaPendiente, String proceso){
      String[] aux;
      for(int i = 0; i < TOTAL_CLIENTES; i++){
         aux = registroProcesosClientes[i].getProcesos();
         for(int j = 0; j < registroProcesosClientes[i].getContadorProcesos(); j++){
            if(aux[j].equals(proceso)){
               registroProcesosClientes[i].setCadena(registroProcesosClientes[i].getCadena()+cadenaPendiente); //cambier el arreglo a un solo string y separar las lineas con una coma
            }
         }
      }
   }
}
class RegistroClientes{
   int idCliente, contadorProcesos;
   String[] procesos;
   String cadena;
   public RegistroClientes(int idCliente, String[] procesos, String cadena){
       this.idCliente = idCliente;
       this.procesos = procesos;
       this.cadena = cadena;
       contadorProcesos = 0;
   }
   public int getContadorProcesos(){
      return this.contadorProcesos;
   }
   public int getIdCliente(){
       return this.idCliente;
   }
   public String[] getProcesos(){
       return this.procesos;
   }
   public String getCadena(){
      return this.cadena;
   }
   public void setContadorProcesos(int contadorProcesos){
      this.contadorProcesos = contadorProcesos;
   }
   public void setIdCliente(int idCliente){
      this.idCliente = idCliente;
   }
   public void setProcesos(String[] procesos){
      this.procesos = procesos;
   }
   public void setCadena(String cadena){
      this.cadena = cadena;
   }
}
//TERMINADO