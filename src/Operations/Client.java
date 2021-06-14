package Operations;

import java.util.ArrayList;

public class Client {
    int ID, arrivalTime, serviceTime;

    public Client(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void afisareClient() {
        System.out.println("ID = " +ID +" ArrivalTime = " +arrivalTime + " si serviceTime = " +serviceTime);
    }

    public ArrayList<Client> sort (ArrayList<Client> waitingClients) {
        ArrayList<Client> newList = new ArrayList<Client>();
        int ID = 1, minArr, serv = 0;

        while(waitingClients.size() > 0) {
            int index = -1, indexMin = 0;
            minArr = Integer.MAX_VALUE;
            for (Client c : waitingClients) {
                index++;
                if(c.getArrivalTime() < minArr) {
                    indexMin = index;
                    minArr = c.getArrivalTime();
                    serv = c.getServiceTime();
                }
            }
            newList.add(new Client(ID, minArr, serv));
            ID++;
            waitingClients.remove(indexMin);
        }
        return newList;
    }
}
