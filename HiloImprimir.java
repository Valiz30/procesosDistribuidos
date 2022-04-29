import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloImprimir extends Thread{ // Clase hilo
    /**
    * Docs HiloImprimir 
    * @code Constructor que necesita los datos del cliente, su interfaz y nombre, 
    * que previamente seran enviados al servidor para actualizar la lista de los estados de cada Proceso del cliente
    */
    SimuladorInterfaz sInterfaz;
    String nombreHilo = "HiloImprimir", cadenaImprimir = "";
    char[] cadenaImprimirChar;
    Datos datos;
    public HiloImprimir(Datos datos, SimuladorInterfaz sInterfaz){ // Constructor, recibe interfaz e identificador del cliente
        this.sInterfaz = sInterfaz;
        this.datos = datos;
    }
    /**
    * run  
    * @code Se utiliza una funcion que actua como hilo para imprimir lo que le corresponde al cliente
    */
    public void run(){
        boolean usar;
        while(true){
            //System.out.println("HiloImprimir");
            usar = true;
            datos.setNombreHilo(nombreHilo);
            while(datos.getNombreHilo() != nombreHilo && usar == true){
                try {
                    Thread.sleep(1000);
                    datos.setNombreHilo(nombreHilo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloImprimir.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloImprimir.class.getName()).log(Level.SEVERE, null, ex);
                }
                cadenaImprimir = sInterfaz.actualizarImprimir(datos.getIdCliente());
                cadenaImprimirChar = cadenaImprimir.toCharArray();
                if(cadenaImprimir != ""){
                    for(int i = 0; i < cadenaImprimirChar.length; i++){
                        if(cadenaImprimirChar[i] == ','){
                            System.out.println("");
                        }else{
                            System.out.print(cadenaImprimirChar[i]);
                        }
                    }
                    System.out.println();
                }
                System.out.print("> ");
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            if(datos.getSalir() == true){
                System.out.println("Terminar HiloImprimir");
                break;
            }
            //System.out.println("Fin HiloImprimir");
            usar = false;
            
        }
    }
}