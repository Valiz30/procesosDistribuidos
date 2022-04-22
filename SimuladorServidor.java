import java.io.*;
import java.rmi.*;
/**
* Docs SimuladorServidor
* @code Servidor que ejecuta la interfaz remota
*
*/

public class SimuladorServidor {
   public static void main(String argv[]) {
      /*if(System.getSecurityManager() == null) {
         System.setSecurityManager(new RMISecurityManager());
      }*/
      try {
         SimuladorInterfaz sInterfaz = new SimuladorImpl(); //Define la interfaz remota
         Naming.rebind("//localhost/SimuladorServidor", sInterfaz); //referencia al objeto remoto
      } catch(Exception e) {
         System.out.println("SimuladorServidor: "+e.getMessage());
         e.printStackTrace();
      }
   }
}

//TERMINADO