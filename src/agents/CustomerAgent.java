import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.util.Vector;

public class CustomerAgent extends Agent {

    private static final long serialVersionUID = -8345978142167560058L;

    private float balance;
    private float influentiability;
    private Vector<String> storePreferences;

    public CustomerAgent(float initBalance, Vector<String> storePreferences) {
        balance = initBalance;
        this.storePreferences = storePreferences;
    }

    public float getBalance() {
        return balance;
    }

    public Vector<String> getStorePreferences() {
        return storePreferences;
    }
}
