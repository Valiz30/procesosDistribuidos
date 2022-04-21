public class ListaProcesos extends Thread{
    int TOTAL_PROCESOS = 30;
    boolean[] estadoProcesos;
    String[] procesosCliente;
    int[] contProcesosCliente;
    Datos datos;
    SimuladorInterfaz sInterfaz;
    public ListaProcesos(Datos datos, SimuladorInterfaz sInterfaz){
        this.datos = datos;
        this.sInterfaz = sInterfaz;
    }
    public void run(){
        while(){
            try{
                estadoProcesos = sInterfaz.actualizarProceso(datos.getProcesosCliente);
                procesosCliente = datos.getProcesosCliente();
                for(int i = 0; i < estadoProcesos.length; i++){
                    if(estadoProcesos[i] == true){
                        System.out.println(x + "|El proceso"+procesosClientes[i]+ "ha terminado de ejecutarse") ;
                        // Se elimina el proceso que marque el indice y se acomoda el registro
                        contProcesosCliente = datos.getContProcesosCliente();
                        for(int j = i; j < contProcesosCliente[0]; j++){//se elimina del registro de procesos del cliente.
                            procesosCliente[j].nombre = procesosCliente[j+1].nombre;
                            if(i == (TOTAL_PROCESOS - 1)){
                                procesosCliente[j].nombre = "";
                            }
                        }
                        contProcesosCliente[0]--;
                        break;
                    }
                }
                datos.setProcesosCliente(procesosCliente);
                datos.setContProcesosCliente(contProcesosCliente);
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    }
}