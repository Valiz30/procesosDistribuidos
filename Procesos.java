import java.io.Serializable;

/**
* @code Clase Proceso del cliente  CLIENTE
* nombre nombre proceso
* totalPaginas  total de paginas que tendra el proceso (diferente al total de invocaciones)
* orden  orden de las invocaciones 
* n_inv  numero de invocaciones (solo usada en una funcion)
*/
import java.io.Serializable;
public class Procesos implements Serializable{
    String nombre;  
    int totalPaginas;
    String orden;  
    int n_inv;
    public Procesos(String nombre, int totalPaginas, String orden, int n_inv){
        this.nombre = nombre;
        this.totalPaginas = totalPaginas;
        this.orden = orden;
        this.n_inv = n_inv;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setTotalPaginas(int totalPaginas){
        this.totalPaginas = totalPaginas;
    }
    public void setOrden(String orden){
        this.orden = orden;
    }
    public void setNumInvocaciones(int n_inv){
        this.n_inv = n_inv;
    }
    public String getNombre(){
        return this.nombre;
    }
    public int getTotalPaginas(){
        return this.totalPaginas;
    }
    public String getOrden(){
        return this.orden;
    }
    public int getNumInvocaciones(){
        return this.n_inv;
    }
}
//TERMINADO