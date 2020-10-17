import jade.core.Agent;
import jade.behaviours.Behaviour;
import java.util.Vector;

public class CustomerAgent implements Agent{

    private String name;
    private float wallet;
    private Vector<String> storePreferences;

    public CustomerAgent(String name, float wallet, Vector<String> storePreferences){
        this.name = name;
        this.wallet = wallet;
        this.storePreferences = storePreferences;
    }

    public String getName() {
        return this.name;
    }

    public float getWallet() {
        return this.wallet;
    }

    public Vector<String> getStorePreferences {
        return this.storePreferences;
    }
}
