import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SimuladorInterfaz extends Remote {
    public void registrar(String nombre) throws RemoteException;
    public void recibir(Paquete paquete) throws RemoteException;
    public Paquete actualizar(int[] contRefTotales, boolean[] estadoProcesosPendientes, int[] contProcesosAct) throws RemoteException;
    public boolean[] actualizarProceso(String[] nombreProcesos) throws RemoteException;
    public void administrarProceso() throws RemoteException;
    public void eliminarCliente(String nombre) throws RemoteException;
}