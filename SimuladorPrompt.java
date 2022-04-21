import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorPrompt extends Thread {
    int[] contProcesosAct, contProcesosCliente;
    String[] listaProcesosDespachar;
    Registro[] tablaPaginas;
    int[] registroTablaPaginas;
    SimuladorInterfaz sInterfaz;
    Datos datos;
    Paquete paquete;
    Procesos procesoNuevo; //proceso que reciba el cliente
    Procesos[] listaProcesos; //lista de procesos desordenada
    String[] procesosCliente = new String[30]; //lista que solo le pertenece a los clientes
    public SimuladorPrompt(Datos datos, Paquete paquete){
        this.datos = datos;
        this.paquete = paquete;
    }
    public void run(){
        while(true){
            contProcesosCliente = datos.getContProcesosCliente();
            procesosCliente = datos.getProcesosCliente();
            try {
                paquete.proceso = prompt(contProcesosCliente, procesosCliente);//añadir nuevo proceso
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
            paquete.identificador = -1;
            paquete.procesoExiste = false;
            try{
                sInterfaz.recibir(paquete);
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    }
    //calcula la longitud de una cadena
    int longitudCadena(char cadena[]){
        int longOrdenDes = 0, i = 0;
        for (i = 0; cadena[i] != '\0'; i++)
                longOrdenDes++;
        return longOrdenDes;
    }
    //Traduce un arreglo de caracteres con simbolos numericos a su valor entero dentro de una variable del mismo tipo
    int cadenaToEntero(char cadena[]){
        /*Convertir de char a entero Orden Desplazamiento*/
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
    Procesos registrarDatos(String comando, String[] listaProcesosDespachar, int[] contProcesosAct){//se tiene que lanzar como promt 
        int indicePrimerEspacio = 0, indiceSegundoEspacio = 0; //indices
        char[] numPaginas = {};
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
    Procesos prompt(int[] contProcesosCliente, String[] procesosCliente) throws InterruptedException{
        int contRefTotales = 0;
        String comando = "";
        Scanner entrada = new Scanner(System.in);
        System.out.println("> ");
        comando = entrada.nextLine();
        procesoNuevo = registrarDatos(comando);//pide datos de proceso
        procesosCliente[contProcesosCliente[0]] = procesoNuevo.nombre;
        contProcesosCliente[0]++;
        datos.setContProcesosCliente(contProcesosCliente);
        return procesoNuevo;  
    }
}