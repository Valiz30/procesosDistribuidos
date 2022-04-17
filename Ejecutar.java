import static java.lang.Math.*;
import java.util.concurrent.TimeUnit;

class Memorias{ /*Entradas de usuario*/
    int tamPag;  //especifica el tamaño de la pagina en bytes
    int tamMemVir; //especifica el tamaño de la Memoria Virtual en bytes
    int tamMemFis; //especifica el tamaño de la Memoria Fisica en bytes
    public Memorias(int tamPag, int tamMemVir, int tamMemFis){
        this.tamPag = tamPag;
        this.tamMemVir = tamMemVir;
        this.tamMemFis = tamMemFis;
    }
}
class Procesos{ /*Entradas de usuario*/
    String nombre;  //nombre proceso
    int totalPaginas;  //total de paginas que tendra el proceso (diferente al total de invocaciones)
    String orden;    // orden de las invocaciones 
    int n_inv;		//numero de invocaciones (solo usada en una funcion)
    public Procesos(String nombre, int totalPaginas, String orden, int n_inv){
        this.nombre = nombre;
        this.totalPaginas = totalPaginas;
        this.orden = orden;
        this.n_inv = n_inv;
    }
}
class Registro{ /*Entradas de la tabla de paginas*/
    int referida;     // bit
    int modificada;   // bit
    int proteccion;   // bit
    int presen_ausen; // bit
    int num_marco;    // marco donde se encuentra la pagina
    public Registro(int referida, int modificada, int proteccion, int presen_ausen, int num_marco){
        this.referida = referida;
        this.modificada = modificada;
        this.proteccion = proteccion;
        this.presen_ausen = presen_ausen;
        this.num_marco = num_marco;
    }
}
public class Ejecutar{
    int TOTAL_P = 30, TOTAL_PAG = 50, AUX = 50;
    String[] listaProcesosDespachar = new String[30]; //arreglo que contiene el orden de los procesos a despachar
    int[] registroTablaPaginas = new int[30]; //arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][])
    Procesos[] proceso = new Procesos[30];
    Memorias memorias;
    Registro[] tablaPaginas = new Registro[50];
    public Ejecutar(){
        
    }
    //funcion para traducir de decimal a binario
    public void decimalBinario(int decimal, int binario[], int totalBits){ 
    float cociente, residuo;
    int i, valor = decimal, cont = totalBits-1, k;
    for(k = 0; k < totalBits; k++){//inicializa en 0's el arreglo donde se almacenara el numero en binario
	    binario[k] = 0;
    }                                        
    for(i = 0; i < totalBits-1; i++){ //genera los bits representativos del valor decimal
        residuo = decimal % 2;
        decimal = decimal / 2;
        binario[cont] = (int)residuo;
        cont--;
    }
    binario[cont] = decimal;
}
    //funcion para traducir de binario a decimal
    int binarioDecimal(int binario[], int totalValores){
        int decimal = 0, i, cont = 0;
        for(i = totalValores - 1; i >= 0; i--){
            if(binario[i] == 1)
                decimal = (int) (decimal + pow(2,cont));//se basa en la elevacion al cuadrado segun la posicion y si el "bit" esta encendido
            cont++;
        }
        return decimal;
    }
    //funcion que pasa de binario a Hexadecximal
    void binarioHexadecimal(int dirBin[], int bitsBin, char dirHex[]){
        int i, cont = 3, valor, cont2 = 0, j;
        int[] aux = new int[4], dirAux = new int[30];
        for(i = 0; i < 4; i++){//inicializa un arreglo auxiliar en 0's, sera para almacenar en grupos de 4 bits y pasarlo a hecadecimal
            aux[i] = 0;
        }
        for(i = bitsBin - 1; i >= 0; i--){
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
                for(j = 0; j < 4; j++){
                    aux[j] = 0;
                }
            }
        }
        dirHex[cont2] = 'G';
        dirAux[cont2] = 'G';//se usa una G para indicar el final del numero Hexadecimal
        cont = 0;
        for(i = cont2 - 1; i >= 0; i--){
            dirHex[cont] = (char)dirAux[i];
            cont++;
        }
    }
    //pide datos de entrada: Respecto al proceso
    int pedirDatos(int cont){//se tiene que lanzar como promt 
	char c;
	int aux = 1, i = 0, total = 0, j = 0, total_referencias;
	cont++;//la variable cont se usara para poder llevar un control del total de proceso añadidos por el usuario
	return cont;
    }
    //llenar las estructuras correspondientes
    void anadirProcesoEstructuras(int contador){
	listaProcesosDespachar[contador-1] = proceso[contador-1].nombre;//anade el nuevo proceso a la lista de proceso a despachar
    }
    //calcula la direccion en base 2, 10 y 16
    void direccionBases(int pagmar, int bitspagmar, int desplazamiento, int bitsDesplazamiento, int dirBin[], /*int* dirDec,*/ char dirHex[]){ //que se modifique la variable
        int i, cont = 0, aux = 9;
        int[] aux1 = new int[bitspagmar], aux2 = new int[bitsDesplazamiento];
        decimalBinario(pagmar,aux1,bitspagmar);//pasa a binario el indice de pagina o el indices de marco, segun se solicite
        decimalBinario(desplazamiento,aux2,bitsDesplazamiento);//pasa a binario el desplazamiento de la pagina o del marco, segun se solicite
        for(i = 0; i < bitspagmar; i++){//añade al arreglo dirBin[], primero los bits de la pagina o el marco
            dirBin[cont] = aux1[i];
            cont++;
        }
        for(i = 0; i < bitsDesplazamiento; i++){//añade al mismo arreglo dirBin[], los bits de desplazamiento
            dirBin[cont] = aux2[i];
            cont++;
        }
        aux = binarioDecimal(dirBin,bitspagmar+bitsDesplazamiento);//pasa de binario a decimal
        //*dirDec = aux;//se le asigna a la variable correspondiente el valor decimal
        binarioHexadecimal(dirBin, bitspagmar + bitsDesplazamiento, dirHex);//pasa de binario a Hexadecimal
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
    //Cuenta el numero de bits que se necesita para poder direccionar un valor numerico
    int contadorBits(int numBytes){
            int bits = 0, i = 0;
            for (i = 0; numBytes != 1; i++){
                    numBytes = numBytes / 2;
                    bits++;
            }
            return bits;
    }
    //traduce los datos pasados como parametos en sus correspondientes bases, regresa ya sea la direccion virtual o la direccion fisica
    /*void traducir(int pagmar, int bitspagmar, int desplazamiento, int bitsDesplazamiento, int dirBin[], int dirDec, char dirHex[]){
            int i;
            direccionBases(pagmar, bitspagmar, desplazamiento, bitsDesplazamiento, dirBin, &dirDec, dirHex); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
            printf("\n Binaria: ");
        for(i = 0; i < bitsDesplazamiento + bitspagmar; i++){
            printf("%i", dirBin[i]);
        }
        printf("\n Decimal: %d", dirDec);
        printf("\n Hexadecimal: ");
        for(i = 0; i < 30; i++){
            if(dirHex[i] != 'G'){
                printf("%c", dirHex[i]);
            }else{
                break;
            } 
        }
            printf("\n");
            // Imprime dirBin, dirDec, dirHex	
    }*/
    //hace la siguiente referencia del proceso en cuestion
    void realizarReferencia(int indiceProceso, int memFisica[]){
            int pagmar, desplazamiento, bitspagmar, bitsDesplazamiento, dirDec, memFi;
            int[] dirBin = new int[30];
            char[] dirHex = new char[30];
            for (memFi = 0; memFi < memorias.tamMemFis; memFi++)//imprime la estructura de la memoria fisica, cada bit representa un byte
                    //printf("|%d", memFisica[memFi]);

            pagmar = 0; //va a contener el valor entero ya sea, del indice de la pagina o del indice del marco para pagina
            desplazamiento = 0;//va a contener el valor entero del desplazamiento
            bitspagmar = 0;//contendra los bits necesarios para direccionar ya sea una pagina o un marco
            bitsDesplazamiento = 0;//contendra el total de bits para direccionar el desplazamiento

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
            nombre = proceso[indiceProceso].nombre;
            int i = 0, indice = 0;
            for (i = 0; i < 3; i++){
                    if (listaProcesosDespachar[i] == nombre){
                            //printf("\n\n Proceso encontrado: %s", listaProcesosDespachar[i]);
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
            registroOrdenRemplazo = proceso[indiceProceso].orden.toCharArray();
            /* Encontrar el primer Orden desplazamiento*/
            int indiceFinal = 0;
            for(int k = 0; k < registroOrdenRemplazo.length; k++){
                if(registroOrdenRemplazo[k] == ',');
                    indiceFinal = k;
            }
            char[] ordenDesplazamiento = proceso[indiceProceso].orden.substring(0, indiceFinal).toCharArray();//almacena la primera invocacion
            /*Longitud del arreglo encontrado ordenDesplazamiento*/
            int longOrdenDes = 1; /* El tamaño toma en cuenta la coma, incrementando a 1 */
            i = 0;
            longOrdenDes += longitudCadena(ordenDesplazamiento);
            /*Separar Orden de desplazamiento (Tomar en cuenta el 1 añadido)*/
            char[] desplazamientoSep = new char[5];// = {'\0'};
            char[] numeroPaginaSep = new char[2]; // = {'\0'};
            int bandera = 0;
            /**/
            /*Separacion de varibale*/
            int contadorArreglo = 0;
            for (i = 0; i < longOrdenDes - 1; i++){
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
            /*Convertir la cadenas a int, tanto el numero de pagina como el numero de desplazamiento*/
            numpaginaInt = cadenaToEntero(numeroPaginaSep);
            desplazamientoInt = cadenaToEntero(desplazamientoSep);
            //printf("\n Num. Pagina : %d, Desplazamiento : %d\n", numpaginaInt, desplazamientoInt);

            /*******************************************************************************************************/

            /* número  de  página  espacio desplazamiento. "4 56"	*/
            /* se Usara -> numpaginaInt, desplazamientoInt */

            /*Imprimir Direccion Virtual*/
            pagmar = numpaginaInt;
            bitspagmar = bitsNumPagVirtual;
            desplazamiento = desplazamientoInt;
            /* bitsDesplazamiento se declaro al inicio */
            //printf(AZUL_T"\n Direccion Virtual"RESET_COLOR);
            //traducir(pagmar, bitspagmar, desplazamiento, bitsDesplazamiento, dirBin, /*dirDec,*/ dirHex); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
            /*********************************/
            /*Buscara la pagina en memoria fisica*/
            if (tablaPaginas[posPagToReferencia+numpaginaInt].presen_ausen == 0){ /*No se encuentra*/
                    //printf(MAGENTA_T "\n FALLO DE PAGINA\n " RESET_COLOR);
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
                            pagmar = marcoPaginaIndice;
                            bitspagmar = bitsMarcoFisica;
                            /* desplazamiento se declaro en Imprimir direccion virtual */
                            /* bitsDesplazamiento se declaro al inicio */
                            //printf(AZUL_T"\n Direccion Fisica"RESET_COLOR);//se imprime la direccion fisica despues de ingresar la pagina a memoria
                            //traducir(pagmar, bitspagmar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
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

                            pagmar = tablaPaginas[posPagToReferencia].num_marco;
                            bitspagmar = bitsMarcoFisica;
                            /* desplazamiento se declaro en Imprimir direccion virtual */
                            /* bitsDesplazamiento se declaro al inicio */
                            /* Imprimir la direccion fisica*/
                            //printf(MAGENTA_T "\n FALLO DE PAGINA\n " RESET_COLOR);
                            //printf(AZUL_T"\n Direccion Fisica"RESET_COLOR);
                            //traducir(pagmar, bitspagmar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
                    }
            }
            else{	/*Se encuentra en memoria fisica la pagina- Imprimir la memoria Fisica*/
                    /* Imprime Mapa de bits*/
                    //printf(AZUL_T"\n MAPA - MEMORIA FISICA \n"RESET_COLOR);
                    for (memFi = 0; memFi < memorias.tamMemFis; memFi++)
                            //printf("|%d", memFisica[memFi]);

                    /*Imprimir la direccion fisica*/
                    pagmar = tablaPaginas[posPagToReferencia].num_marco;
                    bitspagmar = bitsMarcoFisica;
                    /* desplazamiento se declaro en Imprimir direccion virtual */
                    /* bitsDesplazamiento se declaro al inicio */
                    //printf(MAGENTA_T "\n YA ESTABA EN MEMORIA FISICA " RESET_COLOR);
                    //printf(AZUL_T"\n Direccion Fisica"RESET_COLOR);
                    //traducir(pagmar, bitspagmar, desplazamiento, bitsDesplazamiento, dirBin, dirDec, dirHex); // pasara # de pagina, # marco, valor desplazamiento y los arreglos donde se almacenaran las bases
                    tablaPaginas[posPagToReferencia].referida = 1;
            }
            /*- debera de modificar el la entrada que le pertenece a dicho proceso en la tabla de paginas 	*/

            /*Eliminar la invocacion de la variable registro[indiceProceso] */
            /* El 0 se cambia por indiceProceso*/
            String vacio = proceso[indiceProceso].orden.substring(indiceFinal);
            proceso[indiceProceso].orden = vacio;
            /**************************/
    }
    //hace la simulacion del quantum -> 3 segundo -> 1 segundo = 1 referencia
    int quantum(int contador, int indice_proceso, int memFisica[]) throws InterruptedException{
        int cont = 0, j = 0, total_referencias;
        //calcula el total de invocaciones restantes
        for(j = 0; j < proceso[indice_proceso].orden.length(); j++){
            if(proceso[indice_proceso].orden.charAt(j) == ','){
                cont++;
            }
        }
        total_referencias = cont;
        cont = 1;
        //ciclo donde se hara la simulacion del quantum
        while(total_referencias > 0 && cont <= 3){
            realizarReferencia(indice_proceso, memFisica); //solo hace una referencia
            total_referencias--;
            TimeUnit.SECONDS.sleep(1);
            cont++;
        }  
        j = 0;
        cont = 0;
        //calcula el total de invocaciones restantes
        for(j = 0; j < proceso[indice_proceso].orden.length(); j++){
            if(proceso[indice_proceso].orden.charAt(j) == ','){
                cont++;
            }
        }
        total_referencias = cont;
        if(total_referencias == 0){//si ya no hay mas referencias y/o invocaciones pendientes en el proceso 
            //printf("\n Se eliminara el %s (Invocaciones terminadas)\n", registro[indice_proceso].nombre);
            // Se elimina el proceso que marque el indice y se acomoda el registro
            for(int i = 0; i < contador; i++){//se elimina de las estructuras ordenadas
                listaProcesosDespachar[i] = listaProcesosDespachar[i+1];
                registroTablaPaginas[i] = registroTablaPaginas[i+1];
                if(i == (TOTAL_P-1)){
                    for(j = 0; j < listaProcesosDespachar[i].length(); j++)
                        listaProcesosDespachar[i] = "";
                    registroTablaPaginas[i] = 0;
                }
            }
            for(int i = indice_proceso; i < contador; i++){//se elimina de la estructura registro
                proceso[i].nombre = proceso[i+1].nombre;
                proceso[i].totalPaginas = proceso[i+1].totalPaginas;
                proceso[i].orden = proceso[i+1].orden;
                if(i == (TOTAL_P-1)){
                    proceso[i].nombre = "";
                    proceso[i].totalPaginas = 0;
                    proceso[i].orden = "";
                }
            }
        }
            return total_referencias;
    }
    //despacha al proceso dentro de la opcion 2 del menu
    int despacharProceso(int contador, int memFisica[]) throws InterruptedException{
        int indice_proceso = 0, cont = 0, aux=0, pos, aux2=0, total_referencias = 3;
        int[] total_ref = new int[TOTAL_PAG];
        String aux_arg = "";
        Procesos aux_proc;
            //su busca el indice del registro para contar la invocaciones
            for(int i = 0; i < contador; i++){
                    for(int j = 0; j < contador; j++){
                            if(listaProcesosDespachar[i] == proceso[j].nombre){ 
                                    // Cuenta el numero de referencias de cada proceso
                                for(int l = 0; l < proceso[j].orden.length(); l++){
                                    if(proceso[j].orden.charAt(l) == ','){
                                        cont++;
                                    }
                                }
                                total_ref[i] = cont;
                                cont = 0;
                            }
                        break;
                    }
            }

        
        /*printf("Total de referencias: ");
        for(int i = 0; i < contador; i++){
            printf("%i ", total_ref[i]);
        }
        printf("\n");*/

        // Ordena de manera ascendente la lista de procesos a despachar, registro de tabla de paginas
        for(int i = 0; i < contador; i++){
            pos = i;
            aux = total_ref[i];
            aux_arg = listaProcesosDespachar[i];
            aux2 = registroTablaPaginas[i];

            while((pos > 0) && (total_ref[pos-1] > aux)){
                total_ref[pos] = total_ref[pos-1];
                listaProcesosDespachar[pos] = listaProcesosDespachar[pos-1];
                registroTablaPaginas[pos] = registroTablaPaginas[pos-1];
                pos--;
            }
            total_ref[pos] = aux;
            listaProcesosDespachar[pos] = aux_arg;
            registroTablaPaginas[pos] = aux2;
        }
        // Se busca el indice del proceso a despachar en el registro de procesos
        for(int i = 0; i < contador; i++){
            if(listaProcesosDespachar[0] == proceso[i].nombre){ 
                indice_proceso = i;
                break;
            }
        }
        total_referencias = quantum(contador, indice_proceso, memFisica); //le pasa el indice que tiene el proceso en la variable Registro[TOTAL_P];
        if(total_referencias == 0){//si el proceso ha sido eliminado, se decrementa el contador que lleva el control del total de proceso existentes
            contador--;
        }
        return contador;
    }
    //Anade las paginas del proceso nuevo a la tabla de paginas
    int anadirProcesoTablaPaginas(int contador, int contadorTabla){
        int i;
        registroTablaPaginas[contador - 1] = contadorTabla;
        for(i = contadorTabla; i <= contadorTabla + proceso[contador - 1].totalPaginas; i++){ 
            tablaPaginas[i].referida = 0;
            tablaPaginas[i].modificada = 0;
            tablaPaginas[i].proteccion = 0;
            tablaPaginas[i].presen_ausen = 0;
            tablaPaginas[i].num_marco = 0;
        }
        contadorTabla = contadorTabla + proceso[contador - 1].totalPaginas;
        return contadorTabla;
    }
    //menu del programa
    Proceso prompt(int contProcesosAct,memorias) throws InterruptedException{
        Procesos procesoNuevo; //proceso que reciba el cliente
        int opc = 0, contador = 0,contadorTabla = 0;
            int[] memFisica = new int[memorias.tamMemFis]; //variable de la memoria fisica
            int memFi = 0;
            for (memFi = 0; memFi < memorias.tamMemFis; memFi++)
                    memFisica[memFi] = 0;
        while (opc != 3){
            //prompt
            switch (opc){
            case 1: /*Añadir nuevo proceso*/
            contProcesosAct = pedirDatos(contProcesosAct);//pide datos de proceso
                contadorTabla = anadirProcesoTablaPaginas(contProcesosAct, contadorTabla);//se añaden las paginas a la tabla de paginas
                anadirProcesoEstructuras(contProcesosAct);//se añade el proceso a la estructura ya indicada
                break;

            case 2: /*Despachar proceso*/
                if(contProcesosAct > 0){
                    contProcesosAct = despacharProceso(contProcesosAct, memFisica);
                }else{
                    //printf(ROJO_T"\n No hay procesos.\n"RESET_COLOR);
                }
                break;
            default:
                break;
            }
        }
        return procesoNuevo;  
    }
}
