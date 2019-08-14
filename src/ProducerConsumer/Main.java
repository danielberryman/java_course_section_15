package ProducerConsumer;

import java.util.Random;

public class Main {
//    producer/consumer example
//    this example: producer creates a message and a consumer reads it
//    2 threads: 1 creates messages and the other reads them
//    Below both MyMessage methods are acting on the exact same object...
//    ...but they're synchronized so they won't bew able to access object...
//    ...if the other is accessing it.

    public static void main(String[] args) {
        MyMessage message = new MyMessage();
        (new Thread(new MyWriter(message))).start();
        (new Thread(new MyReader(message))).start();
    }
}

class MyMessage {
    private String message;
    private boolean empty = true;

    public synchronized String read() {
//        constantly checking MyMessage object empty field to see if it's false
//        when it's false we set empty to true and return (or read) the message
        while(empty) {
            try {
                wait();
            } catch(InterruptedException e) {

            }
        }
        empty = true;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) {
//        constantly checking MyMessage object field empty to see if it's true
//        when it is true we can write a message and set empty to false since we know this message hasn't been read yet
        while(!empty) {
            try {
                wait();
            } catch(InterruptedException e) {

            }
        }
        empty = false;
        this.message = message;
        notifyAll();
    }
}

class MyWriter implements Runnable {
    private MyMessage message;

    public MyWriter(MyMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        String messages[] = {
                "Humpty Dumpty sat on a wall",
                "Humpty Dumpty had a great fall",
                "All the king's horses and all the king's men",
                "Couldn't put Humpty together again"
        };

        Random random = new Random();

        for(int i=0; i<messages.length; i++) {
            message.write(messages[i]);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch(InterruptedException e) {

            }
        }
        message.write("Finished");
    }
}

class MyReader implements Runnable {
    private MyMessage message;

    public MyReader(MyMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        Random random = new Random();

        for(String latestMessage = message.read(); !latestMessage.equals("Finished"); latestMessage = message.read()) {
            System.out.println(latestMessage);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch(InterruptedException e) {

            }
        }
    }
}