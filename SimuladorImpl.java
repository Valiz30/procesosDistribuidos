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
   int[] idCliente = new int[TOTAL_CLIENTES], contadorProcesosCliente = new int[TOTAL_CLIENTES]; //identificadores de los clientes, contador del total de clientes en linea
   int contClientAct = 0, contPaquetesPendientes = 0, contProcFinalizados = 0; //contadores
   Paquete[] paquetesPendientes = new Paquete[TOTAL_PAQUETES];// registro de paquetes/procesos que esperan a ser ejecutados
   Procesos procesoNull = new Procesos("nombre", 0, "orden", 0); //Para iniciar la instancia de la clase Paquetes
   String[] procFinalizadosNombre = new String[TOTAL_PROCESOS], cadenaCliente = new String[TOTAL_CLIENTES]; //nombre de todos los procesos en el cliente, cadena pendiente a imprimir por cliente
   boolean[] estadoProcesos = new boolean[TOTAL_PROCESOS], estadoClientes = new boolean[TOTAL_CLIENTES]; //estados de los procesos como de los clientes
   boolean procesoPermitido = false; //banderas
   List<List<String>> procesosClientes = new ArrayList();//Lista que contiene la listas de los nombres de cada cliente
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
   * Se le asigna un indice al cliente, si es que el indice esta 
   * @return idCliente - int
   */
   public synchronized int registrar(){ // Recibe el nombre del cliente 
      int idCliente = 0;
      for(int i = 0; i < TOTAL_CLIENTES; i++){
         if(estadoClientes[i] == false){//si el indice esta libre se lo asigna al cliente que realiza la solicitud
            idCliente = i;
            estadoClientes[i] = true;//se establece el identificador como "ocupado"
            break;
         }
      }
      contClientAct++; // Contador de clientes en el servidor
      return idCliente;
   }
   /**
   * Docs eliminarCliente 
   * @code Elimina el cliente del registro
   * @param idCliente es el identificador que tenia el cliente
   */
   public synchronized void eliminarCliente(int idCliente){
      estadoClientes[idCliente] = false; //se "desocupa" el identificador
      contClientAct--; // Se decrementa el total de clientes actuales en el servidor
   }
   /**
   * Docs verificarProceso 
   * @code Verifica el nombre de los nuevos procesos de cada 
   * cliente, no se permite repetir el mismo nombre entre
   * todos los clientes
   * @param proceso el nombre del proceso
   * @return permitido - boolean
   */
   public synchronized boolean verificarProceso(String proceso){ 
      boolean permitido = true;
      for(int i = 0; i < contPaquetesPendientes; i++){
         if(paquetesPendientes[i].getProceso().getNombre().equals(proceso)){//verifica si el nuevo nombre ya se encuenta ocupado
            permitido = false;
            break;
         }
      }
      return permitido;//le indica al cliente si esta o no disponible
   }
   /**
   * Docs recibir 
   * @code Si un proceso llega a un cliente, le avisa al servidor mediante esta funcion.
   * La cantidad de procesos maxima a recibir de forma general queda en 30, si este
   * no es posible, el procesoPermitido sera negado.
   * @param paquete contiene el la informacion del proceso nuevo
   * @param idCliente es el identificador del cliente que envia el 
   * @return procesoPermitido - boolean
   */
   public synchronized boolean recibir(Paquete paquete, int idCliente){
      List<String> procesosAux = new ArrayList();
      for(int i = 0; i < 30; i++){
         procesosAux.add(i,"");
      }
      if(contPaquetesPendientes < 30){ // 30 es el numero maximo de paquetes
         paquetesPendientes[contPaquetesPendientes] = paquete; // Se agrega un nuevo paquete pendiente
         contPaquetesPendientes++; // Incrementa el contador de paquetes pendientes
         procFinalizadosNombre[contProcFinalizados] = paquete.getProceso().nombre;
         estadoProcesos[contProcFinalizados] = false; // Se coloca como falso el estado del proceso hasta que se procese
         for(int i = 0; i < contClientAct; i++){//se añade el nombre del proceso a la lista que le corresponde
            if(i == idCliente){//cada cliente tiene su propia lista
               procesosAux = procesosClientes.get(i);
               procesosAux.add(procFinalizadosNombre[contProcFinalizados]);
               procesosClientes.add(i, procesosAux);
               procesosClientes.remove(i++);
               i--;
               contadorProcesosCliente[i]++;
               break;
            }
         }
         contProcFinalizados++;//incrementa el total de procesos en el servidor
         return procesoPermitido = true; // mientras el contador de paquetes pendientes sea menor a 30 se permitira anadir mas procesos
      }else{
         return procesoPermitido = false; // Sino se cumple, no se permite
      }
      
   }
   /**
   * Docs actualizar 
   * @code le solicita al servidor procesos si es que hay pendientes, teniendo
   * en cuenta el umbral, ademas de que el cliente envia los procesos que ya
   * haya terminado de ejecutar, que son los que el servidor le dio a ejecutar.
   * @param conRefTotales es la carga que el cliente tiene hasta el momento de hacer la llamada
   * @param procesosFinalizados es la lista de nombres que termino de ejecutar el cliente
   * @param contProcesosFinalizados es el total de procesos que termino de ejecutar el cliente
   * @return paqueteEnviar - Paquete 
   */
   public synchronized Paquete actualizar(int contRefTotales, String[] procesosFinalizados, int[] contProcesosFinalizados){ 
      Paquete paqueteEnviar = new Paquete(procesoNull,false); // sera el paquete que contendra o no el proceso que se le asignara al Cliente
      if(contProcesosFinalizados[0] > 0){//si hay al menos un proceso finalizado
         for(int i = 0; i < contProcesosFinalizados[0]; i++){ // Ciclo for que recorre los clientes finalizados por el cliente
            for(int j = 0; j < contProcFinalizados ; j++){// Ciclo for que recorre los procesos que hay registrados en el servidor
               if(procesosFinalizados[i].equals(procFinalizadosNombre[j])){//si encuentra el proceso finalizado en el registro del servidor
                  estadoProcesos[j] = true;//actualiza el estado en el registro del servidor indicando que se ha terminado de ejecutar
                  break;
               }
            }
            
         }
      }
      if(contRefTotales < UMBRAL && contPaquetesPendientes > 0){ // Entra si la carga del cliente es menor al Umbral
         paqueteEnviar.setProceso(paquetesPendientes[contPaquetesPendientes-1].getProceso()) ; // Se establece el paquete a enviar desde el registro de paquetes pendientes
         paqueteEnviar.setProcesoExiste(true);// Se poner como verdadera la bandera que indica que el servidor si envio un proceso
         paquetesPendientes[contPaquetesPendientes-1].setProceso(procesoNull); // Se borra el proceso del registro paquetes pendientes
         paquetesPendientes[contPaquetesPendientes-1].setProcesoExiste(false); // Se establece como falso el estado del proceso existente 
         contPaquetesPendientes--; // Se decrementa el contador paquetes pendientes
         return paqueteEnviar;
      }else{
         return paqueteEnviar;
      }
   }
   /**
   * Docs actualizarProceso 
   * @code el cliente pide una actualizacion de sus procesos,
   * para saber si ya han terminado de ejecutarse.
   * @param nombreProcesos contiene el nombre de todos los procesos
   * que le pertenecen unicamente al cliente
   * @param contProcesosCliente es el total de procesos del cliente
   * @return verProceso - boolean[]
   */
   public synchronized boolean[] actualizarProceso(String[] nombreProcesos, int contProcesosCliente){
      boolean[] verProceso; 
      if(contProcesosCliente > 0){//Si el cliente tiene al menos un proceso
         verProceso = new boolean[nombreProcesos.length];// se "inicializa"  un arreglo que contendra el estado de cada uno de los procesos del cliente
         for(int i = 0; i < contProcesosCliente; i++){ // se recorre los procesos del cliente
            for(int j = 0; j < contProcFinalizados; j++){ // se recorre los procesos registrados en el servidor
               if(nombreProcesos[i].equals(procFinalizadosNombre[j])){ // si encuentra el proceso en el registro del servidor 
                  verProceso[i] = estadoProcesos[j]; //en la variable que contendra el estado de cada uno de los procesos, se le asigna el estado que tiene en el servidor
                  if(estadoProcesos[j] == true){//en el caso de que algun proceso ya haya terminado de ejecutarse
                     for(int k = j; k < contProcFinalizados; k++){// se elimina el proceso de los registros del servidor
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
      }else{ //en caso de que el cliente no tenga procesos pendiente, se retorna la variable "vacia"
         verProceso = new boolean[1];
         verProceso[0] = false;
         return verProceso;
      }
      
   }
   /**
   * Docs actualizarImprimir 
   * @code El cliente solita las cadena que tenga pendiente a imprimir
   * para que el cliente le indique las operaciones por las que esta
   * pasando su proceso al usuario
   * @param idCliente el identificador del cliente que solicita la informacion
   * @return imprimirFinal - String
   */
   public synchronized String actualizarImprimir(int idCliente){
      String imprimirFinal = "";
      imprimirFinal = cadenaCliente[idCliente]; //ubica la cadena que le pertenece al cliente y se la asigna a la variable a retornar
      cadenaCliente[idCliente] = "";//se vacia la cadena del cliente
      return imprimirFinal;
   }
   /**
   * Docs actualizarImprimir 
   * @code El cliente solita las cadena que tenga pendiente a imprimir
   * para que el cliente le indique las operaciones por las que esta
   * pasando su proceso al usuario
   * @param cadenaPendiente cadena que indica la operacion que se esta
   * llevando a cabo en el proceso
   * @param proceso es el proceso al que le pertenece la cadena y/ la informacion
   * operacion.
   */
   public synchronized void imprimir(String cadenaPendiente, String proceso){
      List<String> aux = new ArrayList();
      for(int i = 0; i < contClientAct; i++){//recorre todas las listas que contienen los procesos por cada cliente
         aux = procesosClientes.get(i);//obtiene la lista de cada uno de los clientes
         for(int j = 0; j < contadorProcesosCliente[i]; j++){//busca en la lista el nombre del proceso
            if(aux.get(j).equals(proceso)){//si encuentra al cliente al que le pertenece el proceso
               cadenaCliente[i] = cadenaCliente[i] + cadenaPendiente; //añade la cadena a imprimir al registro para que el cliente pueda solicitarla
            }
         }
      }
   }
}

