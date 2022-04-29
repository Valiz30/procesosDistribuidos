import java.rmi.Remote;
import java.rmi.RemoteException;
    /** SERVIDOR
    * Docs Interfaz RMI 
    * @code La interfaz remota con todas las operaciones que
    * puede realizar el clientes
    */
public interface SimuladorInterfaz extends Remote { //se crea la interfaz remota con todas operaciones que puede realizar el cliente
    /** 
    * @code Registra el proceso por medio del nombre
    * @param nombre Se utilizar par registrar el nombre del proceso
    */
    public int registrar(String nombre) throws RemoteException;
    /** 
    * @code recibe un paquete
    * @param paquete Es el paquete  que recibe en Paquete
    * @param idCliente identificador dle cliente
    */
    public boolean recibir(Paquete paquete, int idCliente) throws RemoteException;
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
    public boolean[] actualizarProceso(String[] nombreProcesos, int contProcesosCliente) throws RemoteException;
    /** 
    * @code eliminarClientes - elimina el cliente tanto como usuario y servidor
    * @param nombre nombre del cliente a eliminar
    */
    public void eliminarCliente(String nombre) throws RemoteException;
    /** 
    * @code actualizarImprimir - actualiza los datos a imprimir por cada cliente
    * @param idCliente identificador para cada uno de los clientes
    */
    public String actualizarImprimir(int idCliente) throws RemoteException;
    /** 
    * @code imprimir - se guarda en registro lo que cada cliente tiene que imprimir
    * @param cadenaPendiente cadena a almacenar en el registro
    * @param proceso es el procesos del cual hace referencia la cadena, para poder establecer a quien le pertenece
    */
    public void imprimir(String cadenaPendiente, String proceso) throws RemoteException;
}


//TERMINADO