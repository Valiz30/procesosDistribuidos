import java.util.logging.Level;
import java.util.logging.Logger;
public class ProcesoNuevo extends Thread{
    /**
    * Docs ProcesoNuevo CLIENTE
    * @code Envia la carga y los procesos que ya finalizaron
    * datos - instancia de la clase Datos
    * paqueteNuevo - el paquete que contiene el nuevo proceso
    * contProcesosAct - contador de procesos totales en el cliente
    * contProcesosFinalizados - contador de los procesos que el cliente ha terminado de ejecutar
    * contadorTabla - indice para saber a partir de que entrada de la tabla de pagias se pueden almacenar las paginas de un proceso nuevo
    * registroTablaPaginas - contiene los indices de los procesos en la tabla de paginas
    * listaProcesosDespachar - contiene los nombres de los procesos a ejecutar en orden
    * procesosFinalizados - la lista de procesos finalizados por el cliente
    * nombreHilo - contiene el nombre del hilo
    * listaProcesos - son los procesos pendientes a ejecutar, contiene todos los datos del proceso
    * sInterfaz - instancia de la interfaz de la aplicacion
    * tablaPaginas - es la tabla de paginas que se usara en la ejecucion
    */
    Datos datos;
    Paquete paqueteNuevo;
    int[] contProcesosAct = {0}, contProcesosFinalizados = {0}, contadorTabla = {0},registroTablaPaginas;
    String[] listaProcesosDespachar, procesosFinalizados =  new String[30];
    String nombreHilo = "ProcesoNuevo";
    Procesos[] listaProcesos;
    SimuladorInterfaz sInterfaz;
    Registro[] tablaPaginas = new Registro[100];
    
    public ProcesoNuevo(Datos datos, SimuladorInterfaz sInterfaz){ // Recive Datos de las variables que utilizara el cliente y su interfaz asi como el nombre del cliente
        this.datos = datos;
        this.sInterfaz = sInterfaz;
        contadorTabla[0] = 0;
    }
    /**
    * Docs ProcesoNuevo
    * @code Actua como hilo para el envio de la carga y los procesos finalizados
    */
    public void run(){
        boolean usar;
        while(true){
            usar = true;
            datos.setNombreHilo(nombreHilo); // Establece el nombre del hilo para el uso de las variables del cliente
            while(datos.getNombreHilo() != nombreHilo && usar == true){// duerme hasta que sea su turno
                try {
                    Thread.sleep(1000); // el hilo duerme durante 1 segundo
                    datos.setNombreHilo(nombreHilo); // y reestablece el nombre del hilo
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcesoNuevo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try{
                try {
                    Thread.sleep(50); // el hilo duerme durante 50 milisegundos
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcesoNuevo.class.getName()).log(Level.SEVERE, null, ex);
                }
                //llamada al servidor para solicitar un proceso nuevo y enviar lo procesos finalizados
                paqueteNuevo = sInterfaz.actualizar(datos.getContRefTotales(), datos.getProcesosFinalizados(), datos.getContProcesosFinalizados());
                if(paqueteNuevo.getProcesoExiste() == true){ // verifica si el servidor si le envio un proceso nuevo
                    contProcesosAct = datos.getContProcesosAct();//obtiene el contador de procesos actuales a ejecutar
                    listaProcesosDespachar = datos.getListaProcesosDespachar(); // Consigue la lista de procesos que se despachara
                    registroTablaPaginas = datos.getRegistroTablaPaginas(); // Se obtiene el registro de la tabla de paginas
                    listaProcesos = datos.getListaProcesos(); // obtiene la lista de procesos pendientes a ejecutar
                    tablaPaginas = datos.getTablaPaginas(); // obtiene la tabla de 
                    // Se registra el paquete, con el proceso nuevo
                    registrar(paqueteNuevo, contProcesosAct, listaProcesosDespachar, registroTablaPaginas, listaProcesos, tablaPaginas);
                }
                for(int i = 0; i < contProcesosFinalizados[0]; i++){//limpia los registros de procesos finalizados
                    procesosFinalizados[i] = ""; 
                }
                contProcesosFinalizados[0] = 0;// Se establece el valor inicial del arreglo de contadador procesos
                datos.setContProcesosFinalizados(contProcesosFinalizados);//actualiza el contador de procesos finalizados
                datos.setprocesosFinalizados(procesosFinalizados);//actualiza los nombres de los procesos finalizados
            }catch(Exception e) {
                System.err.println("Servidor excepcion: "+ e.getMessage());
                e.printStackTrace();
            }
            if(datos.getSalir() == true){
                System.out.println("Terminar ProcesoNuevo");
                break;
            }
            usar = false;
        }
    }
    /**
    * anadirProcesosTablaPaginas
    * @code anade al proceso a la lista de procesos despachar
    * @param paqueteNuevo guarda un nuevo paquete
    * @param listaProcesosDespachar lista de procesos a despachar
    * @param contProcesosAct contador de los procesos Actuales
    * @return procesoNuevo - Procesos
    */
    Procesos registrarDatos(Paquete paqueteNuevo, String[] listaProcesosDespachar, int[] contProcesosAct){//se tiene que lanzar como promt 
        Procesos procesoNuevo = new Procesos(paqueteNuevo.getProceso().getNombre(), paqueteNuevo.proceso.totalPaginas, paqueteNuevo.proceso.orden, paqueteNuevo.proceso.n_inv);
        listaProcesosDespachar[contProcesosAct[0]] = procesoNuevo.nombre ;
	    return procesoNuevo;
    }
    /**
    * anadirProcesosTablaPaginas
    * @code establece las paginas del proceso en el la tabla de paginas
    * @param contProcesosAct contador de los procesos actuales
    * @param contadorTabla contador que establece el indice donde se quedo
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
    * @code Registra los datos del proceso nuevo en los registros del cliente
    * @param paqueteNuevo es el paquete que contiene el proceso nuevo
    * @param contadorProcesoAct contador de los procesos actuales del cliente 
    * @param listaProcesosDespachar lista de los procesos ordenada
    * @param listaProcesos lista de los procesos en orden de llegada
    * @param tablaPaginas la tabla de paginas
    */
    Procesos registrar(Paquete paqueteNuevo, int[] contadorProcesoAct, String[] listaProcesosDespachar, int[] registroTablaPaginas, Procesos[] listaProcesos, Registro[] tablaPaginas) throws InterruptedException{
        int contRefTotales = 0;
        Procesos procesoNuevo;
        procesoNuevo = registrarDatos(paqueteNuevo, listaProcesosDespachar, contProcesosAct);//registra lo datos del proceso
        contadorTabla[0] = anadirProcesoTablaPaginas(contProcesosAct, contadorTabla, procesoNuevo, registroTablaPaginas, tablaPaginas);//se añaden las paginas a la tabla de paginas
        listaProcesos[contadorProcesoAct[0]] = procesoNuevo;//se añade el proceso a la lista general
        for(int i = 0; i < contadorProcesoAct[0]; i++){//for que cuenta las referencias de todos los procesos y establece la carga del cliente
            contRefTotales = contRefTotales + listaProcesos[i].n_inv; 
        }
        contadorProcesoAct[0]++;//incrementa el total de procesos del cliente
        //actualiza los registros
        datos.setContRefTotales(contRefTotales);
        datos.setContProcesosAct(contProcesosAct);
        datos.setListaProcesos(listaProcesos);
        return procesoNuevo;  
    }
}