package Interface;

import Operations.Client;
import Operations.Coada;
import Operations.Operatii;
import Thread.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Interface {
    JPanel panel = new JPanel();
    JTextField clienti = new JTextField(5);
    JLabel text_clienti = new JLabel("N clienti ");
    JTextField cozi = new JTextField(5);
    JLabel text_cozi = new JLabel("Q cozi ");
    JTextField simulation_interval = new JTextField(5);
    JLabel text_simulation = new JLabel("Simulation interval ");
    JTextField arrivalTimeMin = new JTextField(5);
    JTextField arrivalTimeMax = new JTextField(5);
    JLabel text_arrival = new JLabel("Arrival time ");
    JTextField serviceTimeMin = new JTextField(5);
    JTextField serviceTimeMax = new JTextField(5);
    JLabel text_service = new JLabel("Service time ");
    JButton start = new JButton("START");
    JButton stop = new JButton("STOP");
    JPanel right = new JPanel();
    JTextField time = new JTextField(10);
    PrintWriter writer;

    int nrCozi, nrClienti, simulationTime, arrivalMin, arrivalMax, serviceMin, serviceMax;
    ArrayList<Client> waitingClients = new ArrayList<>();
    ArrayList<Coada> allCozi = new ArrayList<>();
    ArrayList<JTextField> textFields = new ArrayList<>();
    ArrayList<JLabel> labels = new ArrayList<>();
    ArrayList<Task> threads = new ArrayList<>();
    ArrayList<Boolean> checkSfarsit = new ArrayList<Boolean>();
    ArrayList<Boolean> checkCoziLibere = new ArrayList<Boolean>();
    Timer clock;
    Operatii o = new Operatii();
    Client clientt = new Client(0, 0, 0);
    boolean startThread = false;
    boolean ThreadsStarted = false;
    int secunde = 0, delay = 1000;
    double averageServing, averageWaiting;
    int nr = 0;

    public void addComponents() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panel.setBackground(new Color(27, 188, 196));

        try {
            writer = new PrintWriter("log.txt", "UTF-8");
        } catch (Exception e) {
            System.out.println("File not found");
        }

        c.gridy = 0;
        c.gridx = 0;
        panel.add(text_clienti, c);
        c.gridx = 1;
        panel.add(clienti, c);

        c.gridy = 1;
        c.gridx = 0;
        panel.add(text_cozi, c);
        c.gridx = 1;
        panel.add(cozi, c);

        c.gridy = 2;
        c.gridx = 0;
        panel.add(text_simulation, c);
        c.gridx = 1;
        panel.add(simulation_interval, c);

        c.gridy = 3;
        c.gridx = 0;
        panel.add(text_arrival, c);
        c.gridx = 1;
        panel.add(arrivalTimeMin, c);
        c.gridx = 2;
        panel.add(arrivalTimeMax, c);

        c.gridy = 4;
        c.gridx = 0;
        panel.add(text_service, c);
        c.gridx = 1;
        panel.add(serviceTimeMin, c);
        c.gridx = 2;
        panel.add(serviceTimeMax, c);

        right.setLayout(new GridBagLayout());
        right.setBackground(new Color(27, 188, 196));
        GridBagConstraints cc = new GridBagConstraints();
        cc.anchor = GridBagConstraints.CENTER;
        right.add(start, cc);

        c.gridx = 5;
        c.gridy = 4;

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 3;
                c.gridwidth  = 5;
                JLabel simulating = new JLabel("Start simulare ");
                panel.add(simulating);

                start.setVisible(false);
                startThread = true;
                c.gridx = 5;
                c.gridy = 5;

                if(clienti.getText().isEmpty() || cozi.getText().isEmpty() || simulation_interval.getText().isEmpty() || arrivalTimeMin.getText().isEmpty() || arrivalTimeMax.getText().isEmpty() || serviceTimeMin.getText().isEmpty() || serviceTimeMax.getText().isEmpty()) {
                    error("Date incomplete");
                    return;
                }

                nrClienti = Integer.parseInt(clienti.getText());
                nrCozi = Integer.parseInt(cozi.getText());
                simulationTime = Integer.parseInt(simulation_interval.getText());
                arrivalMin = Integer.parseInt(arrivalTimeMin.getText());
                arrivalMax = Integer.parseInt(arrivalTimeMax.getText());
                serviceMin = Integer.parseInt(serviceTimeMin.getText());
                serviceMax = Integer.parseInt(serviceTimeMax.getText());


                if (arrivalMax < arrivalMin || serviceMax < serviceMin) {
                    error("Ordine incorecta!");
                    return;
                }

                waitingClients = o.generate(nrClienti, arrivalMin, arrivalMax, serviceMin, serviceMax);
                waitingClients = clientt.sort(waitingClients);
                averageServing = o.getAverageService(waitingClients, nrClienti);
                averageWaiting = o.getAService(waitingClients);

                writer.println("ID | Arrival time | Service Time");
                for (Client c : waitingClients) {
                    c.afisareClient();
                    writer.println(c.getID() + " " + c.getArrivalTime() + " " + c.getServiceTime() + " ");
                }

                writer.println();
                panel.add(Box.createRigidArea(new Dimension(30, 40)));

                for (int i = 1; i <= nrCozi; i++) {
                    checkCoziLibere.add(false);
                    checkSfarsit.add(false);
                    JTextField servers = new JTextField(10);
                    allCozi.add(new Coada());
                    allCozi.get(i - 1).setIndex(i - 1);
                    textFields.add(servers);
                    servers.setEditable(false);
                    JLabel server = new JLabel("Server " + i);
                    labels.add(server);
                    c.gridy++;
                    c.gridx = 0;
                    panel.add(server, c);
                    c.gridx++;
                    panel.add(servers, c);
                }

                if (nrClienti < nrCozi) {
                    for (int j = 0; j < nrCozi - nrClienti; j++)
                        checkSfarsit.set(j, true);
                }

                right.add(stop, c);
                stop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetInterface();
                    }
                });

                clienti.setVisible(false);
                text_clienti.setVisible(false);
                cozi.setVisible(false);
                text_cozi.setVisible(false);
                simulation_interval.setVisible(false);
                text_simulation.setVisible(false);
                text_arrival.setVisible(false);
                arrivalTimeMax.setVisible(false);
                arrivalTimeMin.setVisible(false);
                text_service.setVisible(false);
                serviceTimeMax.setVisible(false);
                serviceTimeMin.setVisible(false);
                time.setVisible(false);
            }
        });

        ActionListener ceas = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.gridx = 0;
                c.gridy++;
                time.setEditable(false);
                time.setVisible(true);
                panel.add(time, c);

                System.out.println("Clock!");
                if (startThread) {
                    refreshInterface();
                    if (secunde == Integer.parseInt(simulation_interval.getText())) {
                        error("Sfarsit");
                        resetInterface();
                    } else
                        secunde++;
                    if (!ThreadsStarted) {
                        threads = o.startThreads(nrCozi);
                        ThreadsStarted = true;
                    }
                    String timp = "Time: " +secunde;
                    time.setText(timp);
                    time.setEditable(false);

                    if (waitingClients.isEmpty() == false) {
                        while (secunde == waitingClients.get(0).getArrivalTime()) {
                            int min = o.findMin(allCozi);
                            System.out.println("La secunda " + secunde + " a avut loc adaugare");
                            allCozi.get(min).addClient(waitingClients.get(0));
                            waitingClients.remove(0);
                            o.findPeakHour(allCozi, secunde);

                            if (waitingClients.isEmpty())
                                break;
                            startSimulating();
                        }
                    }
                    startSimulating();
                }

                for(Coada c: allCozi) {
                    if(c.getSize() > 1)
                        nr = nr + c.getSize() - 1;
                }
                try {
                    if (!allCozi.isEmpty()) {
                        writer.println("Time =" + secunde);
                        writer.print("Waiting clients: ");
                        for (Client c : waitingClients) {
                            writer.print("(" +c.getID() + ", " +c.getArrivalTime() + ", " +c.getServiceTime() + ") ");
                        }
                        writer.println();
                    }
                    for (int i = 0; i < nrCozi; i++) {
                        int j = i + 1;
                        writer.print("Queue " +j + ": ");
                        writer.println(allCozi.get(i).toString());
                    }
                } catch (Exception d) {
                }
            }
        };

        clock = new Timer(delay, ceas);
        clock.setInitialDelay(0);
        clock.start();
    }

       public void startSimulating() {
            int i = 0;
            System.out.println("Clienti ramasi = " + waitingClients.size());
            System.out.println();

            while (i < allCozi.size()) {
                if (waitingClients.isEmpty() && allCozi.isEmpty())
                    error("Sfarsit");
                if (allCozi.get(i).getSize() <= 0 && waitingClients.size() > 0)
                    checkCoziLibere.set(i, false);

                if (checkCoziLibere.get(i) == false) {
                    try {
                        int curent = allCozi.get(i).getCurrent();
                        threads.get(i).setCurrentPos(curent);
                    } catch (Exception E) {
                        i++;
                        continue; }
                    checkCoziLibere.set(i, true);
                } else {
                    try {
                        if (threads.get(i).getCurrentPos() <= 0) {
                            allCozi.get(i).removeClient();
                            System.out.println("S-a eliminat un client!");
                            System.out.println();

                            if (allCozi.get(i).getSize() > 0) {
                                threads.get(i).setCurrentPos(allCozi.get(i).getCurrent());
                            } else {
                                if (waitingClients.isEmpty())
                                    allCozi.get(i).removeClient();
                                else {
                                    continue;
                                }
                            }
                        } else {
                            int curent = threads.get(i).getCurrentPos();
                            allCozi.get(i).updateCurrent(curent);
                        }
                    } catch (Exception E) {
                        checkSfarsit.set(i, true);
                        if (o.checkIfEmpty(checkSfarsit) == true) {
                           error("Sfarsit");
                            i = nrClienti + 1;
                            resetInterface();
                        } else i++;
                        continue; }
                }
                i++;
            }
           refreshInterface();
        }

    public void error(String message) {
        if(!message.equals("Sfarsit")) {
            JOptionPane.showMessageDialog(null, message, "Eroare", JOptionPane.ERROR_MESSAGE);
            resetInterface();
        }
        if(message.equals("Sfarsit")) {
            writer.println("Average waiting time: " + String.format("%.2f", o.getAverageWaiting(averageWaiting, nrClienti, nr)));
            writer.println("Average service time: " + String.format("%.2f", (averageServing)));
            writer.println("Peak hour: " +o.getSecunda() + " seconds");
            JOptionPane.showMessageDialog(null, message, "Sfarsit simulare", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshInterface() {
        try{
            int j = 0;
            while(j < nrCozi) {
                textFields.get(j).setText(allCozi.get(j).toString());
                if(allCozi.get(j).getSize() <= 0 || allCozi.get(j).toString().equalsIgnoreCase("0")) {
                    textFields.get(j).setVisible(false);
                    labels.get(j).setVisible(false);
                }
                else {textFields.get(j).setVisible(true);
                    labels.get(j).setVisible(true);
                }
                j++;
            }
        }catch(Exception E) {}
        writer.println();
    }
    
    public void createInterface() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setVisible(true);
        this.addComponents();
        frame.add(panel, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);
        frame.setLocationRelativeTo(null);
    }

    public void resetInterface() {
        checkCoziLibere.clear();
        secunde = 0;
        startThread = false;
        waitingClients.clear();
        nrCozi = 0;
        checkSfarsit.clear();
        for(JTextField a : textFields)
            a.setVisible(false);
        for(JLabel a : labels)
            a.setVisible(false);
        textFields.clear();
        labels.clear();
        text_arrival.setVisible(true);
        text_service.setVisible(true);
        arrivalTimeMax.setVisible(true);
        arrivalTimeMin.setVisible(true);
        serviceTimeMax.setVisible(true);
        serviceTimeMin.setVisible(true);
        arrivalTimeMax.setText("");
        arrivalTimeMin.setText("");
        serviceTimeMax.setText("");
        serviceTimeMin.setText("");
        text_cozi.setVisible(true);
        text_arrival.setVisible(true);
        stop.setVisible(false);
        start.setVisible(true);
        simulation_interval.setVisible(false);
        cozi.setText("");
        time.setText("");
        clock.stop();
        writer.close();
        System.exit(1);
    }
}
