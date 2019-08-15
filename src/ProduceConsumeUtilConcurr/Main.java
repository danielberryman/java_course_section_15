package ProduceConsumeUtilConcurr;

import java.util.Random;
import java.util.concurrent.*;

import static ProduceConsumeUtilConcurr.Main.EOF;

public class Main {
    public static final String EOF = "EOF";

    public static void main(String[] args) {
        ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<String>(6);
//        ReentrantLock bufferLock = new ReentrantLock();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Producer producer = new Producer(buffer, ThreadColor.ANSI_BLUE);
        Consumer consumer1 = new Consumer(buffer, ThreadColor.ANSI_GREEN);
        Consumer consumer2 = new Consumer(buffer, ThreadColor.ANSI_RED);

//        new Thread(producer).start();
//        new Thread(consumer1).start();
//        new Thread(consumer2).start();
        executorService.execute(producer);
        executorService.execute(consumer1);
        executorService.execute(consumer2);

        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(ThreadColor.ANSI_RED + "I'm being printed for the Callable class");
                return "This is the callable result";
            }
        });

        try {
            System.out.println(future.get());
        } catch (ExecutionException e) {
            System.out.println("Something went wrong");
        } catch (InterruptedException e) {
            System.out.println("Thread running the task was interrupted");
        }

        executorService.shutdown();
    }
}

class Producer implements Runnable {
    private ArrayBlockingQueue<String> buffer;
    private String color;

    public Producer(ArrayBlockingQueue<String> buffer, String color) {
        this.buffer = buffer;
        this.color = color;
//        this.bufferLock = bufferLock;
    }

    @Override
    public void run() {
        Random r = new Random();
        String[] nums = {"2", "3", "4", "5"};
        for(String num : nums) {
            try {
                System.out.println(color + "Adding..." + num);
                buffer.put(num);

                Thread.sleep(r.nextInt(1000));
            } catch(InterruptedException e) {
                System.out.println("Producer was interrupted");
            }
        }
        System.out.println(color + "Adding EOF and exiting...");
        try {
            buffer.put("EOF");
        } catch (InterruptedException e) {
        }
    }
}

class Consumer implements Runnable {
    private ArrayBlockingQueue<String> buffer;
    private String color;

    public Consumer(ArrayBlockingQueue<String> buffer, String color) {
        this.buffer = buffer;
        this.color = color;
    }

    @Override
    public void run() {
        int counter = 0;
        while(true) {
            synchronized (buffer) {
                try {
                    if (buffer.isEmpty()) {
                        continue;
                    }
                    if (buffer.peek().equals(EOF)) {
                        System.out.println(color + "Exiting");
                        break;
                    } else {
                        System.out.println(color + "Removed" + buffer.take());
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    }
}
