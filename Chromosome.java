
import java.util.ArrayList;
import java.util.Random;

/**
 * This class allows the creation Chromosome objects, which are essentially
 * lists of Items. Individually, they represent a member of a population.
 */
public class Chromosome extends ArrayList<Item> implements Comparable<Chromosome> {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Random rng = new Random();

    public Chromosome() {
    }

    //Constructor. Takes a list of items and creates a chromosome object.
    public Chromosome(ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            int r = rng.nextInt((1 - 0) + 1);
            if (r == 0) {
                Item temp = items.get(i);
                temp.setIncluded(false);
                this.add(new Item(temp));
            } else {
                Item temp = items.get(i);
                temp.setIncluded(true);
                this.add(new Item(temp));
            }

        }

    }

    //The crossover method compares this chromosome with another chromosome and returns a child with randomized results based on the parents.
    public Chromosome crossover(Chromosome other) {
        ArrayList<Item> testerR = new ArrayList();
        for (int i = 0; i < other.size(); i++) {
            int r = rng.nextInt((10));
            if (r < 6) {
                testerR.add(other.get(i));
            } else if (r > 5) {
                testerR.add(this.get(i));
            }
        }
        Chromosome child = new Chromosome(testerR);
        return child;
    }

    //When applied, this Mutate method has a 10% chance of flipping the items inclusivity.
    public void mutate() {
        int[] mutationOdds = new int[this.size()];
        for (int j = 0; j < mutationOdds.length; j++) {
            mutationOdds[j] = rng.nextInt(10);
            if (mutationOdds[j] == 1) {
                if (this.get(j).isIncluded() == false) {
                    this.get(j).setIncluded(true);
                } else {
                    this.get(j).setIncluded(false);
                }

            }
        }

    }

    //Get fitness method. Returns fitness.
    public int getFitness() {
        double totalWeight = 0;
        int totalValue = 0;
        if(this.size() == 0){
            return 0;
        }
        for (Item aThi : this) {
            if (aThi.isIncluded()) {
                totalWeight += aThi.getWeight();
                totalValue += aThi.getValue();
            }
        }
        if (totalWeight >= 10) {
            return 0;
        } else {
            return totalValue;
        }

    }

    //toString method. Returns the fitness followed by the items this individual has. NetBeans reccomended using this way and it seems more efficient.
    @Override
    public String toString() {
        String s = "";
        s = this.stream().filter((Item aThi) -> aThi.isIncluded()).map((aThi) -> ((aThi.toString()))).reduce(s, String::concat);
        return s + "\t->" + this.getFitness() + "\n";
    }

    //CompareTo method. Sorts the most fit individual to the top of the list.
    @Override
    public int compareTo(Chromosome o) {
        int comparison = 0;
        if (this.getFitness() < o.getFitness()) {
            comparison += 1;
        }
        if (this.getFitness() > o.getFitness()) {
            comparison -= 1;
        }
        if (comparison != 0) {
            return comparison;
        }
        return comparison;
    }
}
