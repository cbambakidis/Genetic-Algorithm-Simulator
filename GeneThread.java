import java.util.*;

public class GeneThread extends Thread {
    private int starter = 0;
    private int numEpochs;
    private int popSize;
    private int threadNum;
    private final static double MUTATION = .1;
    private ArrayList<Chromosome> CurrentPopulation = new ArrayList<>();
    private ArrayList<Chromosome> NextPopulation = new ArrayList<>();
    private Random random = new Random();
    private boolean isMoreItemsList = false;

    public GeneThread(ArrayList<Chromosome> currentPop, int numEpochs, int popSize, int threadNumber) {
        this.numEpochs = numEpochs;
        for (Chromosome X : currentPop) {
            Chromosome n = new Chromosome(X);
            CurrentPopulation.add(n);
        }
        // CurrentPopulation.addAll(currentPop);
        if (currentPop.get(0).size() > 10) {
            isMoreItemsList = true;
        }
        this.popSize = popSize;
        threadNum = threadNumber;
    }

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
            // MEMBERS OF THE NEXT POPULATION/
            CurrentPopulation.clear();
            for (int i = 0; i < popSize; i++) {
                CurrentPopulation.add(NextPopulation.get(i));
            }
            Collections.sort(CurrentPopulation);
            if (CurrentPopulation.get(0).getFitness() > topChromie.getFitness()) {
                topChromie = CurrentPopulation.get(0);
            }
            if (isMoreItemsList) {
                if (CurrentPopulation.get(0).getFitness() >= 7000) {
                    System.out.println("Thread " + threadNum + " has achieved greatness: " + topChromie);
                    return;
                }
            } else {
                if (CurrentPopulation.get(0).getFitness() >= 3400) {
                    System.out.println("Thread " + threadNum + " has achieved greatness: " + topChromie);
                    return;
                }
            }
            if (starter % 1000 == 1) {
                System.out.println("(Thread " + threadNum + "): Still computing..");
            }

            starter++;
        }
    }

    public void printTop(int x) {
        System.out.println("Thread " + threadNum + " :");
        CurrentPopulation.subList(0, x).forEach((s) -> {
            System.out.print(s);
        });
    }

    public Chromosome getTop() {
        return CurrentPopulation.get(0);
    }

    public ArrayList<Chromosome> getCurrentPopulation() {
        return CurrentPopulation;
    }
}
