import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimuladorPrompt extends Thread {
    /**
    *   Docs de Variables de la Clase     CLIENTE
    *   procesosClientes - contiene el nombre de todos los procesos del cliente
    *   nombreHilo - establece el nombre del hilo
    *   sInterfaz - intancia de la interfaz entre el cliente y el servidor
    *   datos - la instancia de los datos que manejan los hilos
    +   procesoNuevo - variable donde se almacenara el nuevo proceso que reciba el cliente
    *   paquete - es el paquete que contendra el proceso que el cliente le enviara al servidor
    *   procesoPermitido - bandera que indica si el nombre del proceso es aceptado (que no exceda el total permitido en el servidor)
    *   
    */
    String[] procesosCliente = new String[30]; 
    String nombreHilo = "SimuladorPrompt";
    SimuladorInterfaz sInterfaz;
    Datos datos;
    Procesos procesoNuevo = new Procesos("", 0, "", 0);
    Paquete paquete = new Paquete(procesoNuevo, false);
    boolean procesoPermitido; 
    
    /**
    * Docs SimuladorPromt Constructor
    *
    * @param datos Recibe los datos y los asigna
    * @param paquete Recibe el paquete y los asigna 
    * @param nombreCliente Recibe el nombre del cliente y asigna
    */
    public SimuladorPrompt(Datos datos, SimuladorInterfaz sInterfaz){
        this.datos = datos;
        this.sInterfaz = sInterfaz;
    }
    /**
    * Docs run 
    * @code Metodo que se encarga de la ejecucion del hilo, cuya tarea
    * es la de leer los procesos del usuario, obtener los datos necesarios
    * y pasarle el registro al servidor para que pueda administrarlo
    */
    public void run(){
        boolean usar; //variable para establecer el uso de los recursos compartidos
        while(true){// el hilo se ejecutara de manera indefinida hasta que el cliente lo indique
            try {
                paquete.proceso = prompt();//funcion que pide el proceso y retorna el proceso con sus datos
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
            paquete.procesoExiste = false; 
            try{
                procesoPermitido = sInterfaz.recibir(paquete, datos.getIdCliente());//le envia el proceso al servidor y el servidor indica si el proceso esta permitido
                if(procesoPermitido == false){
                    System.out.println("CANTIDAD DE PROCESOS EXCEDIDA - NO SE HA REGISTRADO EL PROCESO");
                }
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            if(datos.getSalir() == true){//si el usuario quiere terminar la ejecucion del programa, se sale del ciclo y termina su ejecucion
                System.out.println("Terminar SimuladorPrompt");
                break;
            }
            usar = false;//establece que ya no quiere usar los recursos compartidos
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
    *   Variables:
    * indicePrimerEspacio - indica en donde termina el nombre y comienza el total de paginas
    * indiceSegundoEspacio - indica en donde termina el total de pagina y donde comienza el orden de invocaciones
    ¡ contReferencias - indica el total de referencias del nuevo proceso
    * @param comando recibe el string, la entrada total que utilizo el cliente para el proceso
    * @return procesoNuevo - informacion del proceso 
    */
    Procesos registrarDatos(String comando){
        int indicePrimerEspacio = 0, indiceSegundoEspacio = 0; //indices
        int contReferencias = 0; //contadores
        for(int i = 0; i < comando.length(); i++){//identifica donde termina una palabra(donde hay un espacio) para rgistrar el nombre y coloca un indice
            if(comando.charAt(i) == ' '){
                indicePrimerEspacio = i;
                break;
            }
        }
        procesoNuevo.nombre = comando.substring(0, indicePrimerEspacio);//registra el nombre del proceso
        for(int i = indicePrimerEspacio + 1; i < comando.length(); i++){//identifica el lugar en el string donde está el numero de paginas que ingreso el cliente en el comando y coloca segundo indice
            if(comando.charAt(i) == ' '){
                indiceSegundoEspacio = i;
                break;
            }
        }
        procesoNuevo.totalPaginas = cadenaToEntero(comando.substring(indicePrimerEspacio + 1, indiceSegundoEspacio).toCharArray());//registra el numero de paginas
        procesoNuevo.orden = comando.substring(indiceSegundoEspacio + 1, comando.length());//registra el orden del proceso
        for(int l = 0; l < procesoNuevo.orden.length(); l++){//cuenta las referencias contenidas en el orden del proceso nuevo
            if(procesoNuevo.orden.charAt(l) == ','){
                contReferencias++;
            }
        }
        procesoNuevo.n_inv = contReferencias;//establece el numero de invocaciones de acuerdo con el numero de referencias 
        return procesoNuevo;
    }
    /**
    * Docs prompt
    * @code Obtiene los datos ingresador por el usuario para un nuevo proceso o salir del prompt
    * Variables:
    * procesoNuevo - contendra la informacion del proceso nuevo
    * contadorEspacios - variable que se utilizara para validar la sintaxis de los datos introducidos por el usuario
    * contadorEspaciosReferencias - para validar la sintaxis del orden de invocaciones
    * contError - para volver a pedir el comando
    * contProcesosCliente - cuando el cliente ingresa un nuevo proceso, incrementa el total de procesos que le pertenecen
    * comando - almacenara el comando ingresado por el usuario
    * usar, espacios, simbolos, cadenaCorrecta, pagRef, refAceptada, indiceModificado, procesoPermitido - banderas para verificar la sintaxis de comando
    * @return procesoNuevo - informacion del proceso 
    */
    Procesos prompt() throws InterruptedException{
        Procesos procesoNuevo = null;
        int contadorEspacios = 0, contadorEspaciosReferencias = 0, contError = 0;
        int contProcesosCliente = 0;
        String comando = "";
        Scanner entrada = new Scanner(System.in);
        boolean usar = true, espacios = false, simbolos = true, cadenaCorrecta = false, pagRef = false, refAceptada = false, indiceModificado = false, procesoPermitido = false;
        while(procesoPermitido == false){ //se ejecutara hasta que el cliente ingrese un nombre de proceso correcto (solo el nombre)
            System.out.print(">");
            comando = entrada.nextLine();//lee el comando ingresado por el cliente
            char[] comandoChar = comando.toCharArray(); //obtiene los caracteres del comando para verificar la sintaxis
            if(comando.equals("salir") == false){ //no verifica la sintaxis si el usuario ingresa la cadena "salir"
                while(cadenaCorrecta == false){ //mientras el usuario no ingrese una cadena correcta, la volvera a pedir
                    if(contError > 0){//si el usuario se equivoca una vez, volvera a pedir el comando
                        System.out.print(">");
                        comando = entrada.nextLine();
                        comandoChar = comando.toCharArray();
                    }
                    espacios = false;
                    simbolos = true;
                    cadenaCorrecta = false;
                    pagRef = false;
                    refAceptada = false;
                    contadorEspacios = 0;
                    contadorEspaciosReferencias = 0;
                    if(comando.length() > 1){ //el comando debe tener mas de un caracter
                        for(int i = 0; i < comandoChar.length; i++){//recorre todo el comando
                            if(comandoChar[i] == ' '){//cuenta los espacios en el comando
                                contadorEspacios++;
                            }
                            if(contadorEspacios > 0 && i < comandoChar.length - 1){//verifica que los simbolos, despues del nombre, solo sean numericos
                                if(comandoChar[i] == ' ' || comandoChar[i] == ','){//si encuentra un espacio, pasa al siguiente caracter
                                    indiceModificado = true;
                                    i++;
                                }
                                if((int)comandoChar[i] < 48 || (int)comandoChar[i] > 58){//si no son numericos
                                    simbolos = false;//indica que los simbolos son incorrectos
                                    break;// y deja de leer la cadena
                                }
                                if(indiceModificado == true){//si se salta un espacio, regresa el indice
                                    i--;
                                    indiceModificado = false;
                                }
                                    
                            }
                            if(contadorEspacios > 2){//comienza a verificar la sintaxis del orden de las invocaciones
                                if(comandoChar[i] == ' '){//cuando encuentra un espacio, establece que se acepta la Pagina de la referencia
                                    if(contadorEspaciosReferencias == 0){
                                        contadorEspaciosReferencias++;
                                        pagRef = true;
                                    }else{
                                        pagRef = false;
                                        break;
                                    }
                                    
                                }
                                if(comandoChar[i] == ',' && pagRef == true){//si ya se acepto la referencia y encuentra una coma, acepta el desplazamiento
                                    refAceptada = true;
                                    if(i != comandoChar.length-1){
                                        pagRef = false;
                                        refAceptada = false;
                                        contadorEspaciosReferencias = 0;
                                    }
                                }
                            }
                            if(contadorEspacios >= 3){//tiene que tener los tres elementos
                                espacios = true;
                            }
                        }
                        if(espacios == true && simbolos == true && refAceptada == true){//si todas las condiciones son aceptadas la cadena sera correcta
                            cadenaCorrecta = true;
                        }
                        if(cadenaCorrecta != true){// si la cadena no es aceptada, lo indica y vuelve a pedir el comando
                            contError++;
                            System.out.println("DATOS INCORRECTOS");
                        }
                    }else{
                        contError++;
                        System.out.println("DATOS INCORRECTOS");
                    }
                }
            }
            //en caso de que el usuario ponga el comando para salir 
            if(comando.toLowerCase().equals("salir")){ //Revisa en minusculas si el comano es "salir"
                try{
                    sInterfaz.eliminarCliente(datos.getIdCliente()); //Manda a eliminar al cliente al servidor
                }catch(Exception e) {
                    System.err.println("Servidor excepcion: "+ e.getMessage());
                    e.printStackTrace();
                }
                datos.setSalir(true);
                return procesoNuevo;//null
            }
            //el hilo establece que quiere hacer uso del recurso compartido
            usar = true;
            datos.setNombreHilo(nombreHilo);
            while(datos.getNombreHilo() != nombreHilo && usar == true){//hace esperar al hilo su turno
                try {
                    Thread.sleep(50);
                    datos.setNombreHilo(nombreHilo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            contProcesosCliente = datos.getContProcesosCliente();
            procesosCliente = datos.getProcesosCliente();
            procesoNuevo = registrarDatos(comando);//se obtiene los datos del proceso segun el comando incertado por el cliente
            try{
                if(sInterfaz.verificarProceso(procesoNuevo.getNombre())){//verifica si el nombre del proceso es aceptado 
                    procesoPermitido = true;
                }else{
                    System.out.println("NOMBRE DE PROCESO NO PERMITIDO");
                }
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
        }
        procesosCliente[contProcesosCliente] = procesoNuevo.nombre;//agrega el nuevo proceso a la lista de procesos del cliente
        contProcesosCliente++;//incrementa el numero de procesos en el cliente
        datos.setContProcesosCliente(contProcesosCliente);//actualiza el contador de los procesos del cliente
        datos.setProcesosCliente(procesosCliente);//actualiza los procesos del cliente
        usar = false;
        return procesoNuevo;  //retorna el proceso nuevo
    }
}