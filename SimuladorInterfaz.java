import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SimuladorInterfaz extends Remote {
    public int registrar(String name) throws RemoteException;
    public void recibir() throws RemoteException;
    public void administrarProceso() throws RemoteException;
}