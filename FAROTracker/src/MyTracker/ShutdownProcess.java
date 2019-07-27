/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

/**
 *
 * @author szhc
 */
public class ShutdownProcess {
    public ShutdownProcess(TrackerThread tracker) {
        trackerThread = tracker;
        initComponents();
    }
    
    private void initComponents() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if(trackerThread != null) {
                    trackerThread.ExitMeasurement();
                }
                System.out.println("Exit...");
            }
        });
    }
    
    private final TrackerThread trackerThread;
}
