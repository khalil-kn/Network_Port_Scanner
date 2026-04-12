package model;

/**
 * Represents the result of a single port scan.
 */
public class ScanResult {

    private final String host;
    private final int port;
    private final boolean open;

    public ScanResult(String host, int port, boolean open) {
        this.host = host;
        this.port = port;
        this.open = open;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isOpen() { return open; }

    @Override
    public String toString() {
        return String.format("Host: %s | Port: %d | Status: %s",
                host, port, open ? "OPEN" : "CLOSED");
    }
}