import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Receives a new order and should init the cooking process.
 * Use a threadpool to represent the kitchen and submit a dummy task that
 * just sleeps and modifies the status of an order.
 * Use a seprate thread pool for cooking in the Kitchen Server.
 * Add random sleep intervals to KitchenServer methods to represent network delay.
 * You may use a CompletableFuture object to set the return value of async with the complete()-method.
 * Use newFixedThreadPool(). myPool.submit(task) to submit a task for execution.
 */
public class KitchenServer extends AbstractKitchenServer implements Runnable {

    private ServerSocket serverSocket;
    private Thread thread = new Thread(this);
    private boolean serverRunning;
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public KitchenServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverRunning = true;
            thread.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdownServer() {
        serverRunning = false;
    }

    @Override
    void receiveOrder(Order order) {
        CookingTask cookingTask = new CookingTask(order);
        executor.submit(cookingTask);
        //Thread.sleep(random.nextInt());
        cook(order);
    }

    @Override
    void cook(Order order) {
        //Thread.sleep(random.nextInt());
    }

    @Override
    void checkStatus(String string) {

    }

    @Override
    void serveOrder(String string) {

    }

    @Override
    public void run() {
        Order order = null;
        while(serverRunning) {
            try {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                try {
                    order = (Order) ois.readObject();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //init cooking process
                receiveOrder(order);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
