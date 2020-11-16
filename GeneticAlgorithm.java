/*
 *  @author Constantine Bambakidis
 *  CS1181, Fall 2020
 *  Project 1
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This project is a genetic algorithm. The purpose is to find the best
 * combination of items to take, based on weight and value, during a zombie
 * apocalypse. There are 7 possible items, with varying values and weights. The
 * best chromosome will be one that can take the highest value without exceeding
 * 10 pounds. In this case, the highest possible value is 3400. This algorithm
 * is made to quickly solve that problem.
 */
public class GeneticAlgorithm {

    public static void main(String[] args) throws IOException, InterruptedException {
        int choice = 0;
        Scanner keyboard = new Scanner(System.in);
        int n = 0;
        int numThreads = 0;
        int numEpochs = 0;
        int popSize = 0;
        String textFile = new String();
        // Get user choice for wordlist.
        // Test change.
        while (n < 1) {
            try {
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
        // CONTROLS

        ArrayList<Item> itemList = new ArrayList<>(readData(textFile)); // Change list name.

        ArrayList<Chromosome> CurrentPopulation;

        // STEP ONE: INITIALIZE FIRST POPULATION WITH 10 MEMBERS.
        CurrentPopulation = initializePopulation(itemList, popSize);
        // Pass all threads current population, divided up.
        int epochsPerThread = numEpochs / numThreads;
        ArrayList<GeneThread> threadList = new ArrayList<GeneThread>();
        for (int i = 0; i < numThreads; i++) {
            GeneThread N = new GeneThread(CurrentPopulation, epochsPerThread, popSize / numThreads, i);
            threadList.add(N);
        }
        final long startTime = System.currentTimeMillis();
        for (GeneThread D : threadList) {
            D.start();
        }
        for (GeneThread D : threadList) {
            D.join();
        }
        final long endTime = System.currentTimeMillis();
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
        System.out.println("Average (top) fitness: " + avgFitness / count);
        for (Chromosome N : allTop) {
            if (N.getFitness() > best.getFitness()) {
                best = N;
            }
        }
        System.out.println("Best: " + best);

        System.out.println("From bruteforce: ");
        // System.out.println(BruteForce.getOptimalSet(itemList));
    }

    /**
     * This method parses the name, value and weight of the items from the list.
     *
     * @param filename is the name of the file with the list of items.
     * @return returns a usable ArrayList of item objects.
     * @throws FileNotFoundException in case the file doesn't exist.
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

    /**
     * This method initializes the population with the desired size and items.
     * Returns an ArrayList of chromosomes.
     *
     * @param items          is an ArrayList of items that each individual
     *                       chromosome will have. What's included for each member
     *                       is randomized in the constructor.
     * @param populationSize is the desired number of individuals per population
     *                       generated.
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
