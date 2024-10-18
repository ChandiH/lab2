public class Rider implements Runnable {
    private static int riderCount = 0;
    private int id;
    private BusStop busStop;

    public Rider(BusStop busStop) {
        this.busStop = busStop;
        this.id = ++riderCount;
    }

    @Override
    public void run() {
        try {
            System.out.println("Rider: Rider " + id + " arrives at bus stop");
            busStop.riderArrives();

            busStop.boardBus();
            System.out.println("Rider: Rider " + id + " boards the bus");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}