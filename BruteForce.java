import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
        Item[] t = new Item[items.size()];
        for (int i = 0; i < items.size(); i++) {
            t[i] = items.get(i);
        }
        ArrayList<Item[]> returnSet = getAllCombinations(t);
       ArrayList<Chromosome> n = new ArrayList<>();
      // List<List<Item>> n;
        for (List<Item> D : d) {
            Chromosome L = new Chromosome();
            for (int g = 0; g < D.size(); g++) {
                L.add(D.get(g));
            }
            n.add(L);
        }
        Chromosome fittest = new Chromosome();

        for (int i = 0; i < n.size(); i++) {
            if (n.get(i).getFitness() > fittest.getFitness()) {
                fittest = n.get(i);
            }
        }
        return fittest;

    }

    public static ArrayList<Item[]> getAllCombinations(Item[] s) {
        ArrayList<Item[]> allPossibleCombos = new ArrayList<>();
        // Base Case. if there are no more items in the list, add it to the possible
        // combo.
        if (s.length <= 1) {
            allPossibleCombos.add(s);
        }

        for (int i = 0; i < s.length; i++) {
            // Get item to start permutations with.
            Item firstItem = s[i];
            // SubList of items to permute with.
            Item[] smaller = new Item[s.length];
            // Add the items leading up to current index to smaller list.
            for (int t = 0; t < i; t++) {
                smaller[t] = s[t];
            }
            // then add items from index+1 all the way to end of length.
            for (int n = i + 1; n < s.length; n++) {
                smaller[n] = s[n];
            }
            // Temp arraylist of items.
            ArrayList<Item[]> temp = getAllCombinations(smaller);
            // Add items to allpossiblecombos.
            for (int j = 0; j < temp.size(); j++) {
                ArrayList<Item> temp2 = new ArrayList<>();
                temp2.add(firstItem);
                // Add first item, then all rest of items from arraylist.
                for (int k = 0; k < temp.get(j).length; k++) {
                    temp2.add(temp.get(j)[k]);
                }
                allPossibleCombos.add((Item[]) temp2.toArray());
            }
        }
        return allPossibleCombos;

    }
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









    public static HashSet<String> permute(String s) {
        HashSet<String> permutaions = new HashSet<>(); // List of arraylists of items.
        // base
        if (s.length() <= 1) { // If no items in arraylist, add item to final list.
            permutaions.add(s);
        }

        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i); // First letter to permute with.
            String smaller = s.substring(0, i) + s.substring(i + 1); // Sub array of items to permute it with.
            // make recursive call with "smaller"
            // that returns a HashSet, store that in a variable
            HashSet<String> temp = permute(smaller); // Temp list of items from permuting the smaller arraylist of
                                                     // items.
            for (int j = 0; j < temp.size(); j++) {
                permutaions.add(letter + temp.toArray()[j].toString());
            }
            // loop through that HashSet and add "letter" to the beginning of each string in
            // the HashSet
            // add the resulting string (with the letter added) to the HS called
            // permutations
        }
        return permutaions;
    }
}
