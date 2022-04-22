class Datos{
    /**
    *   Docs Datos - Contiene las variables que utilizara el cliente.
    *   contProcesosAct, contRefTotales, contProcesosCliente,contProcesosFinalizados - Contadores
    *   registroTablaPaginas  Arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][])
    */
    int[] contProcesosAct = {}, contProcesosCliente = {}, contProcesosFinalizados = {},registroTablaPaginas = new int[30]; 
    String[] listaProcesosDespachar, procesosCliente = new String[30], procesosFinalizados = new String[30];
    String nombreCliente = "";
    Registro[] tablaPaginas;
    boolean procesosPendientes = false;
    Procesos[] listaProcesos = new Procesos[30];
    int carga, contRefTotales; 
    
    /**
    *   Datos()
    *   @param contProcesosAct Contador de los procesos actualess
    *   @param registroTablaPaginas indice del proceso en la tabla de paginas
    *   @param listaProcesosDespachar orden a despechar de los procesos
    *   @param tablaPaginas Es la tabla de paginas de objeto Registro 
    */
    public Datos(int[] contProcesosAct, int[] registroTablaPaginas, String[] listaProcesosDespachar, Registro[] tablaPaginas){
        this.contProcesosAct = contProcesosAct;
        this.registroTablaPaginas = registroTablaPaginas;
        this.listaProcesosDespachar = listaProcesosDespachar;
        this.tablaPaginas = tablaPaginas;
        contRefTotales = 0;
        contProcesosCliente[0] = 0;
        contProcesosFinalizados[0] = 0;
        carga = 0;
    }
    /**
    *   SETTER Y GETTERS DE LA CLASE DATOS CON SYNCRHONIZED
    * (que solamente un subproceso puede acceder a dicho método a la vez)
    */
    public synchronized String getNombreCliente(){
        return this.nombreCliente;
    }
    public synchronized int[] getContProcesosFinalizados() {
        return this.contProcesosFinalizados;
    }
    public synchronized int[] getContProcesosCliente(String nombreCliente) {
        int[] contAux = new int[1];
        contAux[0] = -1;
        if(nombreCliente == this.nombreCliente){
            return this.contProcesosCliente;
        }else{
            return contAux;
        }
    }
    public synchronized String[] getProcesosCliente(String nombreCliente) {
        String[] procesoAux = new String[1];
        procesoAux[0] = "";
        if(nombreCliente == this.nombreCliente){
            return this.procesosCliente;
        }else{
            return procesoAux;
        }
    }
    public synchronized String[] getProcesosFinalizados(){
        return this.procesosFinalizados;
    }
    public synchronized int getCarga(){
        return this.carga;
    }
    public synchronized Procesos[] getListaProcesos(){
        return this.listaProcesos;
    }
    public synchronized int getContRefTotales(){
        return this.contRefTotales;
    }
    public synchronized boolean getProcesosPendientes(){
        return this.procesosPendientes;
    }
    public synchronized int[] getContProcesosAct(){
        return this.contProcesosAct;
    }
    public synchronized int[] getRegistroTablaPaginas(){
        return this.registroTablaPaginas;
    }
    public synchronized String[] getListaProcesosDespachar(){
        return this.listaProcesosDespachar;
    }
    public synchronized Registro[] getTablaPaginas(){
        return this.tablaPaginas;
    }
    public synchronized void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }
    public synchronized void setContProcesosFinalizados(int[] contProcesosFinalizados) {
        this.contProcesosFinalizados = contProcesosFinalizados;
    }
    public synchronized int[] setContProcesosCliente(int[] contProcesosCliente, String nombreCliente) {
        int[] contAux = new int[1];
        contAux[0] = -1;
        if(nombreCliente == this.nombreCliente){
            this.contProcesosCliente = contProcesosCliente;
            contAux[0] = 1;
            return contAux;
        }else{
            return contAux; 
        }
    }
    public synchronized int[] setProcesosCliente(String[] procesosCliente, String nombreCliente) {
        int[] contAux = new int[1];
        contAux[0] = -1;
        if(nombreCliente == this.nombreCliente){
            this.procesosCliente = procesosCliente;
            contAux[0] = 1;
            return contAux;
        }else{
            return contAux; 
        }
    }
    public synchronized void setprocesosFinalizados(String[] procesosFinalizados){//pendiente pasar la bandera
        this.procesosFinalizados = procesosFinalizados;
    }
    public synchronized void setCarga(int carga){
        this.carga = carga;
    }
    public synchronized void setListaProcesos(Procesos[] listaProcesos){
        this.listaProcesos = listaProcesos;
    }
    public synchronized void setContRefTotales(int contRefTotales){
        this.contRefTotales = contRefTotales;
    }
    public synchronized void setProcesosPendientes(boolean ProcesosPendientes){
        this.procesosPendientes = ProcesosPendientes;
    }
    public synchronized void setContProcesosAct(int[] contProcesosAct){
        this.contProcesosAct = contProcesosAct;
    }    
    public synchronized void setRegistroTablaPaginas(int[] RegistroTablaPaginas){
        this.registroTablaPaginas = RegistroTablaPaginas;
    }
    public synchronized void setListaProcesosDespachar(String[] ListaProcesosDespachar){
        this.listaProcesosDespachar = ListaProcesosDespachar;
    }
    public synchronized void setTablaPaginas(Registro [] TablaPaginas){
        this.tablaPaginas = TablaPaginas;
    }

}

//TERMINADO