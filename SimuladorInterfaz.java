import java.rmi.Remote;
import java.rmi.RemoteException;
    /**
    * Docs Interfaz RMI 
    * @code La interfaz remota con todas las operaciones que
    * puede realizar el clientes
    */
public interface SimuladorInterfaz extends Remote { //se crea la interfaz remota con todas operaciones que puede realizar el cliente
    /** 
    * @code Registra el proceso por medio del nombre
    * @param nombre Se utilizar par registrar el nombre del proceso
    */
    public void registrar(String nombre) throws RemoteException;
    /** 
    * @code recibe un paquete
    * @param paquete Es el paquete  que recibe en Paquete
    */
    public boolean recibir(Paquete paquete) throws RemoteException;
    /** 
    * @code actualiza el paquete recibiendo un objeto de tipo paquete
    * el Objeto podra determinar si hay un respuesta positiva o negativa ante la actualizacion
    * @param contRefTotales contador = carga del cliente
    * @param procesosFinalizados Arreglo de procesos finalizados que se traen desde el servidor
    * @param contProcesosFinalizados contador de Procesos que han finalizados
    */
    public Paquete actualizar(int contRefTotales, String[] procesosFinalizados, int[] contProcesosFinalizados) throws RemoteException;
    /** 
    * @code actualizarProceso actualiza los procesos que se les dio el servidor
    * el Objeto podra determinar si hay un respuesta positiva o negativa ante la actualizacion
    * @param nombreProcesos array de un los nombres de los procesos
    * @param contProcesosCliente contador de los procesos que tiene el cliente
    */
    public boolean[] actualizarProceso(String[] nombreProcesos, int[] contProcesosCliente) throws RemoteException;
    /** 
    * @code eliminarClientes - elimina el cliente tanto como usuario y servidor
    * @param nombre nombre del cliente a eliminar
    */
    public void eliminarCliente(String nombre) throws RemoteException;
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