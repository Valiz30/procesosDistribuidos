 import static java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Ejecutar extends Thread{
    /**    CLIENTE 
    *   Docs Ejecutar
    *   contProcesosAct - cantidad de procesos que el servidor le asigno al cliente para ejecutar
    *   listaProcesosDespachar - contiene lo procesos a ejecutar por el cliente en orden
    *   registroTablaPaginas  Arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][])
    *   nombreHilo - es el nombre del Hilo que va a tener permiso de acceder a esta clase, en un determinado tiempo
    *   tablaPaginas - sera la tabla que contendra las entradas de la tabla de paginas
    *   listaProcesos - lista que contiene los procesos que el servidor le asigno al cliente, pero en desorden
    *   memorias - son las memoria que se usaran en el proceso de ejecucion
    *   memFisica - representa las localidades de la memoria fisica
    *   procesosFinalizados - es la lista de nombres de los proceso que el cliente ha terminado de ejecutar
    *   datos -  es la intancia de la clase Datos, que contiene los recursos compartidos entre los hilos
    *   sInterfaz - instancia de la interfaz de la aplicacion
    */
    Procesos[] listaProcesos;
    Memorias memorias;
    int TOTAL_P = 30, TOTAL_PAG = 50, AUX = 50,contRefTotales = 0;
    int[] contProcesosAct = {0},registroTablaPaginas = new int[30]; //arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][]) //contadores
    int[] memFisica; //variable de la memoria fisica
    Registro[] tablaPaginas = new Registro[50]; 
    String[] listaProcesosDespachar, procesosFinalizados = new String[30];
    String nombreHilo = "Ejecutar";
    Datos datos;
    SimuladorInterfaz sInterfaz;
    /**
    * Docs Ejecutar
    * @code Constructor
    */
    public Ejecutar(Memorias memorias, Datos datos, SimuladorInterfaz sInterfaz){
        memFisica = new int[memorias.tamMemFis];
        for (int i = 0; i < memorias.tamMemFis; i++)
            memFisica[i] = 0;
        this.memorias = memorias;
        this.datos = datos;
        this.sInterfaz = sInterfaz;
    }

    /**
    * run
    * @code actuando como hilo.  Anade las paginas del proceso nuevo a la tabla de paginas
    */
    public void run(){
        
        boolean usar;
        while(true){
            //se establece que el hilo quiere hacer uso de los recursos compartidos
            usar = true;
            datos.setNombreHilo(nombreHilo);
            while(datos.getNombreHilo() != nombreHilo && usar == true){//identifica si el hilo puede actuar o si espera
                try {
                    Thread.sleep(1000);
                    datos.setNombreHilo(nombreHilo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ejecutar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(datos.getContProcesosAct()[0] > 0){//realiza las operaciones si hay procesos pendientes a ejecutar
                //almacena las variables que va a usar el hilo
                contProcesosAct = datos.getContProcesosAct();
                listaProcesosDespachar = datos.getListaProcesosDespachar();
                listaProcesos = datos.getListaProcesos();
                procesosFinalizados = datos.getProcesosFinalizados();
                tablaPaginas = datos.getTablaPaginas();
                if(contProcesosAct[0] != 0){
                    try{
                        //el hilo realiza la simulacion de los procesos
                        datos.setContProcesosAct(despacharProceso(contProcesosAct, listaProcesosDespachar, listaProcesos, procesosFinalizados, tablaPaginas));
                        for(int i = 0; i < contProcesosAct[0]; i++){//calcula la carga resultante del cliente
                            contRefTotales = contRefTotales + listaProcesos[i].n_inv; 
                        }
                        datos.setContRefTotales(contRefTotales);//actualiza la carga del Hilo 
                    }catch(InterruptedException e){
                        throw new RuntimeException(e);
                    };
                }else{
                    datos.setProcesosPendientes(false);
                    break;
                }
                
            }
            if(datos.getSalir() == true){
                System.out.println("Terminar Ejecutar");
                break;
            }
            usar = false;
        }
    }
    /**
    *   decimalBinario()
    *   @code funcion para traducir de decimal a binario
    *   @param decimal valor de entrada en decimal
    *   @param binario[] se guarda el valor binario
    *   @param totalBits contador que se usa para los bits totales
    */
    public void decimalBinario(int decimal, int binario[], int totalBits){ 
        float residuo;
        int cont = totalBits-1;
        for(int k = 0; k < totalBits; k++){//inicializa en 0's el arreglo donde se almacenara el numero en binario
            binario[k] = 0;
        }                                        
        for(int i = 0; i < totalBits-1; i++){ //genera los bits representativos del valor decimal
            residuo = decimal % 2;
            decimal = decimal / 2;
            binario[cont] = (int)residuo;
            cont--;
        }
        binario[cont] = decimal;
    }
    /**
    *   binarioDecimal
    *   @code funcion para traducir de binario a decimal
    *   @param binario[] entradad de binario
    *   @return decimal - int
    */
    int binarioDecimal(int binario[], int totalValores){
        int decimal = 0, cont = 0;
        for(int i = totalValores - 1; i >= 0; i--){
            if(binario[i] == 1)
                decimal = (int) (decimal + pow(2,cont));//se basa en la elevacion al cuadrado segun la posicion y si el "bit" esta encendido
            cont++;
        }
        return decimal;
    }
    /**
    *   binarioHexadecimal 
    *   @code funcion que pasa de binario a Hexadecximal
    *   @param dirBin[] entrada de binario
    *   @param bitsBin bits del binario
    *   @param dirHex[] guarda el valor hexadecimal
    */
    void binarioHexadecimal(int dirBin[], int bitsBin, char dirHex[]){
        int cont = 3, valor, cont2 = 0;
        int[] aux = new int[4], dirAux = new int[30];
        for(int i = 0; i < 4; i++){//inicializa un arreglo auxiliar en 0's, sera para almacenar en grupos de 4 bits y pasarlo a hecadecimal
            aux[i] = 0;
        }
        for(int i = bitsBin - 1; i >= 0; i--){
            if(cont >= 0){
                aux[cont] = dirBin[i];
                cont--;
            }
            if(cont == -1 || (cont > -1 && i == 0)){
                valor = binarioDecimal(aux,4);  //obtiene el valor decimal del grupo de 4 bits
                switch(valor){//segun sea el valor en decimal, se le asiganara el simbolo correspondiente
                    case 0: dirAux[cont2] = '0';
                        break;
                    case 1: dirAux[cont2] = '1';
                        break;
                    case 2: dirAux[cont2] = '2';
                        break;
                    case 3: dirAux[cont2] = '3';
                        break;
                    case 4: dirAux[cont2] = '4';
                        break;
                    case 5: dirAux[cont2] = '5';
                        break;
                    case 6: dirAux[cont2] = '6';
                        break;
                    case 7: dirAux[cont2] = '7';
                        break;
                    case 8: dirAux[cont2] = '8';
                        break;
                    case 9: dirAux[cont2] = '9';
                        break;
                    case 10: dirAux[cont2] = 'A';
                        break;
                    case 11: dirAux[cont2] = 'B';
                        break;
                    case 12: dirAux[cont2] = 'C';
                        break;
                    case 13: dirAux[cont2] = 'D';
                        break;
                    case 14: dirAux[cont2] = 'E';
                        break;
                    case 15: dirAux[cont2] = 'F';
                        break;
                    default: break;
                }
                cont2++;
                cont = 3;
                for(int j = 0; j < 4; j++){
                    aux[j] = 0;
                }
            }
        }
        dirHex[cont2] = 'G';
        dirAux[cont2] = 'G';//se usa una G para indicar el final del numero Hexadecimal
        cont = 0;
        for(int i = cont2 - 1; i >= 0; i--){
            dirHex[cont] = (char)dirAux[i];
            cont++;
        }
    }
    /**
    *   traducir 
    *   @code traduce los datos pasados como parametos en sus correspondientes bases, 
    *   regresa ya sea la direccion virtual o la direccion fisica
    *   @param indicePagMar[] indice del marco de pagina
    *   @param bitsPagMar bits del marco de pagina
    *   @param desplazamiento desplazamiento
    *   @param bitsDesplazamiento bits de dezplazamiento
    *   @param dirBin[] direccion en binario
    *   @param dirDec[] direccion en decimal
    *   @param dirHex[] direccion en hexadecimal
    */
    void traducir(int indicePagMar, int bitsPagMar, int desplazamiento, int bitsDesplazamiento, int[] dirBin, int[] dirDec, char[] dirHex, Procesos[] listaProcesos, int indiceProcesoDespachar){
        int i;
        String cadenaEnviar = "";
        direccionBases(indicePagMar, bitsPagMar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
        cadenaEnviar = cadenaEnviar + "," + "PROCESO: " + listaProcesos[indiceProcesoDespachar].getNombre() + " - Binaria: ";
        for(i = 0; i < bitsDesplazamiento + bitsPagMar; i++){
            cadenaEnviar = cadenaEnviar + dirBin[i];
        }
        cadenaEnviar = cadenaEnviar + " - Decimal: " + dirDec[0];
        cadenaEnviar = cadenaEnviar + " - Hexadecimal: ";
        for(i = 0; i < 30; i++){
            if(dirHex[i] != 'G'){
                cadenaEnviar = cadenaEnviar + dirHex[i];
            }else{
                break;
            } 
        }
        try{
            sInterfaz.imprimir(cadenaEnviar, listaProcesos[indiceProcesoDespachar].getNombre());
        }catch(Exception e) {
            System.err.println("Servidor excepcion: "+ e.getMessage());
            e.printStackTrace();
        }
    }
    /**
    *   direccionBases 
    *   @code calcula la direccion en base 2, 10 y 16
    *   regresa ya sea la direccion virtual o la direccion fisica
    *   @param indicePagMar[] indice del marco de pagina
    *   @param bitsPagMar bits del marco de pagina
    *   @param desplazamiento desplazamiento
    *   @param bitsDesplazamiento bits de dezplazamiento
    *   @param dirBin[] direccion en binario
    *   @param dirDec[] direccion en decimal
    *   @param dirHex[] direccion en hexadecimal
    */
    void direccionBases(int indicePagMar, int bitsPagMar, int desplazamiento, int bitsDesplazamiento, int[] dirBin, int[] dirDec, char[] dirHex){ //que se modifique la variable
        int i, cont = 0, aux = 9;
        int[] aux1 = new int[bitsPagMar], aux2 = new int[bitsDesplazamiento];
        decimalBinario(indicePagMar, aux1, bitsPagMar);//pasa a binario el indice de pagina o el indices de marco, segun se solicite
        decimalBinario(desplazamiento, aux2, bitsDesplazamiento);//pasa a binario el desplazamiento de la pagina o del marco, segun se solicite
        for(i = 0; i < bitsPagMar; i++){//añade al arreglo dirBin[], primero los bits de la pagina o el marco
            dirBin[cont] = aux1[i];
            cont++;
        }
        for(i = 0; i < bitsDesplazamiento; i++){//añade al mismo arreglo dirBin[], los bits de desplazamiento
            dirBin[cont] = aux2[i];
            cont++;
        }
        dirDec[0] = binarioDecimal(dirBin, bitsPagMar + bitsDesplazamiento);//pasa de binario a decimal
        binarioHexadecimal(dirBin, bitsPagMar + bitsDesplazamiento, dirHex);//pasa de binario a Hexadecimal
    }
    /**
    *   longitudCadena 
    *   @code   calcula la longitud de una cadena
    *   regresa ya sea la direccion virtual o la direccion fisica
    *   @param cadena[] entrada como cadena
    *   @return int
    */
    int longitudCadena(char cadena[]){
            int longOrdenDes = 0, i = 0;
            for (i = 0; cadena[i] != '\0'; i++)
                    longOrdenDes++;
            return longOrdenDes;
    }
    /**
    *   cadenaToEntero 
    *   @code Traduce un arreglo de caracteres con simbolos numericos a su valor entero 
    *   dentro de una variable del mismo tipo
    *   @param cadena[] cadenad de entrada
    *   @return suma - int
    */
    int cadenaToEntero(char cadena[]){
            /*Convertir de char a entero Orden Desplazamiento*/
            int unidad = 1, i = 0, enteroChar = 0, suma = 0;
            for (i = longitudCadena(cadena) - 1; i != -1; i--){
                    enteroChar = cadena[i] - '0';
                    enteroChar = enteroChar * unidad;
                    suma = suma + enteroChar;
                    unidad = unidad * 10;
            }
            return suma;
    }
    /**
    *   contadorBits 
    *   @code Cuenta el numero de bits que se necesita para poder direccionar un valor numerico
    *   dentro de una variable del mismo tipo
    *   @param numBytes[] entrada de binario
    *   @return bits - int
    */
    int contadorBits(int numBytes){
            int bits = 0, i = 0;
            for (i = 0; numBytes != 1; i++){
                    numBytes = numBytes / 2;
                    bits++;
            }
            return bits;
    }
    /**
    *   realizarReferencia 
    *   @code hace la siguiente referencia del proceso en cuestion
    *   @param indiceProcesoDespachar[] indice proceso de despachar
    *   @param memFisica representacion de la memoria fisica
    *   @param memorias[] informacion sobre la memoria fisica, virtual
    */
    void realizarReferencia(int indiceProcesoDespachar, int memFisica[], Memorias memorias, Procesos[] listaProcesos, Registro[] tablaPaginas, String[] listaProcesosDespachar){
            int indicePagMar = 0, desplazamiento = 0, bitsPagMar = 0, bitsDesplazamiento = 0; 
            int[] dirBin = new int[30], dirDec = {0};
            char[] dirHex = new char[30];
            String cadenaEnviar = "";
            desplazamiento = memorias.tamPag;//asginacion para saber cual es el tamño de la pagina
            bitsDesplazamiento = contadorBits(desplazamiento);//en base a la asignacion anterior se obtien el total de bits

            /* Virtual */
            int numeroPaginas = memorias.tamMemVir / memorias.tamPag;//calcula el numero de paginas en memoria virtual
            int bitsNumPagVirtual = contadorBits(numeroPaginas);//calcula el numero de bits necesarios para direccionar una pagina
            int bitsDireccionVirtual = bitsDesplazamiento + bitsNumPagVirtual;//calcula el total de bits para una direccion virtual

            /* Fisica */
            int marcoPaginas = memorias.tamMemFis / memorias.tamPag;//calcula el numero de marcos en memoria virtual
            int bitsMarcoFisica = contadorBits(marcoPaginas);//calcula el numero de bits necesarios para direccionar un marco
            int bitsDireccionFisica = bitsDesplazamiento + bitsMarcoFisica;//calcula el total de bits para una direccion fisica

            // Buscar el nombre del proceso a despachar
            String nombre = "";
            nombre = listaProcesos[indiceProcesoDespachar].nombre;
            int i = 0, indice = 0;
            for (i = 0; i < 3; i++){
                    if (listaProcesosDespachar[i] == nombre){
                            cadenaEnviar = cadenaEnviar + "," + " Proceso encontrado: "+listaProcesosDespachar[i];
                        try{
                            sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[i]);//envia la cadena de la operacion que acaba de realizar 
                        }catch(Exception e) {
                            System.err.println("Servidor excepcion: "+ e.getMessage());
                            e.printStackTrace();
                        }
                            cadenaEnviar = "";
                            indice = i;
                            break;
                    }
            }
            /*devuelve un int indice a partir donde se encuentran las paginas dentro de la tabla de paginas*/
            int posPagToReferencia = registroTablaPaginas[indice];
            /* Si es 0, la página virtual a la que pertenece la entrada no se encuentra
            actualmente en la memoria fisica*/

            /******************************************************************************************************/
            /*******************|CONVERTIR DE CHAR A INT -> NUM PAGINA Y DESPLAZAMIENTO|***************************/
            /*Variables a utilizar para almacenar*/
            int numpaginaInt = 0;
            int desplazamientoInt = 0;

            char[] registroOrdenRemplazo = new char[100];
            registroOrdenRemplazo = listaProcesos[indiceProcesoDespachar].orden.toCharArray();
            /* Encontrar el primer Orden desplazamiento*/
            int indiceFinal = 0;
            for(int k = 0; k < registroOrdenRemplazo.length; k++){
                if(registroOrdenRemplazo[k] == ','){
                    indiceFinal = k;
                    break;
                }
            }
            char[] ordenDesplazamiento = listaProcesos[indiceProcesoDespachar].orden.substring(0, indiceFinal+1).toCharArray();//almacena la primera invocacion
            /*Longitud del arreglo encontrado ordenDesplazamiento*/
            int longOrdenDes = 1; /* El tamaño toma en cuenta la coma, incrementando a 1 */
            i = 0;
            //longOrdenDes += longitudCadena(ordenDesplazamiento);
            String lg = String.valueOf(ordenDesplazamiento);
            longOrdenDes += lg.length();
            /*Separar Orden de desplazamiento (Tomar en cuenta el 1 añadido)*/
            char[] desplazamientoSep = new char[5];// = {'\0'};
            char[] numeroPaginaSep = new char[2]; // = {'\0'};
            int bandera = 0;
            /*Separacion de variable*/
            int contadorArreglo = 0;
            for (i = 0; i < ordenDesplazamiento.length; i++){
                if(ordenDesplazamiento[i] == ','){
                    break;
                }
                if (ordenDesplazamiento[i] == ' '){//si en el arreglo de caracteres que contiene la primera invocacion, encuentra un espacio, quiere decir que lo que continua es el desplazamiento
                        bandera = 1;//activa la bandera para indicar que sigue el desplazamiento
                        contadorArreglo = -1; /* Toma en cuenta que no se hace nada en el espacio, y el sig suma para que quede en 0 */
                }
                if (ordenDesplazamiento[i] != ' '){
                        if (bandera == 0)
                                numeroPaginaSep[contadorArreglo] = ordenDesplazamiento[i];//si la bandera no esta en 1, quiere decir que lee el numero de pagina
                        else
                                desplazamientoSep[contadorArreglo] = ordenDesplazamiento[i];//si la bandera esta en 1, quiere decir que ahora leera el desplazamiento
                }
                contadorArreglo++;
            }
            for(int x = 0; x < ordenDesplazamiento.length; x++)
            /*Convertir la cadenas a int, tanto el numero de pagina como el numero de desplazamiento*/
            numpaginaInt = cadenaToEntero(numeroPaginaSep);
            desplazamientoInt = cadenaToEntero(desplazamientoSep);
            cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" Num. Pag: "+numpaginaInt+", Desplazamiento: "+desplazamientoInt;
            try{
                sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            cadenaEnviar = "";
            /*******************************************************************************************************/

            /* número  de  página  espacio desplazamiento. "4 56"	*/
            /* se Usara -> numpaginaInt, desplazamientoInt */

            /*Imprimir Direccion Virtual*/
            indicePagMar = numpaginaInt;
            bitsPagMar = bitsNumPagVirtual;
            desplazamiento = desplazamientoInt;
            /* bitsDesplazamiento se declaro al inicio */
            cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" Direccion Virtual: ";
            try{
                sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            };
            traducir(indicePagMar, bitsPagMar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex, listaProcesos, indiceProcesoDespachar); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
            cadenaEnviar = "";
            /*********************************/
            /*Buscara la pagina en memoria fisica*/
            if (tablaPaginas[posPagToReferencia+numpaginaInt].presen_ausen == 0){ /*No se encuentra*/
                    cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" FALLO DE PAGINA: ";
                    try{
                        sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
                    }catch(Exception e) {
                        System.err.println("Servidor excepcion: "+ e.getMessage());
                        e.printStackTrace();
                    }
                    cadenaEnviar = "";
                    int BitsLibres = 0;
                    int marcoPaginaIndice = 0;
                    for (i = 0; i < memorias.tamMemFis; i += memorias.tamPag){//busca espacios libres en memoria fisica
                            if (memFisica[i] == 0){
                                    BitsLibres = 1;//indica si hay bits libres
                                    marcoPaginaIndice = i;//indica a partir de que valor se encuentran bits libres
                                    break;
                            }
                    }
                    /*Llenar mapa de bis para un fallo de pagina*/
                    if (BitsLibres == 1){ /* Se Encuentra libre un marco*/
                            for (i = marcoPaginaIndice; i < marcoPaginaIndice + memorias.tamPag; i++){//llena la memoria fisica
                                    memFisica[i] = 1;
                            }
                            /*Cambiar en tabla De paginas los valores de la pagina que ha sido referenciada*/
                            tablaPaginas[posPagToReferencia+numpaginaInt].presen_ausen = 1;
                            tablaPaginas[posPagToReferencia+numpaginaInt].referida = 1;
                            tablaPaginas[posPagToReferencia+numpaginaInt].num_marco = marcoPaginaIndice;
                            indicePagMar = marcoPaginaIndice;
                            bitsPagMar = bitsMarcoFisica;
                            /* desplazamiento se declaro en Imprimir direccion virtual */
                            /* bitsDesplazamiento se declaro al inicio */
                            cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" Direccion Fisica: ";
                            try{
                                sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
                            }catch(Exception e) {
                                System.err.println("Servidor excepcion: "+ e.getMessage());
                                e.printStackTrace();
                            }
                            traducir(indicePagMar, bitsPagMar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex, listaProcesos, indiceProcesoDespachar); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
                            cadenaEnviar = "";
                    }
                    else{ /*No se encuentra libre un marco*/
                            /*Eliminar una pagina que este en el marco aleatoriamente*/
                            /*Generar un random para eliminar*/
                            int marcosExisten = (memorias.tamMemFis / memorias.tamPag); /*Cuenta desde el 0*/
                            /*numero del marco a eliminar*/
                            int randomEliminaMarco = (int)(Math.floor(Math.random() * (marcosExisten - 0 + 1)) + 0);;	   // Entre el 0 y el numMarcosExistentes
                            int indiceEliminar = randomEliminaMarco * memorias.tamPag; /*Indice donde se encuentra*/
                            /* No se elimina el del mapa de bits, pues de todas formas se va a llenar, solo se cambia en la tabla de paginas */
                            /*Tabla de paginas se busca el marco a eliminar -> randomEliminaMarco*/
                            for(i=0; i < TOTAL_P; i++ ){
                                    if(tablaPaginas[TOTAL_PAG].num_marco == randomEliminaMarco){					
                                            tablaPaginas[TOTAL_P].presen_ausen = 0;
                                            tablaPaginas[TOTAL_P].modificada = 0;
                                            tablaPaginas[TOTAL_P].num_marco = 0;
                                            tablaPaginas[TOTAL_P].referida = 0;
                                            break;
                                    }
                            } 

                            /*Cambiar en tabla De paginas*/
                            tablaPaginas[posPagToReferencia+numpaginaInt].presen_ausen = 1;
                            tablaPaginas[posPagToReferencia+numpaginaInt].referida = 1;
                            tablaPaginas[posPagToReferencia+numpaginaInt].num_marco = randomEliminaMarco;

                            indicePagMar = tablaPaginas[posPagToReferencia].num_marco;
                            bitsPagMar = bitsMarcoFisica;
                            /* desplazamiento se declaro en Imprimir direccion virtual */
                            /* bitsDesplazamiento se declaro al inicio */
                            /* Imprimir la direccion fisica*/
                            cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" FALLO DE PAGINA: ";
                            try{
                                sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
                            }catch(Exception e) {
                                System.err.println("Servidor excepcion: "+ e.getMessage());
                                e.printStackTrace();
                            }
                            cadenaEnviar = "";
                            cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" Direccion Fisica: ";
                            try{
                                sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
                            }catch(Exception e) {
                                System.err.println("Servidor excepcion: "+ e.getMessage());
                                e.printStackTrace();
                            }
                            traducir(indicePagMar, bitsPagMar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex, listaProcesos, indiceProcesoDespachar); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
                            cadenaEnviar = "";
                    }
            }
            else{	/*Se encuentra en memoria fisica la pagina- Imprimir la memoria Fisica*/
                    
                    /*Imprimir la direccion fisica*/
                    indicePagMar = tablaPaginas[posPagToReferencia].num_marco;
                    bitsPagMar = bitsMarcoFisica;
                    /* desplazamiento se declaro en Imprimir direccion virtual */
                    /* bitsDesplazamiento se declaro al inicio */
                    cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" YA ESTABA EN MEMORIA FISICA: ";
                    try{
                        sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
                    }catch(Exception e) {
                        System.err.println("Servidor excepcion: "+ e.getMessage());
                        e.printStackTrace();
                    }
                    cadenaEnviar = "";
                    cadenaEnviar = cadenaEnviar + "," + "PROCESO: "+listaProcesos[indiceProcesoDespachar].getNombre()+" Direccion Fisica: ";
                    try{
                        sInterfaz.imprimir(cadenaEnviar, listaProcesosDespachar[indice]);
                    }catch(Exception e) {
                        System.err.println("Servidor excepcion: "+ e.getMessage());
                        e.printStackTrace();
                    }
                    cadenaEnviar = "";
                    traducir(indicePagMar, bitsPagMar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex, listaProcesos, indiceProcesoDespachar); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
                    tablaPaginas[posPagToReferencia].referida = 1;
            }
            /*- debera de modificar el la entrada que le pertenece a dicho proceso en la tabla de paginas 	*/

            /*Eliminar la invocacion de la variable registro[indiceProceso] */
            /* El 0 se cambia por indiceProceso*/
            String vacio = listaProcesos[indiceProcesoDespachar].orden.substring(indiceFinal+1);
            listaProcesos[indiceProcesoDespachar].orden = vacio;
            /**************************/
    }
    /**
    *   quantum 
    *   @code    hace la simulacion del quantum -> 3 segundo -> 1 segundo = 1 referencia
    *   @param contProcesosAct[] contador de los procesos Actuales
    *   @param indiceProcesoDespachar indice de procesos a despachar
    *   @param memFisica[] representacion de la memoria fisica
    *   @param memorias informacion sobre las memorias.A
    *   @param procesosFinalizados array de los procesos finalizados
    */
    int quantum(int[] contProcesosAct, int indiceProcesoDespachar, int memFisica[], Memorias memorias, String[] procesosFinalizados, Procesos[] listaProcesos, Registro[] tablaPaginas, String[] listaProcesosDespachar) throws InterruptedException{
        int contReferencias = 0, j = 0, total_referencias = 0;
        boolean procesoFinalizado = false; 
        //calcula el total de invocaciones restantes
        for(j = 0; j < listaProcesos[indiceProcesoDespachar].orden.length(); j++){
            if(listaProcesos[indiceProcesoDespachar].orden.charAt(j) == ','){
                contReferencias++;
            }
        }
        total_referencias = contReferencias;
        contReferencias = 1;
        //ciclo donde se hara la simulacion del quantum
        while(total_referencias > 0 && contReferencias <= 3){
            realizarReferencia(indiceProcesoDespachar, memFisica, memorias, listaProcesos, tablaPaginas, listaProcesosDespachar); //solo hace una referencia
            total_referencias--;
            TimeUnit.SECONDS.sleep(1);
            contReferencias++;
        }
        datos.setTablaPaginas(tablaPaginas);
        j = 0;
        contReferencias = 0;
        //calcula el total de invocaciones restantes
        for(j = 0; j < listaProcesos[indiceProcesoDespachar].orden.length(); j++){
            if(listaProcesos[indiceProcesoDespachar].orden.charAt(j) == ','){
                contReferencias++;
            }
        }
        total_referencias = contReferencias;
        if(total_referencias == 0){//si ya no hay mas referencias y/o invocaciones pendientes en el proceso 
            int[] contProcesosFinalizados = datos.getContProcesosFinalizados();
            procesosFinalizados[contProcesosFinalizados[0]] = listaProcesosDespachar[0];
            contProcesosFinalizados[0]++;
            datos.setprocesosFinalizados(procesosFinalizados);
            datos.setContProcesosFinalizados(contProcesosFinalizados);
            // Se elimina el proceso que marque el indice y se acomoda el registro
            for(int i = 0; i < contProcesosAct[0]; i++){//se elimina de las estructuras ordenadas
                listaProcesosDespachar[i] = listaProcesosDespachar[i+1];
                registroTablaPaginas[i] = registroTablaPaginas[i+1];
                if(i == (TOTAL_P-1)){
                    for(j = 0; j < listaProcesosDespachar[i].length(); j++)
                        listaProcesosDespachar[i] = "";
                    registroTablaPaginas[i] = 0;
                }
            }
            for(int i = indiceProcesoDespachar; i < contProcesosAct[0]; i++){//se elimina de la estructura registro
                listaProcesos[i].nombre = listaProcesos[i+1].nombre;
                listaProcesos[i].totalPaginas = listaProcesos[i+1].totalPaginas;
                listaProcesos[i].orden = listaProcesos[i+1].orden;
                if(i == (TOTAL_P-1)){
                    listaProcesos[i].nombre = "";
                    listaProcesos[i].totalPaginas = 0;
                    listaProcesos[i].orden = "";
                }
            }
            procesoFinalizado = true;
        }
        
        datos.setListaProcesos(listaProcesos);
        datos.setListaProcesosDespachar(listaProcesosDespachar);
        return total_referencias;
    }
    /**
    *   despacharProceso 
    *   @code  despacha el proceso 
    *   @param contProcesosAct[]contador de procesos actuales
    *   @param listaProcesosDespachar array de string de los procesos a despachar
    *   @param listaProcesos[] lista de los procesos
    *   @param procesosFinalizados[] array de los procesos finalizados
    */
    int[] despacharProceso(int contProcesosAct[], String[] listaProcesosDespachar, Procesos[] listaProcesos, String[] procesosFinalizados, Registro[] tablaPaginas) throws InterruptedException{
        int indiceProcesoDespachar = 0,  pos = 0; //indices
        int contReferencias = 0, totalReferenciasFinal = 3; //contadores
        int aux = 0, aux2=0; //auxiliares
        int[] refTotales = new int[TOTAL_PAG];
        String aux_arg = "";
            //se busca el indice del registro para contar la invocaciones
            for(int i = 0; i < contProcesosAct[0]; i++){
                    for(int j = 0; j < contProcesosAct[0]; j++){
                            if(listaProcesosDespachar[i] == listaProcesos[j].nombre){ 
                                    // Cuenta el numero de referencias de cada proceso
                                for(int l = 0; l < listaProcesos[j].orden.length(); l++){
                                    if(listaProcesos[j].orden.charAt(l) == ','){
                                        contReferencias++;
                                    }
                                }
                                refTotales[i] = contReferencias;
                                contReferencias = 0;
                            }
                        break;
                    }
            }
        // Ordena de manera ascendente la lista de procesos a despachar, registro de tabla de paginas
        for(int i = 0; i < contProcesosAct[0]; i++){
            pos = i;
            aux = refTotales[i];
            aux_arg = listaProcesosDespachar[i];
            aux2 = registroTablaPaginas[i];

            while((pos > 0) && (refTotales[pos-1] > aux)){
                refTotales[pos] = refTotales[pos-1];
                listaProcesosDespachar[pos] = listaProcesosDespachar[pos-1];
                registroTablaPaginas[pos] = registroTablaPaginas[pos-1];
                pos--;
            }
            refTotales[pos] = aux;
            listaProcesosDespachar[pos] = aux_arg;
            registroTablaPaginas[pos] = aux2;
        }
        // Se busca el indice del proceso a despachar en el registro de procesos
        for(int i = 0; i < contProcesosAct[0]; i++){
            if(listaProcesosDespachar[0] == listaProcesos[i].nombre){ 
                indiceProcesoDespachar = i;
                break;
            }
        }
        while(totalReferenciasFinal > 0)
            totalReferenciasFinal = quantum(contProcesosAct, indiceProcesoDespachar, memFisica, memorias, procesosFinalizados, listaProcesos, tablaPaginas, listaProcesosDespachar); //le pasa el indice que tiene el proceso en la variable Registro[TOTAL_P];
        if(totalReferenciasFinal == 0){//si el proceso ha sido eliminado, se decrementa el contador que lleva el control del total de proceso existentes
            contProcesosAct[0]--;
        }
        return contProcesosAct;
    }
}