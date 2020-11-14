/*
 *  @author Constantine Bambakidis
 *  CS1181, Fall 2020
 *  Project 1
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Random;
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

    public static void main(String[] args) throws IOException {
        int choice = 0;
        Scanner keyboard = new Scanner(System.in);
        int n = 0;
        String textFile = new String();
        //Get user choice for wordlist.
        //Test change.
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
        //CONTROLS
        ArrayList<Item> itemList = new ArrayList(readData(textFile)); //Change list name.
        final double MUTATION = .1;
        final int STARTINGSIZE = 10;
        int goal;
        if (choice == 1) {
            goal = 3400;
            System.out.println("Goal fitness set to 3400. Computing");
        } else {
            goal = 7000;
            System.out.println("Goal fitness set to 7000. Please wait, this list takes a few minutes... I promise it works");
        }
        final int GOAL = goal;
        int topFitness = 0;
        int currentRun = 0;
        ArrayList<Chromosome> CurrentPopulation;
        ArrayList<Chromosome> NextPopulation = new ArrayList();

        //STEP ONE: INITIALIZE FIRST POPULATION WITH 10 MEMBERS.
        CurrentPopulation = initializePopulation(itemList, STARTINGSIZE);
        NextPopulation.addAll(CurrentPopulation);
        Random random = new Random();
        
        while (topFitness < GOAL) {
            currentRun++;

            //STEP 2: ADD EACH OF THE INDIVIDUALS IN THE CURRENT POPULATION TO THE NEXT POPULATION
            NextPopulation.addAll(CurrentPopulation);

            //STEP 3: RANDOMLY PAIR OFF THE INDIVIDUALS AND PERFORM CROSSOVER. ADD CHILD TO NEXT POPULATION AS WELL.
            Collections.shuffle(CurrentPopulation);
            for (int g = 0; g < CurrentPopulation.size(); g++) {
                NextPopulation.add(CurrentPopulation.get(g).crossover(NextPopulation.get(g)));
            }

            //STEP 4: APPLY MUTATION TO 10% OF THE NEXT POPULATION.
            for (int i = 0; i < (int) Math.floor(NextPopulation.size() * MUTATION); i++) {
                int j = random.nextInt(NextPopulation.size());
                NextPopulation.get(j).mutate();
            }

            //STEP 5: SORT THE MEMBERS OF THE NEXT POPULATION ACCORDING TO THEIR FITNESS.
            Collections.sort(NextPopulation);

            //STEP 6: CLEAR OUT THE OLD POPULATION AND ADD IN THE TOP 10 MEMBERS OF THE NEXT POPULATION/
            CurrentPopulation.clear();
            for (int i = 0; i < 10; i++) {
                CurrentPopulation.add(NextPopulation.get(i));
            }

            //for testing purposes
            topFitness = NextPopulation.get(0).getFitness();
            Collections.sort(CurrentPopulation);
            if(currentRun % 1000 == 1){
                System.out.println("Still computing..");
            }
            //System.out.println("[Current run:" + currentRun + " Top fitness:" + topFitness + "]");
        }

        //Show top 10 chromosomes. Population is sorted on line 66.
        CurrentPopulation.subList(0, 10).forEach((s) -> {
            System.out.print(s);
        });
        System.out.println("Took " + currentRun + " runs");

        System.out.println("From bruteforce: ");
        System.out.println(BruteForce.permute1(itemList));
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
        ArrayList<Item> itemsList = new ArrayList();
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
        }
        return itemsList;
    }

    /**
     * This method initializes the population with the desired size and items.
     * Returns an ArrayList of chromosomes.
     *
     * @param items is an ArrayList of items that each individual chromosome
     * will have. What's included for each member is randomized in the
     * constructor.
     * @param populationSize is the desired number of individuals per population
     * generated.
     * @return returns a population with randomized items.
     */
    public static ArrayList<Chromosome> initializePopulation(ArrayList<Item> items, int populationSize) {
        ArrayList<Chromosome> Generation = new ArrayList();
        for (int i = 0; i < populationSize; i++) {
            Chromosome initialGen = new Chromosome(items);
            Generation.add(initialGen);
        }

        return Generation;
    }
}
