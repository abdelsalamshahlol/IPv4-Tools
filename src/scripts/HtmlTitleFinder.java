/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdelsalam Shahlol
 */
public class HtmlTitleFinder {
    // This class is used to start executorService and find HTML TITLE FOR ip adddresses

    final ExecutorService es = Executors.newFixedThreadPool(2);
    final List<Future<String>> futures = new ArrayList<>();

    public static Future<String> runTitleFinder(final ExecutorService es, final String ip) {
        return es.submit(new Callable<String>() {
            @Override
            public String call() {
                String returnMe = "";
                try {
                    URL myURL = new URL("http://" + ip);
                    URLConnection myURLConnection = myURL.openConnection();
                    myURLConnection.connect();
                    myURLConnection.setConnectTimeout(1500);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(myURLConnection.getInputStream()));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        Document doc = (Document) Jsoup.parse(inputLine);
                        String title = doc.title();
                        System.out.println(ip + " " + title);
                        if (title != null) {
                            returnMe = title;
                            break;
                        }

                    }
                    br.close();
                } catch (Exception ex) {
                    //System.err.println(ex.getMessage());
                    // Logger.getLogger(IP.class.getName()).log(Level.SEVERE, null, ex);
                    returnMe = "";
                }
                return returnMe;
            }
        });
    }

    public String getHtmlTitle(String ip) throws UnknownHostException {
        String title = "";
        try {
            if (!Thread.currentThread().isInterrupted()) {
                title = runTitleFinder(es, ip).get();
            }
            es.shutdown();
            return title;
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println("EX HTM " + ex.getMessage());
            Logger.getLogger(HtmlTitleFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void killThread() {
        es.shutdown();
        try {
            if (!es.awaitTermination(60, TimeUnit.SECONDS)) {
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
