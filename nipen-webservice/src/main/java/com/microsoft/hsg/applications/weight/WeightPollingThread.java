package com.microsoft.hsg.applications.weight;

import com.microsoft.hsg.ConnectionFactory;
import com.microsoft.hsg.applications.OnlineRequestTemplate;
import com.microsoft.hsg.applications.PersonInfo;
import com.microsoft.hsg.things.ThingProvider;
import com.microsoft.hsg.things.Weight;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WeightPollingThread extends Thread {
    private final int POLLING_TIME_IN_SECONDS = 600;
    private int secondsLeft = 0;
    private boolean running = false;

    public static PersonInfo personInfo = null;

    public void run() {
        System.out.println("Weight polling thread has started.");
        String lastWeightTime = null;

        long time = System.currentTimeMillis();
        secondsLeft = POLLING_TIME_IN_SECONDS;
        running = true;
        while (running) {
            /*secondsLeft = POLLING_TIME_IN_SECONDS - (int)((System.currentTimeMillis() - time) / 1000.0);

            if (secondsLeft <= 0) {
                stopThread();
                break;
            }*/

            OnlineRequestTemplate requestTemplate = new OnlineRequestTemplate(ConnectionFactory.getConnection());
            ThingProvider thingProvider = new ThingProvider(requestTemplate);
            List weights = thingProvider.getThingsByType(Weight.Type);

            if (weights != null && weights.size() > 0) {
                Weight weight = (Weight)weights.get(0);
                if (lastWeightTime == null) {
                    lastWeightTime = weight.getTime();
                }
                else if (!lastWeightTime.equals(weight.getTime()) &&
                        weight.getTime() != null && weight.getValue() != null) {
                    sendWeightToNipen(weight);
                    lastWeightTime = weight.getTime();
                }
            }

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
            }
        }

        System.out.println("Weight polling thread has stopped.");
    }

    public boolean isRunning() {
        return running;
    }

    public void stopThread() {
        running = false;
        secondsLeft = 0;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    private void sendWeightToNipen(Weight weight) {
        String jsonMessage =
                        "{\"userId\":1,\"timestamp\": \"" +
                        weight.getTime() + "\",\"value\":" +
                        weight.getValue() + ",\"unit\":\"kg\"}";

        HttpURLConnection httpConnection = null;

        try {
            URL endPoint = new URL("http://mhealthdemo03.cloudapp.net/nipen/api/human/weight");
            httpConnection = (HttpURLConnection)endPoint.openConnection();

            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.addRequestProperty("Content-Type", "application/json");

            httpConnection.connect();

            OutputStream outputStream = httpConnection.getOutputStream();

            outputStream.write(jsonMessage.getBytes());
            outputStream.flush();
            outputStream.close();

            // we must read anyway !
            InputStream inputStream = httpConnection.getInputStream();
            inputStream.close();

            System.out.println("The following value has been sent to NIP: " + jsonMessage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (httpConnection != null)
                httpConnection.disconnect();
        }
    }
}
