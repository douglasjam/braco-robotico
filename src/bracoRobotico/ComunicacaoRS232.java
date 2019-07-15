package bracoRobotico;

import javax.comm.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
//classe Principal 
public class ComunicacaoRS232 {

    ArrayList<CommPortIdentifier> portasDisponiveis;
    CommPortIdentifier portaEscolhida;
    public Integer delayMS;
    public Integer motorUm, motorDois, motorTres, motorQuatro,
            motorCinco, motorSeis, motorSete, motorOito;
    private int baudrate;
    private int timeout;
    private SerialPort portaSaida;
    private OutputStream saida;

    public ComunicacaoRS232() {

        portasDisponiveis = capturarPortasDisponiveis();
        portaEscolhida = capturarPortasDisponiveis().get(0);
        baudrate = 2400;
        delayMS = 200;
        timeout = 100;
    }

    public ArrayList<CommPortIdentifier> capturarPortasDisponiveis() {

        Enumeration identificadoresPortas = CommPortIdentifier.getPortIdentifiers();
        ArrayList<CommPortIdentifier> portasCapturadas = new ArrayList<CommPortIdentifier>();

        while (identificadoresPortas.hasMoreElements()) {
            CommPortIdentifier ips = (CommPortIdentifier) identificadoresPortas.nextElement();
            portasCapturadas.add(ips);
        }

        return portasCapturadas;
    }

    public CommPortIdentifier getPortaEscolhida() {
        return portaEscolhida;
    }

    public ArrayList<String> getPortasDisponiveis() {

        ArrayList<String> portasDisponiveisString = new ArrayList<String>();
        for (CommPortIdentifier porta : portasDisponiveis) {
            portasDisponiveisString.add(porta.getName());
        }
        return portasDisponiveisString;
    }

    public void setPorta(CommPortIdentifier Porta) {
        this.portaEscolhida = Porta;
    }

    public void setPorta(String porta) {
        for (CommPortIdentifier portaVarrida : portasDisponiveis) {
            if (portaVarrida.getName().equals(porta)) {
                this.portaEscolhida = portaVarrida;
                return;
            }

        }
        this.portaEscolhida = portasDisponiveis.get(0);
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public Integer getDelayMS() {
        return delayMS;
    }

    public void setDelayMS(Integer delayMS) {
        this.delayMS = delayMS;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean portaExiste(String nomePorta) {

        for (CommPortIdentifier porta : portasDisponiveis) {
            if (porta.getName().equals(nomePorta)) {
                return true;
            }
        }
        return false;
    }

    public void abrirPorta() {
        try {
            portaSaida = (SerialPort) portaEscolhida.open("SComm", timeout);

            portaSaida.setSerialPortParams(baudrate,
                    portaSaida.DATABITS_8,
                    portaSaida.STOPBITS_2,
                    portaSaida.PARITY_NONE);
        } catch (Exception e) {
            System.out.println("Erro ao abrir a porta! STATUS: " + e);
            System.exit(1);
        }
    }
    //função que envie um bit para a porta serial 

    public void enviar(Integer motor, Integer msg) {

        try {
            saida = portaSaida.getOutputStream();
        } catch (Exception e) {
            System.out.println("Erro.STATUS: " + e);
        }
        try {
            saida.write(motor);
            Thread.sleep(20);
            saida.write(msg);
            saida.flush();
            saida.close();
        } catch (Exception e) {
            System.out.println("Houve um erro durante o envio. ");
            System.out.println("STATUS: " + e.getMessage());
            System.exit(1);
        }

    }

    //método RUN da thread de leitura 
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Erro. Status = " + e);
        }
    }
    //gerenciador de eventos de leitura na serial 

    //função que fecha a conexão 
    public void fecharPorta() {
        try {
            portaSaida.close();
            System.out.println("CONEXAO FECHADA>>FIM..");
        } catch (Exception e) {
            System.out.println("ERRO AO FECHAR. STATUS: " + e);
            System.exit(0);
        }
    }
}
