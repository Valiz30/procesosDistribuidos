public class SimuladorPrompt extends Thread {
    int[] contProcesosAct, contadorTabla;
    String[] listaProcesosDespachar;
    Registro[] tablaPaginas;
    int[] registroTablaPaginas;
    SimuladorInterfaz sInterfaz;
    Ejecutar ejProceso;
    public SimuladorPrompt(int[] contProcesosAct,int[] contadorTabla, String[] listaProcesosDespachar, Registro[] tablaPaginas, int[] registroTablaPaginas, SimuladorInterfaz sInterfaz){
        this.contProcesosAct = contProcesosAct;
        this.contadorTabla = contadorTabla;
        this.listaProcesosDespachar = listaProcesosDespachar;
        this.tablaPaginas = tablaPaginas;
        this.sInterfaz = sInterfaz;
        this.registroTablaPaginas = registroTablaPaginas;
    }
    run(){
        while(true){
            contProcesosAct = ejProceso.contProcesosAct;
            paquete.proceso = prompt();//añadir nuevo proceso
            paquete.identificador = -1;
            paquete.procesoExiste = false;
            sInterfaz.recibir(paquete);
        }
    }
    Proceso registrarDatos(String comando, String[] listaProcesosDespachar, int[] contProcesosAct){//se tiene que lanzar como promt 
        Procesos procesoNuevo;
        int indicePrimerEspacio = 0, indiceSegundoEspacio = 0;
        char[] numPaginas = {};
        for(int i = 0; i < comando.length(); i++){
            if(comando.charAt(i) == ' '){
                indicePrimerEspacio = i;
                break;
            }
        }
        procesoNuevo.nombre = comando.substring(0, indicePrimerEspacio);
        for(int i = indicePrimerEspacio + 1; i < comando.length(); i++){
            if(comando.charAt(i) == ' '){
                indiceSegundoEspacio = i;
                break;
            }
        }
        procesoNuevo.totalPaginas = cadenaToEntero(comando.substring(indicePrimerEspacio + 1, indiceSegundoEspacio).toCharArray());
        procesoNuevo.orden = comando.substring(indiceSegundoEspacio + 1, comando.length());
	    listaProcesosDespachar[contProcesosAct] = procesoNuevo.nombre ;//anade el nuevo proceso a la lista de proceso a despachar
	    return procesoNuevo;
    }
    //Anade las paginas del proceso nuevo a la tabla de paginas
    int anadirProcesoTablaPaginas(int[] contProcesosAct, int[] contadorTabla, Procesos procesoNuevo){
        int i;
        registroTablaPaginas[contProcesosAct] = contadorTabla[0];
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
    Proceso prompt() throws InterruptedException{
        Procesos procesoNuevo; //proceso que reciba el cliente
        String comando = "";
        Scanner entrada = new Scanner();
        System.out.println("> ");
        comando = entrada.nextLine();
        procesoNuevo = registrarDatos(comando, listaProcesosDespachar, contProcesosAct[0]);//pide datos de proceso
        contadorTabla[0] = anadirProcesoTablaPaginas(contProcesosAct[0], contadorTabla[0], procesoNuevo);//se añaden las paginas a la tabla de paginas            contProcesosAct[0]++;
        return procesoNuevo;  
    }
    public int[] getContProcesosAct() {
        return contProcesosAct;
    }
    public String[] getListaProcesosDespachar() {
        return listaProcesosDespachar;
    }
    public void setEjProceso(Ejecutar ejProceso) {
        this.ejProceso = ejProceso;
    }
}