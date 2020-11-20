import java.util.ArrayList;
import java.util.List;

public class BruteForce {
    /**
     * For lists of items 10 or under, we use this recursive method to bruteforce
     * it.
     * 
     * @param items is the list of items to find possible combos from.
     * 
     * @return the best possible combination of items.
     */
    public static Chromosome getOptimalSet(ArrayList<Item> items) throws InvalidArgumentException {
        // Throw our custom exception if the input is too large.
        if (items.size() > 10) {
            throw new InvalidArgumentException(
                    "Over 10 items: Can't bruteforce with " + items.size() + " items, (for your processors sake)");
        }

        // Call the recursive method on the list of items.
        ArrayList<ArrayList<Item>> listOfChromosomes = crack(items);
        ArrayList<Chromosome> arrayListOfCombos = new ArrayList<Chromosome>();

        // Convert from arraylists of items to arraylist of chromosomes.
        for (List<Item> D : listOfChromosomes) {
            ArrayList<Item> temp = new ArrayList<>();
            for (Item N : D) {
                temp.add(N);
            }
            Chromosome placeholder = new Chromosome(temp);
            arrayListOfCombos.add(placeholder);
        }

        // Get fittest chromosome out of all the possible combos, and return it.
        Chromosome fittest = arrayListOfCombos.get(0);
        for (int i = 0; i < arrayListOfCombos.size(); i++) {
            if (arrayListOfCombos.get(i).getFitness() > fittest.getFitness()) {
                fittest = arrayListOfCombos.get(i);
            }
        }
        return fittest;

    }

    /**
     * I adapted this method from the practice problem, as well as something I saw
     * from user "Maximin" on stackoverflow. Take an arraylist as input and return
     * an arraylist of arraylists of items as output.
     * 
     * @param input is the input list of items.
     * 
     * @return a chromosome in it's most basic form, an arraylist of arraylists of
     *         items.
     */
    private static ArrayList<ArrayList<Item>> crack(ArrayList<Item> input) {
        ArrayList<ArrayList<Item>> output = new ArrayList<ArrayList<Item>>();
        if (input.isEmpty()) {
            output.add(new ArrayList<Item>());
            return output;
        }
        ArrayList<Item> thelist = new ArrayList<Item>(input);
        Item first = thelist.get(0);
        List<Item> temp = thelist.subList(1, thelist.size());
        ArrayList<Item> rest = new ArrayList<Item>();
        rest.addAll(temp);
        for (ArrayList<Item> permutations : crack(rest)) {
            List<ArrayList<Item>> subLists = new ArrayList<ArrayList<Item>>();
            for (int i = 0; i <= permutations.size(); i++) {
                ArrayList<Item> subList = new ArrayList<Item>();
                subList.addAll(permutations);
                subList.add(i, first);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }
}
