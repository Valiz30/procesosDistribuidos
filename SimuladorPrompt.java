import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorPrompt extends Thread {
    /**
    *   Docs de Variables de la Clase     CLIENTE
    * procesosCliente - lista que solo le pertenece a los clientes
    * procesoNuevo - proceso que reciba el cliente
    * contProcesosAct - contador De todos los Procesos actuales del cliente que recibe del servidor
    * contProcesosCliente - contador de procesos que recibe del usuario
    * nombreCliente - nombre que se usa cuando se registra el cliente
    * sInterfaz - interfaz RMI 
    * datos -  Datos que maneja el cliente
    * paquete - Contiene la informacion del proceso
    */
    String[] listaProcesosDespachar,procesosCliente = new String[30]; 
    String nombreCliente, nombreHilo = "SimuladorPrompt";    
    SimuladorInterfaz sInterfaz;
    Datos datos;
    Procesos procesoNuevo = new Procesos("", 0, "", 0);
    Paquete paquete = new Paquete(procesoNuevo, false);
    boolean procesoPermitido; //---------------------------------------
    
    /**
    * Docs SimuladorPromt Constructor
    *
    * @param datos Recibe los datos y los asigna
    * @param paquete Recibe el paquete y los asigna 
    * @param nombreCliente Recibe el nombre del cliente y asigna
    */
    public SimuladorPrompt(Datos datos, String nombreCliente, SimuladorInterfaz sInterfaz){
        this.datos = datos;
        this.nombreCliente = nombreCliente;
        this.sInterfaz = sInterfaz;
    }
    /**
    * Docs run 
    * @code Metodo que se encarga de la ejecucion del hilo en donde se le asigna un 
    * procesos al paquete desde el prompt, y se van ejecutando los valores pertenecientes
    *
    */
    public void run(){
        boolean usar;
        while(true){
            //System.out.println("SimuladorPrompt");
            try {
                paquete.proceso = prompt();//añade proceso a un paquete
                if(datos.getSalir()){
                    break;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
            paquete.procesoExiste = false;
            try{
                procesoPermitido = sInterfaz.recibir(paquete, datos.getIdCliente());
                if(procesoPermitido == false){
                    System.out.println("CANTIDAD DE PROCESOS EXCEDIDA - NO SE HA REGISTRADO EL PROCESO");
                }
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            if(datos.getSalir() == true){
                System.out.println("Terminar SimuladorPrompt");
                break;
            }
            //System.out.println("Fin SimuladorPrompt");
            usar = false;
        }
    }
    /**
    * Docs longitudCadena
    * @code Mide la longitud de una cadena de caracteres
    * @param cadena recibe un array de caracteres que se ira contando en un ciclo
    * @return int - Longitu de cadena
    */
    int longitudCadena(char cadena[]){
        int longOrdenDes = 0, i = 0;
        for (i = 0; cadena[i] != '\0'; i++)
                longOrdenDes++;
        return longOrdenDes;
    }
    /**
    * Docs cadenaToEntero
    * @code Pasa Una cadena de simbolos numericos a su valor de Int (String 123 -> Int 123)
    * @param cadena recibe un array de caracteres que representan un numero, que previamente
    * sera transformada a un valor Int
    * @return int - Valor de la cadena
    */
    int cadenaToEntero(char cadena[]){
        int unidad = 1, i = 0, enteroChar = 0, suma = 0;
        for (i = cadena.length - 1; i != -1; i--)
        {
                enteroChar = cadena[i] - '0';
                enteroChar = enteroChar * unidad;
                suma = suma + enteroChar;
                unidad = unidad * 10;
        }
        return suma;
    }
    /**
    * Docs registrarDatos
    * @code Obtiene un String del cliente el cual contiene los datos del proceso y se encarga de separar los datos que contiene ese string
      para crear un nuevo proceso y pasarlo como objeto
    *
    * @param comando recibe el string, la entrada total que utilizo el cliente para el proceso
    * @return Procesos - informacion del proceso 
    */
    Procesos registrarDatos(String comando){//se tiene que lanzar como promt 
        //System.out.println("Registra datos");
        int indicePrimerEspacio = 0, indiceSegundoEspacio = 0; //indices
        int contReferencias = 0; //contadores
        for(int i = 0; i < comando.length(); i++){//identifica donde termina una palabra(donde hay un espacio) para rgistrar el nombre y coloca un indice
            if(comando.charAt(i) == ' '){
                indicePrimerEspacio = i;
                break;
            }
        }
        procesoNuevo.nombre = comando.substring(0, indicePrimerEspacio);//registra el nombre del proceso
        //System.out.println("NOMBRE: "+procesoNuevo.nombre);
        for(int i = indicePrimerEspacio + 1; i < comando.length(); i++){//identifica el lugar en el string donde está el numero de paginas que ingreso el cliente en el comando y coloca segundo indice
            if(comando.charAt(i) == ' '){
                indiceSegundoEspacio = i;
                break;
            }
        }
        procesoNuevo.totalPaginas = cadenaToEntero(comando.substring(indicePrimerEspacio + 1, indiceSegundoEspacio).toCharArray());//registra el numero de paginas
        //System.out.println("TOTAL PAGINAS: "+procesoNuevo.totalPaginas);
        procesoNuevo.orden = comando.substring(indiceSegundoEspacio + 1, comando.length());//registra el orden del proceso
        //System.out.println("ORDEN: "+procesoNuevo.orden);
        for(int l = 0; l < procesoNuevo.orden.length(); l++){//cuenta las referencias cotenidas en el orden delproceso nu
            if(procesoNuevo.orden.charAt(l) == ','){
                contReferencias++;
            }
        }
        procesoNuevo.n_inv = contReferencias;//establece el numero de invocaciones de acuerdo con el numero de referencias 
	    //System.out.println("NUMERO INVOCACIONES: "+procesoNuevo.n_inv);
        return procesoNuevo;
    }
    /**
    * Docs prompt
    * @code Obtiene los datos ingresador por el usuario para un nuevo proceso o salir del prompt
    * @return procesoNuevo - informacion del proceso 
    */
    Procesos prompt() throws InterruptedException{
        Procesos procesoNuevo = null;
        int contRefTotales = 0;
        int contProcesosCliente = 0;
        String comando = "";
        String[] procAux = new String[1];
        Scanner entrada = new Scanner(System.in);
        boolean usar = true;
        System.out.print("> ");
        comando = entrada.nextLine();
        //System.out.println(comando);
        //en caso de que el usuario ponga el comando para salir 
        if(comando.toLowerCase().equals("salir")){ //Revisa en minusculas si el comano es "salir"
            try{
                sInterfaz.eliminarCliente(nombreCliente); //Manda a eliminar al cliente
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            //System.out.println("COMANDO SALIR");
            datos.setSalir(true);
            return procesoNuevo;//null
        }
        //agrega un proceso nuevo 
        //System.out.println("SimuladorPrompt");
        usar = true;
        datos.setNombreHilo(nombreHilo);
        /*
        * Permite acceder a una variable
        */
        while(datos.getNombreHilo() != nombreHilo && usar == true){//hace esperar al hilo su turno
            try {
                Thread.sleep(1000);
                datos.setNombreHilo(nombreHilo);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //actualizamos las variables
        contProcesosCliente = datos.getContProcesosCliente();
        procesosCliente = datos.getProcesosCliente();
        procesoNuevo = registrarDatos(comando);//pide datos de proceso
        procesosCliente[contProcesosCliente] = procesoNuevo.nombre;//agrega el nuevo proceso a la lista de procesos del cliente
        //System.out.println("NOMBRE PROCESO CLIENTE: "+procesosCliente[contProcesosCliente]);
        contProcesosCliente++;
        //System.out.println("CONTADOR PROC CLIENTE: "+contProcesosCliente);
        //se actualizan varibles del cliente 
        datos.setContProcesosCliente(contProcesosCliente);
        datos.setProcesosCliente(procesosCliente);
        //System.out.println("Datos colocados");
        usar = false;
        return procesoNuevo;  
    }
}
// TERMINADO