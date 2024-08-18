package Canvas;

public class LifeRule {
    int[] born;
    int[] stay;

    public LifeRule(int[] born, int[] stay) {
        this.born = born;
        this.stay = stay;
    }

    public boolean checkIfBorn(int neighbors) {
        for (int i : this.born) {
            if (neighbors == i) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfStays(int neighbors) {
        for (int i : this.stay) {
            if (neighbors == i) {
                return true;
            }
        }
        return false;
    }
}
