public class Procesos{ /*Entradas de usuario*/
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