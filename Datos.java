class Datos{
    int[] contProcesosAct = {}, contRefTotales = {}, contProcesosCliente = {}, contProcesosFinalizados = {}; //contadores
    int[] registroTablaPaginas = new int[30]; //arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][])
    String[] listaProcesosDespachar;
    Registro[] tablaPaginas;
    boolean procesosPendientes = false;
    Procesos[] listaProcesos = new Procesos[30];
    String[] procesosCliente = new String[30];
    int carga;
    String[] procesosFinalizados = new Proceso[30];
    public Datos(int[] contProcesosAct, int[] registroTablaPaginas, String[] listaProcesosDespachar, Registro[] tablaPaginas){
        this.contProcesosAct = contProcesosAct;
        this.registroTablaPaginas = registroTablaPaginas;
        this.listaProcesosDespachar = listaProcesosDespachar;
        this.tablaPaginas = tablaPaginas;
        contRefTotales[0] = 0;
        contProcesosCliente[0] = 0;
        contProcesosFinalizados[0] = 0;
        carga = 0;
    }
    //metodos
    public synchronized int[] getContProcesosFinalizados() {
        return contProcesosFinalizados;
    }
    public synchronized int[] getContProcesosCliente() {
        return contProcesosCliente;
    }
    public synchronized String[] getProcesosCliente(int ) {
        return procesosCliente;
    }
    public synchronized String[] getprocesosFinalizados(){
        return this.procesosFinalizados;
    }
    public synchronized int getCarga(){
        return this.Carga;
    }
    public synchronized Procesos[] getListaProcesos(){
        return this.listaProcesos;
    }
    public synchronized int[] getContRefTotales(){
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
    public synchronized void setContProcesosFinalizados(int[] contProcesosFinalizados) {
        this.contProcesosFinalizados = contProcesosFinalizados;
    }
    public synchronized void setContProcesosCliente(int[] contProcesosCliente) {
        this.contProcesosCliente = contProcesosCliente;
    }
    public synchronized void setProcesosCliente(String[] procesosCliente) {
        this.procesosCliente = procesosCliente;
    }
    public synchronized void setprocesosFinalizados(String[] procesosFinalizados){//pendiente pasar la bandera
        this.procesosFinalizados = procesosFinalizados;
    }
    public synchronized void setCarga(Int Carga){
        this.Carga = Carga;
    }
    public synchronized void setListaProcesos(Procesos[] listaProcesos){
        this.listaProcesos = listaProcesos;
    }
    public synchronized void setContRefTotales(int[] contRefTotales){
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