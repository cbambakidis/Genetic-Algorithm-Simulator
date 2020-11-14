import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BruteForce {
    // For lists of items 10 or under, we use this recursive method
    // To bruteforce it.
    public static ArrayList<Item> getOptimalSet(ArrayList<Item> items) {

        Collection<List<Item>> d = permute1(items);
        if(items.size() > 10){
            System.out.println("Invalid Parameters.");
            return null;
        }

       ArrayList<Chromosome> n = new ArrayList<>();
        for (List<Item> D : d) {
            ArrayList<Item> FF = new ArrayList<>();
            for (Item N : D){
                FF.add(N);
            }
            Chromosome L = new Chromosome(FF);
            n.add(L);
        }
        Chromosome fittest = n.get(0);

        for (int i = 0; i < n.size(); i++) {
            if (n.get(i).getFitness() > fittest.getFitness()) {
                fittest = n.get(i);
            }
        }
        return fittest;

    }
    //"Adapt" it to be non generic.
    public static <T> Collection<List<T>> permute1(Collection<T> input) {
        Collection<List<T>> output = new ArrayList<List<T>>();
        if (input.isEmpty()) {
            output.add(new ArrayList<T>());
            return output;
        }
        List<T> list = new ArrayList<T>(input);
        T head = list.get(0);
        List<T> rest = list.subList(1, list.size());
        for (List<T> permutations : permute1(rest)) {
            List<List<T>> subLists = new ArrayList<List<T>>();
            for (int i = 0; i <= permutations.size(); i++) {
                List<T> subList = new ArrayList<T>();
                subList.addAll(permutations);
                subList.add(i, head);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }
}
