import java.util.concurrent.Semaphore;

public class BusStop {
    private Semaphore waitingRiders = new Semaphore(0);
    private Semaphore busMutex = new Semaphore(1);
    private Semaphore allAboard = new Semaphore(0);
    private int waitingCount = 0;

    public synchronized void riderArrives() throws InterruptedException {
        waitingCount++;
        System.out.println("Bus Stop: Waiting riders: " + waitingCount);
    }

    public void boardBus() throws InterruptedException {
        waitingRiders.acquire();
        allAboard.release();
    }

    public void busArrives() throws InterruptedException {
        busMutex.acquire();
        int boarding = Math.min(waitingCount, 50);
        System.out.println("Bus: Boarding " + boarding + " riders");

        for (int i = 0; i < boarding; i++) {
            waitingRiders.release();
            allAboard.acquire();
        }

        waitingCount -= boarding;
    }

    public void busDeparts() {
        busMutex.release();
    }
}