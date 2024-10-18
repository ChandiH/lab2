import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Main {
    private static final double BUS_MEAN_ARRIVAL_TIME = 0.5;
    private static final double RIDER_MEAN_ARRIVAL_TIME = 0.01;

    public static void main(String[] args) {
        Random random = new Random();
        BusStop busStop = new BusStop();

        // Create a scheduled executor service with 100 threads
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1000);

        scheduleBusArrivals(executorService, busStop, random);
        scheduleRiderArrivals(executorService, busStop, random);

        // Run simulation for a specific duration
        try {
            TimeUnit.HOURS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        System.out.println("Simulation ended");
    }

    private static void scheduleBusArrivals(ScheduledExecutorService executorService, BusStop busStop, Random random) {
        // Schedule bus arrivals with exponential distribution
        Runnable busScheduler = new Runnable() {
            @Override
            public void run() {
                executorService.execute(new Bus(busStop));
                long delay = exponentialDistribution(BUS_MEAN_ARRIVAL_TIME, random);
                System.out.println("Next bus will arrives in " + delay + " seconds");
                executorService.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        };
        executorService.submit(busScheduler);
    }

    private static void scheduleRiderArrivals(ScheduledExecutorService executorService, BusStop busStop,
                                              Random random) {
        // Schedule rider arrivals with exponential distribution
        Runnable riderScheduler = new Runnable() {
            @Override
            public void run() {
                executorService.execute(new Rider(busStop));
                executorService.schedule(this, exponentialDistribution(RIDER_MEAN_ARRIVAL_TIME, random), TimeUnit.MILLISECONDS);
            }
        };
        executorService.submit(riderScheduler);
    }

    private static long exponentialDistribution(double lambda, Random random){
        // Generate random delay based on exponential distribution
        return (long) (Math.log(1 - random.nextDouble()) / (-1.0 / lambda) * 60 * 1000);
    }
}