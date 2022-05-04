class Datos{
    /**    CLIENTE 
    *   Docs Datos - Contiene las variables que utilizaran los hilos del cliente.
    *   contProcesosAct - cantidad de procesos que el servidor le asigno al cliente para ejecutar
    *   contRefTotales - carga del cliente
    *   contProcesosCliente - cantidad de procesos que le pertenecen al cliente (los que ingresa el usuario)
    *   contProcesosFinalizados - cantidad de procesos que el cliente ha terminado de ejecutar de todos los que el servidor le envio
    *   listaProcesosDespachar - contiene lo procesos a ejecutar por el cliente en orden
    *   procesosCliente - son los procesos que el usuario ingreso en el Prompt
    *   registroTablaPaginas  Arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][])
    *   nombreHilo - es el nombre del Hilo que va a tener permiso de acceder a esta clase, en un determinado tiempo
    *   tablaPaginas - sera la tabla que contendra las entradas de la tabla de paginas
    *   procesosPendientes - indica si el cliente tiene procesos pendientes a ejecutar
    *   salir - indica si el cliente terminara de ejecutarse
    *   listaProcesos - lista que contiene los procesos que el servidor le asigno al cliente, pero en desorden
    *   idCliente - es el identificador del cliente
    */
    int[] contProcesosAct = {0}, contProcesosFinalizados = {0},registroTablaPaginas = new int[30]; 
    String[] listaProcesosDespachar, procesosCliente = new String[30], procesosFinalizados = new String[30];
    String nombreHilo = "";
    Registro[] tablaPaginas;
    boolean procesosPendientes = false, salir = false;
    Procesos[] listaProcesos = new Procesos[30];
    int contRefTotales, contProcesosCliente, idCliente;
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
        this.contRefTotales = 0;
        this.contProcesosCliente = 0;
        this.contProcesosFinalizados[0] = 0;
        for(int i = 0; i < 30; i++){
            this.procesosFinalizados[i] = "0";
            this.listaProcesos[i] = new Procesos("", 0, "", 0);
        }
    }
    /**
    *   SETTER Y GETTERS DE LA CLASE DATOS CON SYNCRHONIZED
    * (que solamente un subproceso puede acceder a dicho método a la vez)
    */
    public synchronized int getIdCliente(){
        return this.idCliente;
    }
    public synchronized String getNombreHilo(){
        return this.nombreHilo;
    }
    public synchronized boolean getSalir(){
        return this.salir;
    }
    public synchronized int[] getContProcesosFinalizados() {
        return this.contProcesosFinalizados;
    }
    public int getContProcesosCliente() {
        return this.contProcesosCliente;
    }
    public String[] getProcesosCliente() {
        return this.procesosCliente;
    }
    public synchronized String[] getProcesosFinalizados(){
        return this.procesosFinalizados;
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
    public synchronized void setIdCliente(int idCliente){
        this.idCliente = idCliente;
    }
    public synchronized void setNombreHilo(String nombreHilo){
        this.nombreHilo = nombreHilo;
    }
    public synchronized void setSalir(boolean salir){
        this.salir = salir;
    }
    public synchronized void setContProcesosFinalizados(int[] contProcesosFinalizados) {
        this.contProcesosFinalizados = contProcesosFinalizados;
    }
    public void setContProcesosCliente(int contProcesosCliente) {
        this.contProcesosCliente = contProcesosCliente;
    }
    public void setProcesosCliente(String[] procesosCliente) {
        this.procesosCliente = procesosCliente;
    }
    public synchronized void setprocesosFinalizados(String[] procesosFinalizados){//pendiente pasar la bandera
        this.procesosFinalizados = procesosFinalizados;
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