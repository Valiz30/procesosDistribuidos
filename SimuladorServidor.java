import java.io.*;
import java.rmi.*;
/**
* Docs SimuladorServidor SERVIDOR
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
      } catch(Exception e) { //catch de alguna excepcion durante el proceso
         System.out.println("SimuladorServidor: "+e.getMessage()); // Manda el mensaje de la excepcion
         e.printStackTrace(); // Imprime el registro del stack donde se ha iniciado la excepcion
      }
   }
}

//TERMINADO