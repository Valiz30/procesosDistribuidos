
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorPrompt extends Thread {
    int[] contProcesosAct, contadorTabla;
    String[] listaProcesosDespachar;
    Registro[] tablaPaginas;
    int[] registroTablaPaginas;
    SimuladorInterfaz sInterfaz;
    Datos datos;
    Paquete paquete;
    Procesos procesoNuevo; //proceso que reciba el cliente
    public SimuladorPrompt(Datos datos, Paquete paquete){
        this.datos = datos;
        this.paquete = paquete;
    }
    public void run(){
        while(true){
            contProcesosAct = datos.getContProcesosAct();
            listaProcesosDespachar = datos.getListaProcesosDespachar();
            registroTablaPaginas = datos.getRegistroTablaPaginas();
            try {
                paquete.proceso = prompt(contProcesosAct,listaProcesosDespachar, registroTablaPaginas);//añadir nuevo proceso
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
        int indicePrimerEspacio = 0, indiceSegundoEspacio = 0;
        char[] numPaginas = {};
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
	    listaProcesosDespachar[contProcesosAct[0]] = procesoNuevo.nombre ;//anade el nuevo proceso a la lista de proceso a despachar
	    return procesoNuevo;
    }
    //Anade las paginas del proceso nuevo a la tabla de paginas
    int anadirProcesoTablaPaginas(int[] contProcesosAct, int[] contadorTabla, Procesos procesoNuevo, int[] registroTablaPaginas){
        int i;
        registroTablaPaginas[contProcesosAct[0]] = contadorTabla[0];
        for(i = contadorTabla[0]; i <= contadorTabla[0] + procesoNuevo.totalPaginas; i++){ 
            tablaPaginas[i].referida = 0;
            tablaPaginas[i].modificada = 0;
            tablaPaginas[i].proteccion = 0;
            tablaPaginas[i].presen_ausen = 0;
            tablaPaginas[i].num_marco = 0;
        }
        contadorTabla[0] = contadorTabla[0] + procesoNuevo.totalPaginas;
        return contadorTabla[0];
    }
    Procesos prompt(int[] contadorProcesoAct, String[] listaProcesosDespachar, int[] registroTablaPaginas) throws InterruptedException{
        
        String comando = "";
        Scanner entrada = new Scanner(System.in);
        System.out.println("> ");
        comando = entrada.nextLine();
        procesoNuevo = registrarDatos(comando, listaProcesosDespachar, contProcesosAct);//pide datos de proceso
        contadorTabla[0] = anadirProcesoTablaPaginas(contProcesosAct, contadorTabla, procesoNuevo, registroTablaPaginas);//se añaden las paginas a la tabla de paginas            contProcesosAct[0]++;
        return procesoNuevo;  
    }
}