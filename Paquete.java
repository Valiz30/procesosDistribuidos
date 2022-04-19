public class Paquete {
    int identificador;
    Procesos proceso;
    boolean procesoExiste;//banderas
    public Paquete(int identificador, Procesos proceso, boolean procesoExiste){
        this.identificador = identificador;
        this.proceso = proceso;
        this.procesoExiste = procesoExiste;
    }
}