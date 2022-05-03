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
   int[] idCliente = new int[5], contadorProcesosCliente = new int[5];
   int contClientAct = 0, contPaquetesPendientes = 0, contProcFinalizados = 0; //contadores
   Paquete[] paquetesPendientes = new Paquete[TOTAL_PAQUETES];// registro de paquetes/procesos que esperan a ser ejecutados
   Procesos procesoNull = new Procesos("nombre", 0, "orden", 0);
   String[] procFinalizadosNombre = new String[TOTAL_PROCESOS],cadenaCliente = new String[5]; 
   boolean[] estadoProcesos = new boolean[TOTAL_PROCESOS], estadoClientes = new boolean[TOTAL_CLIENTES];
   boolean procesoPermitido = false;
   List<List<String>> procesosClientes = new ArrayList();
   /**
   * Docs SimuladorImpl 
   * @code Constructor del objeto remoto
   */
   public SimuladorImpl() throws RemoteException{ 
      super();
      for(int i = 0; i < 30; i++){
         paquetesPendientes[i] = new Paquete(procesoNull, false);
         procFinalizadosNombre[i] = ""; 
         estadoProcesos[i] = false;
      }
      for(int i = 0; i < TOTAL_CLIENTES; i++){
         idCliente[i] = i;
         contadorProcesosCliente[i] = 0;
         procesosClientes.add(new ArrayList());
         cadenaCliente[i] = "";
         estadoClientes[i] = false;
      }
   }
   /**
   * Docs registrar 
   * @code registra CLIENTE, cuando es creado
   * Se anade el indice del cliente al registro
   * al mismo tiempo se incrementa un contador de clientes actuales
   */
   public synchronized int registrar(){ // Recibe el nombre del cliente 
      int idCliente = 0;
      for(int i = 0; i < TOTAL_CLIENTES; i++){
         if(estadoClientes[i] == false){
            idCliente = i;
            estadoClientes[i] = true;
            break;
         }
      }
      contClientAct++; // Contador de clientes actuales
      return idCliente;
   }
   /**
   * Docs eliminarCliente 
   * @code Elimina el cliente del registro
   */
   public synchronized void eliminarCliente(int idCliente){ // recibe el nombre del cliente a eliminar del registro
      estadoClientes[idCliente] = false;
      contClientAct--; // Se decrementa el valor del contador de clientes actuales
   }

   public synchronized boolean verificarProceso(String proceso){ // recibe el nombre del cliente a eliminar del registro
      boolean permitido = true;
      for(int i = 0; i < contPaquetesPendientes; i++){
         if(paquetesPendientes[i].getProceso().getNombre().equals(proceso)){
            permitido = false;
            break;
         }
      }
      return permitido;
   }
   /**
   * Docs recibir 
   * @code Si un proceso llega a un cliente, le avisa al servidor mediante esta funcion.
   * La cantidad de procesos maxima a recibir de forma general queda en 30, si este
   * no es posible, el procesoPermitido sera negado.
   */
   public synchronized boolean recibir(Paquete paquete, int idCliente){// Recibe como entrata un 
      List<String> procesosAux = new ArrayList();
      for(int i = 0; i < 30; i++){
         procesosAux.add(i,"");
      }
      if(contPaquetesPendientes < 30){ // 30 es el numero maximo de paquetes
         paquetesPendientes[contPaquetesPendientes] = paquete; // Se agrega un nuevo paquete pendiente
         contPaquetesPendientes++; // Incrementa el contador de paquetes pendientes
         procFinalizadosNombre[contProcFinalizados] = paquete.getProceso().nombre;
         estadoProcesos[contProcFinalizados] = false; 
         // Se coloca como falso el estado del proceso hasta que se procese 
         for(int i = 0; i < contClientAct; i++){
            if(i == idCliente){
               procesosAux = procesosClientes.get(i);
               procesosAux.add(procFinalizadosNombre[contProcFinalizados]);
               procesosClientes.add(i, procesosAux);
               procesosClientes.remove(i++);
               i--;
               contadorProcesosCliente[i]++;
               break;
            }
         }
         contProcFinalizados++;
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
      //System.out.println("Entra a actualizar");
      Paquete paqueteEnviar = new Paquete(procesoNull,false); // Se crea una instacia de la clase Paquete 
      if(contProcesosFinalizados[0] > 0){
         for(int i = 0; i < contProcesosFinalizados[0]; i++){ // Ciclo for anidado que busca y compara el registro de procesos finalizados
            //System.out.println("procesosFinalizados: "+procesosFinalizados[i]);
            // con el registro de procesos existentes y si estos coinciden el estado del proceso se establece como verdadero (se rompe el ciclo)
            for(int j = 0; j < contProcFinalizados ; j++){
               //System.out.println("procesosFinalizadosServidor: "+procFinalizadosNombre[i]);
               if(procesosFinalizados[i].equals(procFinalizadosNombre[j])){
                  //System.out.println("Entra a colocar true al estado");
                  estadoProcesos[j] = true;
                  break;
               }
            }
            
         }
      }
      if(contRefTotales < UMBRAL && contPaquetesPendientes > 0){ // Entra si el contador de referncias totales es menor al Umbral
         paqueteEnviar.setProceso(paquetesPendientes[contPaquetesPendientes-1].getProceso()) ; // Se establece el paquete a enviar desde el registro de paquetes pendientes
         paqueteEnviar.setProcesoExiste(true);// Se estable como verdadero la variable proceso existente 
         paquetesPendientes[contPaquetesPendientes-1].setProceso(procesoNull); // Se borra el proceso del registro paquetes pendientes
         paquetesPendientes[contPaquetesPendientes-1].setProcesoExiste(false); // Se establece como falso el estado del prceso existente 
         contPaquetesPendientes--; // Se decrementa el contador paquetes pendientes
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
      if(contProcesosCliente > 0){
         verProceso = new boolean[nombreProcesos.length];
         for(int i = 0; i < contProcesosCliente; i++){ // Se establece el nuevo estado de cada proceso si este ha finalizado de ser procesado 
            for(int j = 0; j < contProcFinalizados; j++){ 
               if(nombreProcesos[i].equals(procFinalizadosNombre[j])){ // compara y busca el nombre del proceso en el registro de procesos existentes 
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
         //System.out.println("SERVER- El cliente no tiene mas de un proceso pendiente");
         verProceso = new boolean[1];
         verProceso[0] = false;
         return verProceso;
      }
      
      
   }
   public synchronized String actualizarImprimir(int idCliente){
      String imprimirFinal = "";
      imprimirFinal = cadenaCliente[idCliente];
      cadenaCliente[idCliente] = "";
      return imprimirFinal;
   }
   public synchronized void imprimir(String cadenaPendiente, String proceso){
      List<String> aux = new ArrayList();
      for(int i = 0; i < contClientAct; i++){
         aux = procesosClientes.get(i);
         for(int j = 0; j < contadorProcesosCliente[i]; j++){
            if(aux.get(j).equals(proceso)){
               cadenaCliente[i] = cadenaCliente[i] + cadenaPendiente; //cambier el arreglo a un solo string y separar las lineas con una coma
            }
         }
      }
   }
}

