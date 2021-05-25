package client.services;

import client.services.AbstractOrderClient;
import server.AbstractKitchenServer;
import shared.Order;
import shared.OrderStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represent a self-service app
 * Allows a user to assemble an order, submit it to the KitchenSever and output status of the order.
 * Send a request to KitchenServer through the network, GUI should stay responsive through async method calls.
 * For setting up a periodic task, you may use Timer.scheduleAtFiedRate togehter with TimerTask.
 */

public class OrderClient extends AbstractOrderClient {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private AbstractKitchenServer abstractKitchenServer;

    public OrderClient(String ipAddress, int port, AbstractKitchenServer abstractKitchenServer) throws IOException {
        socket = new Socket(ipAddress, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        this.abstractKitchenServer = abstractKitchenServer;
    }

    @Override
    public void submitOrder() {
        Thread submitThread = new Thread(() -> {
            try {
                oos.writeObject(super.order);
                oos.flush();
                startPollingServer(super.order.getOrderID());
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        submitThread.start();
    }

    @Override
    protected void startPollingServer(String orderId) {
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("polling for status");
                try {
                    oos.writeObject(orderId);
                    Order newOrder = (Order) ois.readObject();
                    if (order.getStatus() == OrderStatus.Ready) {
                        pickUpOrder();
                        Thread.interrupted();
                    }
                }
                catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    @Override
    protected void pickUpOrder() {
        //Start an asynchronous request to {@link AbstractKitchenServer#serveOrder(String)}
    }
}