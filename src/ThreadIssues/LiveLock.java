package ThreadIssues;

public class LiveLock {

    public static void main(String[] args) {
        final Worker worker1 = new Worker("Worker 1", false);
        final Worker worker2 = new Worker("Worker 2", true);

        final SharedResource sharedResource = new SharedResource(worker1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                worker1.work(sharedResource, worker2);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                worker2.work(sharedResource, worker1);
            }
        }).start();
    }

}

class SharedResource {
    private Worker owner;

    public SharedResource(Worker owner) {
        this.owner = owner;
    }

    public Worker getOwner() {
        return owner;
    }

    public synchronized void setOwner(Worker owner) {
        this.owner = owner;
    }
}

class Worker {
    private String name;
    private boolean active;

    public Worker(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public synchronized void work(SharedResource sharedResource, Worker otherWorker) {
        while(active) {
            if(sharedResource.getOwner() != this) {
                try {
                    wait(10);
                } catch (InterruptedException e) {

                }
                continue;
            }

            if(otherWorker.isActive()) {
                System.out.println(getName() + " : give the resource to the worker " + otherWorker.getName());
                sharedResource.setOwner(otherWorker);
                continue;
            }

            System.out.println(getName() + ": working on the common resource");
            active = false;
            sharedResource.setOwner(otherWorker);
        }
    }
}
