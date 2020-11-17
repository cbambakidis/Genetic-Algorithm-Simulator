import java.util.ArrayList;
import java.util.List;

public class BruteForce {
    // For lists of items 10 or under, we use this recursive method
    // To bruteforce it.
    //MAKE CUSTOM ECXEPTION CLASS 
    public static ArrayList<Item> getOptimalSet(ArrayList<Item> items) throws InvalidArgumentException {
        if (items.size() > 10) {
                throw new InvalidArgumentException("Over 10 items: Can't bruteforce with " + items.size() + " items, for your processors sake.");
        }
        ArrayList<ArrayList<Item>> listOfChromosomes = crack(items);
        ArrayList<Chromosome> arrayListOfCombos = new ArrayList<Chromosome>();
        //Convert from arraylists of items to arraylist of chromosomes. Couldn't figure out how to do it with wrappers.
        for (List<Item> D : listOfChromosomes) {
            ArrayList<Item> temp = new ArrayList<>();
            for (Item N : D) {
                temp.add(N);
            }
            Chromosome placeholder = new Chromosome(temp);
            arrayListOfCombos.add(placeholder);
        }
        //Get fittest chromosome out of all the possible combos.
        Chromosome fittest = arrayListOfCombos.get(0);
        for (int i = 0; i < arrayListOfCombos.size(); i++) {
            if (arrayListOfCombos.get(i).getFitness() > fittest.getFitness()) {
                fittest = arrayListOfCombos.get(i);
            }
        }
        return fittest;

    }

    // I adapted this method from the practice problem as well as from a question user "Maximin" on stackoverflow.
    //Take an arraylist as input and return an arraylist of arraylists of items as output.
    private static ArrayList<ArrayList<Item>> crack(ArrayList<Item> input) {
        ArrayList<ArrayList<Item>> output = new ArrayList<ArrayList<Item>>();
        if (input.isEmpty()) {
            output.add(new ArrayList<Item>());
            return output;
        }
        ArrayList<Item> list = new ArrayList<Item>(input);
        Item head = list.get(0);
        List<Item> temp = list.subList(1, list.size());
        ArrayList<Item> rest = new ArrayList<Item>();
        rest.addAll(temp);
        for (ArrayList<Item> permutations : crack(rest)) {
            List<ArrayList<Item>> subLists = new ArrayList<ArrayList<Item>>();
            for (int i = 0; i <= permutations.size(); i++) {
                ArrayList<Item> subList = new ArrayList<Item>();
                subList.addAll(permutations);
                subList.add(i, head);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }
}
