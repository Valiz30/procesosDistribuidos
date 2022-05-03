public class RegistroClientes{
    private int idCliente, contadorProcesos;
    private String[] procesos;
    private String cadena;
    public RegistroClientes(int idCliente, String[] procesos, String cadena){
        this.idCliente = idCliente;
        this.procesos = procesos;
        this.cadena = cadena;
        contadorProcesos = 0;
    }
    public void anadirProceso(String proceso){
        procesos[contadorProcesos] = proceso;
        contadorProcesos++;
    }
    public int getContadorProcesos(){
       return this.contadorProcesos;
    }
    public int getIdCliente(){
        return this.idCliente;
    }
    public String[] getProcesos(){
        return this.procesos;
    }
    public String getCadena(){
       return this.cadena;
    }
    public void setContadorProcesos(int contadorProcesos){
       this.contadorProcesos = contadorProcesos;
    }
    public void setIdCliente(int idCliente){
       this.idCliente = idCliente;
    }
    public void setProcesos(String[] procesos){
       this.procesos = procesos;
    }
    public void setCadena(String cadena){
       this.cadena = cadena;
    }
 }