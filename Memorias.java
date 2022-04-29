/**
* Docs Memorias  CLIENTE
* @code informacion sobre la memoria utilizada
* tamPag - especifica el tamaño de la pagina en bytes
* tamMemVir - especifica el tamaño de la Memoria Virtual en bytes
* tamMemFis - especifica el tamaño de la Memoria Fisica en bytes
*/
public class Memorias{ /*Entradas de usuario*/
    int tamPag;  
    int tamMemVir;
    int tamMemFis; 
    public Memorias(int tamPag, int tamMemVir, int tamMemFis){
        //Asignacion de los valores a la clase
        this.tamPag = tamPag;
        this.tamMemVir = tamMemVir;
        this.tamMemFis = tamMemFis;
    }
}

// TERMINADO