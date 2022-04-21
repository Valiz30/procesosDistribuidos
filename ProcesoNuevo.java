public class ProcesoNuevo extends Thread{//solicita al servidor un nuevo proceso.
    Datos datos;
    Paquete paqueteNuevo;
    int[] contadorProcesoAct = {}, contProcesosFinalizados = {}, contadorTabla = {};
    String[] listaProcesosDespachar, procesosFinalizados;
    int[] registroTablaPaginas;
    Procesos[] listaProcesos;
    SimuladorInterfaz sInterfaz;
    public ProcesoNuevo(Datos datos, SimuladorInterfaz sInterfaz){
        this.datos = datos;
        this.sInterfaz = sInterfaz;
        contadorTabla[0] = 0;
    }
    public void run(){
        while(true){
            try{
                paqueteNuevo = sInterfaz.actualizar(datos.getContRefTotales(), datos.getProcesosFinalizados());
                if(paqueteNuevo.procesoExiste == true){
                    contProcesosAct = datos.getContProcesosAct();//verificar procesos existentes
                    listaProcesosDespachar = datos.getListaProcesosDespachar();
                    registroTablaPaginas = datos.getRegistroTablaPaginas();
                    listaProcesos = datos.getListaProcesos();
                    try {
                        registrar(paqueteNuevo, contProcesosAct, listaProcesosDespachar, registroTablaPaginas, ListaProcesos);
                    } catch (InterruptedException ex){
                        Logger.getLogger(SimuladorPrompt.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                for(int i = 0; i < contProcesosFinalizados[0], i++){
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
    Procesos registrarDatos(Paquete paqueteNuevo, String[] listaProcesosDespachar, int[] contProcesosAct){//se tiene que lanzar como promt 
        procesoNuevo.nombre = paqueteNuevo.proceso.nombre;
        procesoNuevo.totalPaginas = paqueteNuevo.proceso.totalPaginas;
        procesoNuevo.orden = paqueteNuevo.proceso.orden;
        procesoNuevo.n_inv = paqueteNuevo.proceso.n_inv;
        listaProcesosDespachar[contProcesosAct[0]] = procesoNuevo.nombre ;
	    return procesoNuevo;
    }
    //Anade las paginas del proceso nuevo a la tabla de paginas
    int anadirProcesoTablaPaginas(int[] contProcesosAct, int[] contadorTabla, Procesos procesoNuevo, int[] registroTablaPaginas){
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
        return contadorTabla[0];
    }

    void registrar(Paquete paqueteNuevo, int[] contadorProcesoAct, String[] listaProcesosDespachar, int[] registroTablaPaginas, Procesos[] listaProcesos) throws InterruptedException{
        int contRefTotales = 0;
        procesoNuevo = registrarDatos(paqueteNuevo, listaProcesosDespachar, contProcesosAct);//pide datos de proceso
        contadorTabla[0] = anadirProcesoTablaPaginas(contProcesosAct, contadorTabla, procesoNuevo, registroTablaPaginas);//se añaden las paginas a la tabla de paginas            contProcesosAct[0]++;
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