public class ProcesoNuevo extends Thread{//solicita al servidor un nuevo proceso.
    Datos datos;
    public ProcesoNuevo(Datos datos){
        this.datos = datos;
    }
    public void run(){
        while(true){
            try{
                sInterfaz.actualizar(datos.getContRefTotales(),datos.getProcesosFinalizados());
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    }
}