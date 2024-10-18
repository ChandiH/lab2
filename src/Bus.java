import java.util.concurrent.TimeUnit;

public class Bus implements Runnable {
    private BusStop busStop;
    public Bus(BusStop busStop) {
        this.busStop = busStop;
    }

    @Override
    public void run() {
        try {
            busStop.busArrives();
            System.out.println("Bus: Bus arrives at bus stop");

            // Wait for 2 seconds to simulate bus boarding
            TimeUnit.SECONDS.sleep(5);

            busStop.busDeparts();
            System.out.println("Bus: Bus is departing");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}