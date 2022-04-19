class Datos{
    int[] contProcesosAct = {}; //contadores
    int[] registroTablaPaginas = new int[30]; //arreglo que contiene el indice del proceso en la tabla de paginas (usando el mismo orden que en listaProcesosDespachar[][])
    String[] listaProcesosDespachar;
    Registro[] tablaPaginas;
    boolean procesosPendientes = false;
    public Datos(int[] contProcesosAct, int[] registroTablaPaginas, String[] listaProcesosDespachar, Registro[] tablaPagina){
        this.contProcesosAct = contProcesosAct;
        this.registroTablaPaginas = registroTablaPaginas;
        this.listaProcesosDespachar = listaProcesosDespachar;
        this.tablaPaginas = tablaPaginas;
    }
    //metodos
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