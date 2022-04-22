public class ProcesoNuevo extends Thread{
    /**
    * Docs ProcesoNuevo
    * @code Envia la carga y los procesos que ya finalizaron
    */
    Datos datos;
    Paquete paqueteNuevo;
    int[] contProcesosAct = {}, contProcesosFinalizados = {}, contadorTabla = {},registroTablaPaginas;
    String[] listaProcesosDespachar, procesosFinalizados;
    Procesos[] listaProcesos;
    SimuladorInterfaz sInterfaz;
    Registro[] tablaPaginas = new Registro[100];
    public ProcesoNuevo(Datos datos, SimuladorInterfaz sInterfaz){
        this.datos = datos;
        this.sInterfaz = sInterfaz;
        contadorTabla[0] = 0;
    }
    /**
    * Docs ProcesoNuevo
    * @code Actua como hilo para el envio de la carga y los procesos finalizados
    */
    public void run(){
        while(true){
            try{
                paqueteNuevo = sInterfaz.actualizar(datos.getContRefTotales(), datos.getProcesosFinalizados(), datos.getContProcesosFinalizados());
                if(paqueteNuevo.procesoExiste == true){
                    contProcesosAct = datos.getContProcesosAct();//verificar procesos existentes
                    listaProcesosDespachar = datos.getListaProcesosDespachar();
                    registroTablaPaginas = datos.getRegistroTablaPaginas();
                    listaProcesos = datos.getListaProcesos();
                    tablaPaginas = datos.getTablaPaginas();
                    registrar(paqueteNuevo, contProcesosAct, listaProcesosDespachar, registroTablaPaginas, listaProcesos, tablaPaginas);
                }
                for(int i = 0; i < contProcesosFinalizados[0]; i++){
                    procesosFinalizados[i] = ""; 
                }
                contProcesosFinalizados[0] = 0;
                datos.setContProcesosFinalizados(contProcesosFinalizados);
                datos.setprocesosFinalizados(procesosFinalizados);
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    }
    /**
    * anadirProcesosTablaPaginas
    * @code registra los Datos en u nnuevo Proceso y lo anade a listaprocesosdespacahar
    * @param paqueteNuevo guarda un nuevo paquete
    * @param listaProcesosDespachar lista de procesos a despachar
    * @param contProcesosAct contador de los procesos Actuales
    * @return procesoNuevo - Procesos
    */
    Procesos registrarDatos(Paquete paqueteNuevo, String[] listaProcesosDespachar, int[] contProcesosAct){//se tiene que lanzar como promt 
        Procesos procesoNuevo = new Procesos(paqueteNuevo.proceso.nombre, paqueteNuevo.proceso.totalPaginas, paqueteNuevo.proceso.orden, paqueteNuevo.proceso.n_inv);
        listaProcesosDespachar[contProcesosAct[0]] = procesoNuevo.nombre ;
	    return procesoNuevo;
    }
    /**
    * anadirProcesosTablaPaginas
    * @code Anade las paginas del proceso nuevo a la tabla de paginas
    * @param contProcesosAct contador de los procesos actuales
    * @param contadorTabla contador de la tabla
    * @param procesoNuevo se almacena el proceso
    * @param registroTablaPaginas indice de la tabla de paginas
    * @param tablaPaginas tabla de paginas
    */
    int anadirProcesoTablaPaginas(int[] contProcesosAct, int[] contadorTabla, Procesos procesoNuevo, int[] registroTablaPaginas, Registro[] tablaPaginas){
        int i;
        registroTablaPaginas[contProcesosAct[0]] = contadorTabla[0];
        for(i = contadorTabla[0]; i <= contadorTabla[0] + procesoNuevo.totalPaginas; i++){ 
            tablaPaginas[i].referida = 0;
            tablaPaginas[i].modificada = 0;
            tablaPaginas[i].proteccion = 0;
            tablaPaginas[i].presen_ausen = 0;
            tablaPaginas[i].num_marco = 0;
        }
        contadorTabla[0] = contadorTabla[0] + procesoNuevo.totalPaginas;
        datos.setTablaPaginas(tablaPaginas);
        return contadorTabla[0];
    }
    /**
    * registrar
    * @code Registra los procesos en lista Procesos
    * @param paqueteNuevo contador de los procesos actuales
    * @param contadorProcesoAct contador de la tabla
    * @param listaProcesosDespachar se almacena el proceso
    * @param listaProcesos lista de los procesos
    * @param tablaPaginas tabla de paginas
    */
    Procesos registrar(Paquete paqueteNuevo, int[] contadorProcesoAct, String[] listaProcesosDespachar, int[] registroTablaPaginas, Procesos[] listaProcesos, Registro[] tablaPaginas) throws InterruptedException{
        int contRefTotales = 0;
        Procesos procesoNuevo;
        procesoNuevo = registrarDatos(paqueteNuevo, listaProcesosDespachar, contProcesosAct);//pide datos de proceso
        contadorTabla[0] = anadirProcesoTablaPaginas(contProcesosAct, contadorTabla, procesoNuevo, registroTablaPaginas, tablaPaginas);//se añaden las paginas a la tabla de paginas            contProcesosAct[0]++;
        listaProcesos[contadorProcesoAct[0]] = procesoNuevo;
        for(int i = 0; i < contadorProcesoAct[0]; i++){
            contRefTotales = contRefTotales + listaProcesos[i].n_inv; 
        }
        contadorProcesoAct[0]++;
        datos.setContRefTotales(contRefTotales);
        datos.setContProcesosAct(contProcesosAct);
        datos.setListaProcesos(listaProcesos);
        return procesoNuevo;  
    }
}
//TERMINADO