/**
* @code Clase Proceso del cliente
* nombre nombre proceso
* totalPaginas  total de paginas que tendra el proceso (diferente al total de invocaciones)
* orden  orden de las invocaciones 
* n_inv  numero de invocaciones (solo usada en una funcion)
*/

public class Procesos{
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
}
//TERMINADO