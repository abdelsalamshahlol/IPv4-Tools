package scripts;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// This class is used to start executorService and scan ip addresses for listening and open ports
public class PortScan {

    //execution of the port check with using threading and executorService 
    public static Future<String> runPortScan(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    String conditon = "";
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), timeout);
                    socket.close();
                    conditon = String.valueOf(port);
                    return conditon;
                } catch (Exception ex) {
                    return "";
                }
            }
        });
    }

    //the open ports check for listing ports for specified IP address
    public static String checkIpForPorts(final String ip) throws InterruptedException, ExecutionException {
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final int timeout = 200;
        final List<Future<String>> futures = new ArrayList<>();
        String results = "";
        for (int port = 1; port <= 4000; port++) {
            Future<String> con = runPortScan(es, ip, port, timeout);
            if (!con.equals("")) {
                futures.add(con);
            }
        }
        es.shutdown();
        StringBuilder sb = new StringBuilder();

        for (final Future<String> f : futures) {
            if (!f.get().isEmpty()) {
                sb.append(f.get());
                sb.append(" ");
            }
        }
        results = sb.toString();
        return results;
    }

}
