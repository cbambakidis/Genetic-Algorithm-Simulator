/*
 *  @author Constantine Bambakidis
 *  CS1181, Fall 2020
 *  Project 5
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/** 
 * Please Read: Multithreaded Project One - Genetic Algorithm. 
 * The only change from my Project 1 was to change my population size and number of epochs to
 * variables rather than constants. 
 * I also added: the dummy method in getFitness(), the
 * bruteforce method, the Invalid Exception for bruteforce, and the ability to
 * multithread. Running the algorithm more_items.txt was already terribly slow
 * before the dummy method, so it's pretty much unusable in this program unless
 * you have an AMD Threadripper.
 * 
 * Time difference for mutithreading: items.txt, 500 epochs, 10 population size.
 * 1 thread: 55900ms, 3400 average fitness. 
 * 2 threads: 7333ms, 3400 average fitness. 
 * 3 threads: 2227ms, 3233 average fitness. 
 * 4 threads: 1037ms, 2875 average fitness. 
 * 5 threads: 352ms, 3400 average fitness. 
 * 6 threads: 259ms,3400 average fitness. 
 * 7 threads: 275ms, 3400 average fitness. 
 * 8 threads: 231ms, 3050 average fitness. 
 * Multithreading the program saves me nearly an entire minute vs using one just thread.
 */
public class GeneticAlgorithm {

    public static void main(String[] args) throws IOException, InterruptedException {
        int choice = 0;
        final Scanner keyboard = new Scanner(System.in);
        int n = 0;
        int numThreads = 0;
        int numEpochs = 0;
        int popSize = 0;
        String textFile = new String();

        // Get user choice for list choice, population size, epochs, number of threads.
        while (n < 1) {
            try {
                System.out.println(
                "Note: I recommend using the basic items file for testing.\nIn project one, I found that it takes about 7000 epochs to complete the more_items list. This takes\nan eternity to proccess in this project, because the dummy method is added.");
                System.out.print("Please enter [1] for items.txt or [2] for more_items.txt: ");
                choice = keyboard.nextInt();
                keyboard.nextLine();
                switch (choice) {
                    case 1:
                        textFile = "items.txt";
                        n++;
                        break;
                    case 2:
                        textFile = "more_items.txt";
                        n++;
                        break;
                    default:
                        n = 0;
                        break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: you must enter a whole number, either [1] or [2].");
                keyboard.nextLine();
            }
        }
        n = 0;
        while (n < 1) {
            try {
                System.out.print("Please enter the desired number of threads(1-8): ");
                choice = keyboard.nextInt();
                keyboard.nextLine();
                if (choice < 9 && choice > 0) {
                    numThreads = choice;
                    n++;
                    break;
                } else {
                    n = 0;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: you must enter a number between 1 and 8.");
                keyboard.nextLine();
            }
        }

        n = 0;
        while (n < 1) {
            try {
                System.out.print("Please enter the desired number of epochs total: ");
                choice = keyboard.nextInt();
                keyboard.nextLine();
                if (choice > 0) {
                    numEpochs = choice;
                    n++;
                    break;
                } else {
                    n = 0;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: you must enter a whole number.");
                keyboard.nextLine();
            }
        }
        n = 0;
        while (n < 1) {
            try {
                System.out.print("Please enter the desired starting population size: ");
                choice = keyboard.nextInt();
                keyboard.nextLine();
                if (choice > 0) {
                    popSize = choice;
                    n++;
                    break;
                } else {
                    n = 0;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: you must enter a whole number.");
                keyboard.nextLine();
            }
        }
        keyboard.close();

        ArrayList<Item> itemList = new ArrayList<>(readData(textFile));
        ArrayList<Chromosome> CurrentPopulation;

        // STEP ONE: INITIALIZE FIRST POPULATION WITH popSize MEMBERS.
        CurrentPopulation = initializePopulation(itemList, popSize);

        // Pass all threads current population, divided up. Add remainder to last
        // thread, since java does integer divison.
        int epochsPerThread = numEpochs / numThreads;
        int remainder = numEpochs - (epochsPerThread * numThreads);

        // ArrayList of threads.
        ArrayList<GeneThread> threadList = new ArrayList<GeneThread>();
        for (int i = 0; i < numThreads; i++) {

            // This part adds the remainder number of epochs to the last thread
            if (i == numThreads - 1 && remainder != 0) {
                GeneThread G = new GeneThread(CurrentPopulation, epochsPerThread + remainder, popSize / numThreads,
                        i + 5);
                threadList.add(G);
                break;
            }

            // Each thread is initialized with the current population, amount of times it
            // should run, population size it should run for, and a thread number.
            GeneThread N = new GeneThread(CurrentPopulation, epochsPerThread, popSize / numThreads, i + 1);
            threadList.add(N);
        }

        // Start timer, start all threads, join all threads, end the timer.
        final long startTime = System.currentTimeMillis();
        for (GeneThread D : threadList) {
            D.start();
        }
        for (GeneThread D : threadList) {
            D.join();
        }
        final long endTime = System.currentTimeMillis();

        // Print all the stuff.
        System.out.println("Time: " + (endTime - startTime) + "ms");
        ArrayList<ArrayList<Chromosome>> X = new ArrayList<>();
        ArrayList<Chromosome> allTop = new ArrayList<>();
        int avgFitness = 0;
        int count = 0;
        for (GeneThread D : threadList) {
            count++;
            allTop.add(D.getTop());
            X.add(D.getCurrentPopulation());
            avgFitness += D.getTop().getFitness();
        }
        Chromosome best = new Chromosome();
        System.out.println("Average of each threads best chromosome fitness: " + avgFitness / count);
        for (Chromosome N : allTop) {
            if (N.getFitness() > best.getFitness()) {
                best = N;
            }
        }
        System.out.println("Best chromosome across all threads: " + best);

        // Call the bruteforce method
        System.out.println("From bruteforce: ");
        try {
            System.out.println(BruteForce.getOptimalSet(itemList));
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method parses the name, value and weight of the items from the list.
     *
     * @param filename is the name of the file with the list of items.
     * 
     * @return returns a usable ArrayList of item objects.
     * 
     * @throws FileNotFoundException in case the file doesn't exist.
     * 
     * @throws IOException
     */
    public static ArrayList<Item> readData(String filename) throws FileNotFoundException, IOException {
        ArrayList<Item> itemsList = new ArrayList<>();
        try (FileReader text = new FileReader(filename)) {
            Scanner fileIn = new Scanner(text);
            String name;
            int value;
            double weight;
            while (fileIn.hasNextLine()) {
                String line = fileIn.nextLine();
                String[] parts = line.split(", ");
                name = (parts[0]);
                weight = (Double.parseDouble(parts[1]));
                value = (Integer.parseInt(parts[2]));
                Item newItem = new Item(name, weight, value);
                itemsList.add(newItem);
            }
            text.close();
            fileIn.close();
        }
        return itemsList;
    }

    /*
     * This method initializes the population with the desired size and items.
     * Returns an ArrayList of chromosomes.
     *
     * @param items is an ArrayList of items that each individual chromosome will
     * have. What's included for each member is randomized in the constructor.
     * 
     * @param populationSize is the desired number of individuals per population
     * generated.
     * 
     * @return returns a population with randomized items.
     */
    public static ArrayList<Chromosome> initializePopulation(ArrayList<Item> items, int populationSize) {
        ArrayList<Chromosome> Generation = new ArrayList<Chromosome>();
        for (int i = 0; i < populationSize; i++) {
            Chromosome initialGen = new Chromosome(items);
            Generation.add(initialGen);
        }

        return Generation;
    }
}
