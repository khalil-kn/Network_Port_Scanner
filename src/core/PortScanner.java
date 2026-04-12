package core;

import model.ScanResult;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Handles the core scanning logic for a single port.
 */
public class PortScanner {

    private final int timeout;

    public PortScanner(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Scans a single port on the given host.
     * @param host target IP or hostname
     * @param port port number to scan
     * @return ScanResult with open/closed status
     */
    public ScanResult scan(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return new ScanResult(host, port, true);
        } catch (Exception e) {
            return new ScanResult(host, port, false);
        }
    }
}