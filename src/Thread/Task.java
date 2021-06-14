package Thread;

public class Task extends Thread{
    int currentPos = 0, index;
    boolean start = false;

    public Task(int index) {
        this.index = index;
    }

    public int getCurrentPos() {
        return currentPos;
    }
    public void setCurrentPos(int i){
        this.currentPos = i;
        if(i != 0)
            start = true;
    }

    public void run() {
        while(true) {
            if(start == true)
                currentPos--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Exceptie in thread!");
            }
        }
    }
}
