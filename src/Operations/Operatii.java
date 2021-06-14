package Operations;

import Thread.Task;

import java.util.ArrayList;
import java.util.Random;

public class Operatii {
    ArrayList<Client> waitingClients = new ArrayList<>();
    Random random = new Random();
    int max_op = 0;
    int secunda = 0;

    public int getSecunda() {
        return secunda;
    }

    public ArrayList<Client> generate(int nrClienti, int arrivalMIN, int arrivalMAX, int serviceMIN, int serviceMAX) {
        for (int i = 1; i <= nrClienti; i++) {
            int randomArrival = random.nextInt(arrivalMAX - arrivalMIN + 1) + arrivalMIN;
            int randomService = random.nextInt(serviceMAX - serviceMIN + 1) + serviceMIN;
            waitingClients.add(new Client(i, randomArrival, randomService));
        }
        return waitingClients;
    }

    public ArrayList<Task> startThreads(int nrCozi) {
        System.out.println("S-a pornit un thread");
        ArrayList<Task> threads = new ArrayList<>();
        for(int i=0; i<nrCozi; i++) {
            Task s = new Task(i);
            threads.add(s);
            s.start();
        }
        return threads;
    }

    public int findMin(ArrayList<Coada> cozi) {
        int indexMin = -1;
        int sumaMin = Integer.MAX_VALUE;
        for(Coada c:cozi) {
            if(c.getSumServiceTime() <= sumaMin) {
                sumaMin = c.getSumServiceTime();
                indexMin = c.getIndex();
            }
        }
        return indexMin;
    }

    public void findPeakHour(ArrayList<Coada> cozi, int secunda) {
        int suma_op = 0;
        for(Coada c: cozi)
            suma_op = suma_op + c.getSize();
        if(suma_op >= max_op) {
            max_op = suma_op;
            this.secunda = secunda;
        }
    }

    public double getAverageWaiting(double suma, int nrClienti, int nr) {
        double nr2 = Double.valueOf(nr);
        suma = suma + nr2;
        double nr1 = Double.valueOf(nrClienti);
        return suma/nr1;
    }

    public double getAverageService(ArrayList<Client> waitingClients, int nrClienti) {
        double suma = 0;
        double nr = Double.valueOf(nrClienti);
        for(Client c: waitingClients)
            suma = suma + c.getServiceTime();
        return suma/nr;
    }

    public double getAService(ArrayList<Client> waitingClients) {
        double suma = 0;
        for(Client c: waitingClients)
            suma = suma + c.getServiceTime();
        return suma;
    }

    public boolean checkIfEmpty(ArrayList<Boolean> emp) {
        boolean empty = true;
        for(boolean a : emp) {
            if(!a) empty = false;
        }
        return empty;
    }
}
