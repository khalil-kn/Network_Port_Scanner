import service.ScanService;
import model.ScanResult;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Network Port Scanner.
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

        System.out.println("\nScanning " + host + " from port " + startPort + " to " + endPort + "...\n");

        ScanService service = new ScanService(200);
        List<ScanResult> openPorts = service.scanRange(host, startPort, endPort);

        System.out.println("\n--- Scan Complete ---");
        System.out.println("Total open ports found: " + openPorts.size());
    }
}