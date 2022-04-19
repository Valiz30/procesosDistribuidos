public class Memorias{ /*Entradas de usuario*/
    int tamPag;  //especifica el tamaño de la pagina en bytes
    int tamMemVir; //especifica el tamaño de la Memoria Virtual en bytes
    int tamMemFis; //especifica el tamaño de la Memoria Fisica en bytes
    public Memorias(int tamPag, int tamMemVir, int tamMemFis){
        this.tamPag = tamPag;
        this.tamMemVir = tamMemVir;
        this.tamMemFis = tamMemFis;
    }
}