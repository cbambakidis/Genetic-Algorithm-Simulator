
/**
 *
 * @author Costa Bambakidis
 */
public class Item {

    private final String name;
    private final double weight;
    private final int value;
    private boolean included;

    //constructors
    public Item(String name, double weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    public Item(Item other) {
        this.name = other.name;
        this.weight = other.weight;
        this.value = other.value;
        this.included = other.included;
    }

    //getters
    public double getWeight() {
        return weight;

    }

    public int getValue() {
        return value;

    }

    public boolean isIncluded() {
        return included;
    }

    //setters
    public void setIncluded(boolean included) {
        this.included = included;
    }

    //toString method
    @Override
    public String toString() {
        return name + "(" + weight + " lbs," + "$" + value + ")";
    }

}
