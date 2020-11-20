import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
public class GeneThread extends Thread {
    private int starter = 0;
    private int numEpochs;
    private int popSize;
    private int goal;
    private int threadNum;
    private final static double MUTATION = .1;
    private ArrayList<Chromosome> CurrentPopulation = new ArrayList<>();
    private ArrayList<Chromosome> NextPopulation = new ArrayList<>();
    private Random random = new Random();
    /** 
     * This is the constructor for the thread.
     * 
     * @param currentPop is the current population being divided among the threads.
     * 
     * @param numEpochs is the number of iterations the thread will run for.
     * 
     * @param popSize is the population size for the thread to use for each
     * iteration, e.i how many items to keep each generation
     * 
     * @param threadNumber is just to help keep track of things.
     * 
     * @return GeneThread a new gene thread, fully parameterized and ready to run
     * the original algorithm.
     */
    public GeneThread(ArrayList<Chromosome> currentPop, int numEpochs, int popSize, int threadNumber) {
        this.numEpochs = numEpochs;

        // Copy the current population into this thread.
        for (Chromosome X : currentPop) {
            Chromosome n = new Chromosome(X);
            CurrentPopulation.add(n);
        }

        // We set a goal fitness so the thread knows when to terminate, if it's solved the puzzle.
        if (currentPop.get(0).size() > 10) {
            goal = 7000;
        } else {
            goal = 3400;
        }
        this.popSize = popSize;
        threadNum = threadNumber;
    }

    /*
     * I pretty much just copied my entire old genetic algorithm into this method, since it
     * works fine. There wasn't much to change.
     */
    public void run() {
        System.out.println("Thread " + threadNum + " started.");
        Chromosome topChromie = new Chromosome();
        while (starter != numEpochs) {
            NextPopulation.addAll(CurrentPopulation);

            // STEP 3: RANDOMLY PAIR OFF THE INDIVIDUALS AND PERFORM CROSSOVER. ADD CHILD TO
            // NEXT POPULATION AS WELL.
            Collections.shuffle(CurrentPopulation);
            for (int g = 0; g < CurrentPopulation.size(); g++) {
                NextPopulation.add(CurrentPopulation.get(g).crossover(NextPopulation.get(g)));
            }

            // STEP 4: APPLY MUTATION TO 10% OF THE NEXT POPULATION.
            for (int i = 0; i < (int) Math.floor(NextPopulation.size() * MUTATION); i++) {
                int j = random.nextInt(NextPopulation.size());
                NextPopulation.get(j).mutate();
            }

            // STEP 5: SORT THE MEMBERS OF THE NEXT POPULATION ACCORDING TO THEIR FITNESS.
            Collections.sort(NextPopulation);

            // STEP 6: CLEAR OUT THE OLD POPULATION AND ADD IN THE TOP PopulationSize
            // MEMBERS OF THE NEXT POPULATION
            CurrentPopulation.clear();
            for (int i = 0; i < popSize; i++) {
                CurrentPopulation.add(NextPopulation.get(i));
            }
            Collections.sort(CurrentPopulation);

            // Check if this thread has cracked the code. If it has, there's no point in it
            // running, so we return and print our best chromosome to the console.
            if (CurrentPopulation.get(0).getFitness() > topChromie.getFitness()) {
                topChromie = CurrentPopulation.get(0);
                if (CurrentPopulation.get(0).getFitness() >= goal) {
                    System.out.println("Thread " + threadNum + " has cracked the code: " + topChromie);
                    return;
                }
            }

            // Keeps track of how far along the program is for the user.
            if (starter % 1000 == 1) {
                System.out.println("(Thread " + threadNum + "): Still computing..");
            }
            starter++;
        }
    }

    public Chromosome getTop() {
        return CurrentPopulation.get(0);
    }

    public ArrayList<Chromosome> getCurrentPopulation() {
        return CurrentPopulation;
    }
}
