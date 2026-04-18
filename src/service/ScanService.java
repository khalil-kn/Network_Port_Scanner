package service;

import core.PortScanner;
import model.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Manages concurrent port scanning across a range of ports.
 * Uses a fixed thread pool for parallel execution.
 */
public class ScanService {

    private final PortScanner scanner;
    private final int threadCount;

    public ScanService(int timeout, int threadCount) {
        this.scanner = new PortScanner(timeout);
        this.threadCount = threadCount;
    }

    /**
     * Scans a range of ports concurrently using a thread pool.
     * @param host target IP or hostname
     * @param startPort first port to scan
     * @param endPort last port to scan
     * @return list of open ScanResult objects
     */
    public List<ScanResult> scanRange(String host, int startPort, int endPort) {

        // Thread safe list — multiple threads write to this simultaneously
        List<ScanResult> results = Collections.synchronizedList(new ArrayList<>());

        // Create thread pool with fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int port = startPort; port <= endPort; port++) {
            final int p = port; // must be final for lambda

            // Submit each port scan as a separate task
            executor.submit(() -> {
                ScanResult result = scanner.scan(host, p);
                if (result.isOpen()) {
                    results.add(result);
                    System.out.println("[OPEN] Port " + p);
                }
            });
        }

        // Stop accepting new tasks
        executor.shutdown();

        try {
            // Wait for ALL threads to finish before returning
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.err.println("Scan interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        return results;
    }
}