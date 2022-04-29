import java.io.Serializable;

/**
* Docs Paquete  CLIENTE
* @code Paquete que se envia al servidor
* proceso Es el proceso que se guarda en el paquete
* procesoExiste es la bandera que controla, sin existe un proceso en el paquete
*/
public class Paquete implements Serializable {
    Procesos proceso;
    boolean procesoExiste;
    public Paquete(Procesos proceso, boolean procesoExiste){
        this.proceso = proceso;
        this.procesoExiste = procesoExiste;
    }
    public void setProceso(Procesos proceso){
        this.proceso = proceso;
    }
    public void setProcesoExiste(boolean procesoExiste){
        this.procesoExiste = procesoExiste;
    }
    public Procesos getProceso(){
        return this.proceso;
    }
    public boolean getProcesoExiste(){
        return this.procesoExiste;
    }
}

//TERMINADO