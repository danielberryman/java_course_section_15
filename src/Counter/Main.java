package Counter;

public class Main {

    public static void main(String[] args) {
        Thread thread1 = new CounterThread();
        Thread thread2 = new CounterThread();
        thread1.start();
        thread2.start();
    }

    public static class Counter {
        private int i;

        public Counter(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }

    public static class CounterThread extends Thread {
        @Override
        public void run() {
            Counter counter = new Counter(10);
            while(counter.getI() > -1) {
                System.out.println("i = " + counter.getI());
                counter.setI(counter.getI()-1);
            }
        }
    }
}
