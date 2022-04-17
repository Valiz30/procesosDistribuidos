import java.io.*;
import java.rmi.*;

public class SimuladorServidor {
   public static void main(String argv[]) {
      /*if(System.getSecurityManager() == null) {
         System.setSecurityManager(new RMISecurityManager());
      }*/
      try {
         SimuladorInterfaz sInterfaz = new SimuladorImpl();
         Naming.rebind("//localhost/SimuladorServidor", sInterfaz);
      } catch(Exception e) {
         System.out.println("SimuladorServidor: "+e.getMessage());
         e.printStackTrace();
      }
   }
}