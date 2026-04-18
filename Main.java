import model.ScanResult;
import service.ScanService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for Network Port Scanner V3.
 * Supports multi-threaded scanning with service detection.
 */
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter target host (IP or domain): ");
        String host = input.nextLine().trim();

        System.out.print("Enter start port: ");
        int startPort = Integer.parseInt(input.nextLine().trim());

        System.out.print("Enter end port: ");
        int endPort = Integer.parseInt(input.nextLine().trim());

        System.out.print("Enter thread count (recommended: 100): ");
        int threadCount = Integer.parseInt(input.nextLine().trim());

        System.out.println("\nScanning " + host + " from port " + startPort +
                " to " + endPort + " using " + threadCount + " threads...\n");

        long startTime = System.currentTimeMillis();

        ScanService service = new ScanService(200, threadCount);
        List<ScanResult> openPorts = service.scanRange(host, startPort, endPort);

        long endTime = System.currentTimeMillis();

        // Sort by port number
        openPorts.sort(Comparator.comparingInt(ScanResult::getPort));

        System.out.println("\n--- Scan Complete ---");
        System.out.println("Open ports found: " + openPorts.size());
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        System.out.println("\nResults:");
        System.out.println("─".repeat(60));
        openPorts.forEach(System.out::println);
        System.out.println("─".repeat(60));
    }
}