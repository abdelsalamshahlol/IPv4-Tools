/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 * @author Abdelsalam Shahlol
 */
public class PingScan {

    // This class is used to start executorService and ping ip addresses
    final ExecutorService es = Executors.newFixedThreadPool(10);
    final List<Future<String>> futures = new ArrayList<>();

    public static Future<String> runPingScan(final ExecutorService es, final String ip) {
        return es.submit(new Callable<String>() {
            @Override
            public String call() {
                String returnMe = "";
                long currentTime = System.currentTimeMillis();
                Ping p = new Ping();
                p.SendReply(ip);
                // System.out.println(ip + " " + p.IsReachable());
                if (p.IsReachable()) {
                    currentTime = System.currentTimeMillis() - currentTime;
//                     System.err.println(currentTime);
                    returnMe = ip + "," + currentTime;
                }
                return returnMe;
            }
        });
    }

    public List<Future<String>> checkThisIP(String ipStart, String ipEnd)  {

        IPAddress ip1 = new IPAddress(ipStart);
        IPAddress ip2 = new IPAddress(ipEnd);

        if (!ipStart.equals(ipEnd)) {
            do {
                futures.add(runPingScan(es, ip1.toString()));
                ip1 = ip1.next();
            } while (!ip1.equals(ip2));
        } else {
            futures.add(runPingScan(es, ipStart));
        }
        es.shutdown();
        return futures;
    }

    public void killThread() {
        es.shutdown();
        try {
            if (!es.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("KILL THREAD in ping scan");

                es.shutdownNow();
                if (!es.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            System.err.println("message from thread kill " + ie.getMessage());
            es.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
