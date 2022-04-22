import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorPrompt extends Thread {
    /**
    *   Docs de Variables de la Clase
    * procesosCliente - lista que solo le pertenece a los clientes
    * procesoNuevo - proceso que reciba el cliente
    * contProcesosAct - contador De todos los Procesos actuales del cliente que recibe del servidor
    * contProcesosCliente - contador de procesos que recibe del usuario
    * nombreCliente - nombre que se usa cuando se registra el cliente
    * sInterfaz - interfaz RMI 
    * datos -  Datos que maneja el cliente
    * paquete - Contiene la informacion del proceso
    * listaProcesos - lista de procesos desordenada
    */
    int[] contProcesosAct, contProcesosCliente;
    String[] listaProcesosDespachar,procesosCliente = new String[30]; 
    String nombreCliente;    
    SimuladorInterfaz sInterfaz;
    Datos datos;
    Paquete paquete;
    Procesos procesoNuevo; 
    Procesos[] listaProcesos; 
    boolean procesoPermitido; //---------------------------------------
    
    /**
    * Docs SimuladorPromt Constructor
    *
    * @param datos Recibe los datos y los asigna
    * @param paquete Recibe el paquete y los asigna 
    * @param nombreCliente Recibe el nombre del cliente y asigna
    */
    public SimuladorPrompt(Datos datos, String nombreCliente){
        this.datos = datos;
        this.nombreCliente = nombreCliente;
    }
    /**
    * Docs run 
    * @code Metodo que se encarga de la ejecucion del hilo en donde se le asigna un 
    * procesos al paquete desde el prompt, y se van ejecutando los valores pertenecientes
    *
    */
    public void run(){
        while(true){
            try {
                paquete.proceso = prompt();//añadir nuevo proceso
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
            paquete.procesoExiste = false;
            try{
                procesoPermitido = sInterfaz.recibir(paquete);
                if(procesoPermitido == false){
                    System.out.println("CANTIDAD DE PROCESOS EXCEDIDA - NO SE HA REGISTRADO EL PROCESO");
                }
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
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
        for (i = longitudCadena(cadena) - 1; i != -1; i--)
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
    * @code Obtiene un String del cliente, procesos los datos del usuario,
    * los separa hasta obtener los datos del proceso y los registra en un objeto Procesos
    *
    * @param comando recibe el string, la entrada total que utilizo el cliente para el proceso
    * @return Procesos - informacion del proceso 
    */
    Procesos registrarDatos(String comando){//se tiene que lanzar como promt 
        int indicePrimerEspacio = 0, indiceSegundoEspacio = 0; //indices
        // @deprecated char[] numPaginas = {};
        int contReferencias = 0; //contadores
        for(int i = 0; i < comando.length(); i++){
            if(comando.charAt(i) == ' '){
                indicePrimerEspacio = i;
                break;
            }
        }
        procesoNuevo.nombre = comando.substring(0, indicePrimerEspacio);
        for(int i = indicePrimerEspacio + 1; i < comando.length(); i++){
            if(comando.charAt(i) == ' '){
                indiceSegundoEspacio = i;
                break;
            }
        }
        procesoNuevo.totalPaginas = cadenaToEntero(comando.substring(indicePrimerEspacio + 1, indiceSegundoEspacio).toCharArray());
        procesoNuevo.orden = comando.substring(indiceSegundoEspacio + 1, comando.length());
        for(int l = 0; l < procesoNuevo.orden.length(); l++){
            if(procesoNuevo.orden.charAt(l) == ','){
                contReferencias++;
            }
        }
        procesoNuevo.n_inv = contReferencias;
	    return procesoNuevo;
    }
    /**
    * Docs prompt
    * @code Obtiene los datos ingresador por el usuario
    * @return Procesos - informacion del proceso 
    */
    Procesos prompt() throws InterruptedException{
        int contRefTotales = 0;
        int[] auxCont  = {}, auxProc  = {}, contProcesosCliente = {};
        String comando = "";
        String[] procAux = new String[1];
        Scanner entrada = new Scanner(System.in);
        boolean usar = true;
        System.out.println("> ");
        comando = entrada.nextLine();
        while(true){
            usar = true;
            datos.setNombreCliente(nombreCliente);
            /*
            * Permite acceder a una variable
            */
            while(datos.getNombreCliente() != nombreCliente && usar == true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            contProcesosCliente = datos.getContProcesosCliente(nombreCliente);
            procesosCliente = datos.getProcesosCliente(nombreCliente);
            procesoNuevo = registrarDatos(comando);//pide datos de proceso
            procesosCliente[contProcesosCliente[0]] = procesoNuevo.nombre;
            contProcesosCliente[0]++;
            datos.setContProcesosCliente(contProcesosCliente, nombreCliente);
            datos.setProcesosCliente(procesosCliente, nombreCliente);
            usar = false;
            break;
        }
        return procesoNuevo;  
    }
}
// TERMINADO