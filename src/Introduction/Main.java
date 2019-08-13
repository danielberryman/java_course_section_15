package Introduction;

import static Introduction.ThreadColor.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(ANSI_PURPLE+"Hello from the main thread!");

        Thread anotherThread = new AnotherThread();
        anotherThread.setName("== ANotherThread ++");
        anotherThread.start();

        new Thread() {
            public void run() {
                System.out.println(ANSI_CYAN+"Hello from the anonymous class thread!");
            }
        }.start();

        Thread myRunnableThread = new Thread(new MyRunnable() {
            @Override
            public void run() {
                System.out.println(ANSI_RED+"Hello from the anonymous classes implementation of run()");
                try {
                    anotherThread.join();
                    System.out.println(ANSI_RED + "Another thread terminated OR timed out so I'm running again");
                } catch(InterruptedException e) {
                    System.out.println(ANSI_RED + "I couldn't wait after all. I was interrupted");
                }
            }
        });

        myRunnableThread.start();


        System.out.println(ANSI_PURPLE+"Hello again");
    }
}
