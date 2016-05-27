/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identification.host;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author GustavoG
 */
public class IPManager {

    public static String getObtenerMyIp() throws UnknownHostException {
        String ip = null;
        String myHost = InetAddress.getLocalHost().getHostName();
        ip = ObteniendoMiIp(myHost, ip);
        ip = MyIP(myHost, ip);
        ip = ObteniendoMiIpWindows(ip);
        ip = ObteniendoMiIpPls(ip);
        return ip;
    }

    private static String MyIP(String host, String ip) {
        if (ip != null) {
            return ip;
        }
        try {
            List<String> commands = new ArrayList<String>();
            commands.add("ping");
            commands.add(host);
            ProcessBuilder processbuilder = new ProcessBuilder(commands);
            Process process = processbuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            for (int i = 0; i < 5; i++) {
                String ss = (stdInput.readLine());
                if (ss.toUpperCase().contains("PING")) {
                    String ipObtenida = (getIPForText(ss));
                    if (ipObtenida.equals("127.0.1.1")) {
                        return null;
                    } else {
                        return ipObtenida;
                    }
                }

            }
        } catch (Exception e) {

        }
        return null;
    }

    private static String ObteniendoMiIpPls(String ip) {
        if (ip != null) {
            return ip;
        }
        try {
            String ss;
            List<String> commands = new ArrayList<String>();
            commands.add("ifconfig");
            ProcessBuilder processbuilder = new ProcessBuilder(commands);
            Process process = processbuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((ss = stdInput.readLine()) != null) {
                if (ss.contains("Direc")) {
                    String ipObtenida = (getIPForText(ss));
                    if (ipObtenida.equals("127.0.1.1")) {
                        return null;
                    } else {
                        return ipObtenida;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static String ObteniendoMiIpWindows(String ip) {
        if (ip != null) {
            return ip;
        }
        try {
            String ss;
            List<String> commands = new ArrayList<String>();
            commands.add("ipconfig");
            ProcessBuilder processbuilder = new ProcessBuilder(commands);
            Process process = processbuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((ss = stdInput.readLine()) != null) {
                if (getIPForText(ss) != null) {
                    String ipObtenida = (getIPForText(ss));
                    if (ipObtenida.equals("127.0.1.1")) {
                        return null;
                    } else {
                        return ipObtenida;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static String ObteniendoMiIp(String host, String ip) throws UnknownHostException {
        if (ip != null) {
            return ip;
        }
        String ipObtenida = InetAddress.getByName(host).getHostAddress();
        if (ipObtenida.equals("127.0.1.1")) {
            return null;
        } else {
            return ipObtenida;
        }
    }

    private static String getIPForText(String mydata) {
        Pattern pattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Matcher matcher = pattern.matcher(mydata);
        if (matcher.find()) {
            return (matcher.group(0));
        }
        return null;
    }
}
