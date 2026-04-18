package core;

import model.ScanResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles core scanning logic including banner grabbing and service detection.
 */
public class PortScanner {

    private final int timeout;
    private final Map<Integer, String> knownPorts;

    public PortScanner(int timeout) {
        this.timeout = timeout;
        this.knownPorts = buildLookupTable();
    }

    /**
     * Builds a lookup table of well-known port numbers and their services.
     */
    private Map<Integer, String> buildLookupTable() {
        Map<Integer, String> ports = new HashMap<>();
        ports.put(21,   "FTP");
        ports.put(22,   "SSH");
        ports.put(23,   "Telnet");
        ports.put(25,   "SMTP");
        ports.put(53,   "DNS");
        ports.put(80,   "HTTP");
        ports.put(110,  "POP3");
        ports.put(143,  "IMAP");
        ports.put(443,  "HTTPS");
        ports.put(3306, "MySQL");
        ports.put(5432, "PostgreSQL");
        ports.put(6379, "Redis");
        ports.put(8080, "HTTP-Alt");
        ports.put(8443, "HTTPS-Alt");
        ports.put(27017,"MongoDB");
        return ports;
    }

    /**
     * Attempts to detect the service running on a port via banner grabbing.
     * Falls back to lookup table if no banner is received.
     * @param socket already connected socket
     * @param port port number for lookup table fallback
     * @return detected service name
     */
    private String detectService(Socket socket, int port) {
        try {
            // Set timeout for reading banner
            socket.setSoTimeout(timeout);

            // Read banner from service
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            String banner = reader.readLine();

            // Identify service from banner content
            if (banner != null) {
                if (banner.contains("SSH"))   return "SSH";
                if (banner.contains("FTP"))   return "FTP";
                if (banner.contains("SMTP"))  return "SMTP";
                if (banner.contains("HTTP"))  return "HTTP";
                if (banner.contains("POP3"))  return "POP3";
                if (banner.contains("IMAP"))  return "IMAP";
                if (banner.contains("MySQL")) return "MySQL";
                // Banner received but unrecognized — return raw banner
                return "Unknown (" + banner.substring(0, Math.min(banner.length(), 30)) + ")";
            }

        } catch (Exception e) {
            // No banner received — fall through to lookup table
        }

        // Fallback — lookup table
        return knownPorts.getOrDefault(port, "Unknown");
    }

    /**
     * Scans a single port and detects its service.
     * @param host target IP or hostname
     * @param port port number to scan
     * @return ScanResult with open/closed status and service name
     */
    public ScanResult scan(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);

            // Connection succeeded — detect service
            String service = detectService(socket, port);

            return new ScanResult(host, port, true, service);

        } catch (Exception e) {
            return new ScanResult(host, port, false, "N/A");
        }
    }
}