public class Registro{
    /**
    *   Docs de Registro
    * Entradas de la tabla de paginas
    * @code toma los parametros como entrada de la tabla de paginas, tomandolos como bit.
    * haciendo referencia a que cada variable es un bit, excetuando el num_marco que se
    * establece como el marco donde se encuentra la pagina
    */
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

//TERMINADO