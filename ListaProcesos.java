public class ListaProcesos extends Thread{
    boolean[] estadoProcesos;
    String[] procesosCliente;
    Datos datos;
    public ListaProcesos(Datos datos){
        this.datos = datos;
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
                        for(int i = 0; i < contPaquetesAct[0]; i++){//se elimina de las estructuras ordenadas
                            listaProcesosDespachar[i] = listaProcesosDespachar[i+1];
                            registroTablaPaginas[i] = registroTablaPaginas[i+1];
                            if(i == (TOTAL_P-1)){
                                for(j = 0; j < listaProcesosDespachar[i].length(); j++)
                                    listaProcesosDespachar[i] = "";
                                registroTablaPaginas[i] = 0;
                            }
                        }
                        for(int i = indiceProcesoDespachar; i < contPaquetesAct[0]; i++){//se elimina de la estructura registro
                            listaProcesos[i].nombre = listaProcesos[i+1].nombre;
                            listaProcesos[i].totalPaginas = listaProcesos[i+1].totalPaginas;
                            listaProcesos[i].orden = listaProcesos[i+1].orden;
                            if(i == (TOTAL_P-1)){
                                listaProcesos[i].nombre = "";
                                listaProcesos[i].totalPaginas = 0;
                                listaProcesos[i].orden = "";
                            }
                        }
                    }
                }
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    }
}