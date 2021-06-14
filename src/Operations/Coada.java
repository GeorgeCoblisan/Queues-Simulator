package Operations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Coada {
    Queue<Client> clienti = new LinkedList<>();
    int current, index;

    public int getCurrent() {
        return clienti.peek().getServiceTime();
    }
    public void setCurrent(int current) {
        this.current = current;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public void removeClient() {
        clienti.remove(0);
    }
    public void updateCurrent(int current2) {
        clienti.peek().setServiceTime(current2);
    }
    public void addClient(Client c) {
        clienti.add(c);
    }

    public int getSize() {
        return clienti.size();
    }

    public int getSumServiceTime() {
        int sumaServiceTime = 0;
        if(clienti.isEmpty())
            return 0;
        for(Client c: clienti)
            sumaServiceTime = sumaServiceTime + c.getServiceTime();
        return sumaServiceTime;
    }

    public String toString() {
        if(clienti.isEmpty())
            return "0";
        String rez = "";
        for(Client c : clienti) {
            rez = rez + "(" +c.getID() + ", " +c.getArrivalTime() + ", " +c.getServiceTime() + ") ";
        }
        return rez;
    }

}
