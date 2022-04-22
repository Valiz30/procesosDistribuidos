/**
* Docs Paquete 
* @code Paquete que se envia al servidor
* proceso Es el proceso que se guarda en el paquete
* procesoExiste es la bandera que controla, sin existe un proceso en el paquete
*/
public class Paquete {
    Procesos proceso;
    boolean procesoExiste;
    public Paquete(Procesos proceso, boolean procesoExiste){
        this.proceso = proceso;
        this.procesoExiste = procesoExiste;
    }
}

//TERMINADO