package rmi;
import java.io.*;

/**
 *
 * @author Alexandre
 * @author Júlio
 *
 */

public class Ficheiro {

    private ObjectInputStream iS;
    private ObjectOutputStream oS;

    /**
     * Método para abrir um ficheiro para leitura
     * @param nomeDoFicheiro
     * @return
     */
    public boolean abreLeitura(String nomeDoFicheiro) {
        try {
            iS = new ObjectInputStream(new FileInputStream(nomeDoFicheiro));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Método para abrir um ficheiro para escrita
     * @param nomeDoFicheiro
     * @throws IOException
     */
    public void abreEscrita(String nomeDoFicheiro) throws IOException {
        oS = new ObjectOutputStream(new FileOutputStream(nomeDoFicheiro));
    }

    /**
     * Método para ler um objecto do ficheiro
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object leObjecto() throws IOException, ClassNotFoundException {
        return iS.readObject();
    }

    /**
     * Método para escrever um objecto no ficheiro
     * @param objecto
     * @throws IOException
     */
    public void escreveObjecto(Object objecto) throws IOException {
        oS.writeObject(objecto);
    }

    /**
     * Método para fechar um ficheiro aberto em modo leitura
     * @throws IOException
     */
    public void fechaLeitura() throws IOException {
        iS.close();
    }

    /**
     * Método para fechar um ficheiro aberto em modo escrita
     * @throws IOException
     */
    public void fechaEscrita() throws IOException {
        oS.close();
    }
}
