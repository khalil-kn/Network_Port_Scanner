package model;

/**
 * Represents the result of a single port scan including service detection.
 */
public class ScanResult {

    private final String host;
    private final int port;
    private final boolean open;
    private final String service;

    public ScanResult(String host, int port, boolean open, String service) {
        this.host = host;
        this.port = port;
        this.open = open;
        this.service = service;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isOpen() { return open; }
    public String getService() { return service; }

    @Override
    public String toString() {
        return String.format("Host: %s | Port: %-5d | Service: %-10s | Status: %s",
                host, port, service, open ? "OPEN" : "CLOSED");
    }
}