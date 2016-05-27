/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification.host;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class IdentificationHost {

    private final ArrayList<String> ListaDeIp = new ArrayList();
    private boolean waitFor = false;
    private int timeout = 300;

    public boolean isExistRed(String host) {
        try {
            if (InetAddress.getByName(host).isReachable(timeout)) {
                return true;
            }
        } catch (UnknownHostException e4) {
        } catch (IOException e4) {
        }
        return false;
    }

    public void waitFor() {
        while (waitFor) {
            try {
                Thread.sleep(4);
            } catch (InterruptedException ex) {
                Logger.getLogger(IdentificationHost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void identificar_red() throws java.net.UnknownHostException {
        String myIP = IPManager.getObtenerMyIp();
        final List<Thread> conexiones = new ArrayList();
        final String[] Ips = myIP.split("\\.");
        waitFor = true;
        for (int i = 0; i < 256; i++) {

            final int ii = i;

            conexiones.add(new Thread() {

                @Override
                public void run() {
                    if (isExistRed(Ips[0] + "." + Ips[1] + "." + Ips[2] + "." + ii)) {
                        ListaDeIp.add(Ips[0] + "." + Ips[1] + "." + Ips[2] + "." + ii);
                    }
                }
            });

        }

        new Thread() {

            @Override
            public void run() {
                int contador = 0;
                int cantidadMaxima = Thread.activeCount();
                while (true) {
                    if (Thread.activeCount() <= cantidadMaxima + 20) {
                        if (conexiones.size() > contador) {
                            conexiones.get(contador).start();
                            contador++;
                        } else if (Thread.activeCount() == cantidadMaxima) {
                            waitFor = false;
                            break;
                        }
                    }
                }
            }
        }.start();
    }

    public String getService(List<String> address, int port) {
        for (String addres : address) {
            if (isService(addres, port)) {
                return addres;
            }
        }
        return null;
    }

    public boolean isService(String host, int port) {
        if (host == null) {
            return false;
        }
        try {
            final Socket s = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(host, port);
            s.connect(sockaddr, 500);
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    public ArrayList<String> getListaDeIp() {
        return ListaDeIp;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static void main(String[] args) throws java.net.UnknownHostException, IOException {
        IdentificationHost I = new IdentificationHost();
//        I.setTimeout(15000);
        I.identificar_red();
        I.waitFor();
        System.out.println(I.getListaDeIp().size());
//        System.out.println(I.getService(I.getListaDeIp(), 8080));

    }

}
