package io.edurt.datacap.plugin.http.greptime;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GreptimeDBContainer
        extends GenericContainer<GreptimeDBContainer>
{
    private static final DockerImageName DEFAULT_IMAGE_NAME =
            DockerImageName.parse("greptime/greptimedb");

    public static final int PORT = 4001;
    public static final int HTTP_PORT = 4000;

    public GreptimeDBContainer()
    {
        super(DEFAULT_IMAGE_NAME);
        withExposedPorts(PORT, HTTP_PORT);
    }

    public GreptimeDBContainer(final DockerImageName dockerImageName)
    {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(dockerImageName);
        withExposedPorts(PORT, HTTP_PORT);
    }

    public String getLinuxLocalIp()
    {
        String ip = "";
        try {
            Enumeration<NetworkInterface> networkInterfaces =
                    NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ip;
    }
}
