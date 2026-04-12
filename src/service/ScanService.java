package service;

import core.PortScanner;
import model.ScanResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages scanning across a range of ports.
 */
public class ScanService {

    private final PortScanner scanner;

    public ScanService(int timeout) {
        this.scanner = new PortScanner(timeout);
    }

    /**
     * Scans a range of ports on the given host.
     * @param host target IP or hostname
     * @param startPort first port to scan
     * @param endPort last port to scan
     * @return list of ScanResult objects
     */
    public List<ScanResult> scanRange(String host, int startPort, int endPort) {
        List<ScanResult> results = new ArrayList<>();

        for (int port = startPort; port <= endPort; port++) {
            ScanResult result = scanner.scan(host, port);
            if (result.isOpen()) {
                results.add(result);
                System.out.println("[OPEN] Port " + port);
            }
        }
        return results;
    }
}