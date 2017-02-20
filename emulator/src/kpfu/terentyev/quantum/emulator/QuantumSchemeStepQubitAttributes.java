package kpfu.terentyev.quantum.emulator;

/**
 * Created by alexandrterentyev on 19.05.15.
 */
public class QuantumSchemeStepQubitAttributes {
    public String gateID;

    /*
    * This equals to qubit is "upper than other"
    * */
    boolean controlled;
    public static String IdentityGateID = "IdentityGateID";

    public QuantumSchemeStepQubitAttributes(String gateID, boolean controlled) {
        this.gateID = gateID;
        this.controlled = controlled;
    }
    public QuantumSchemeStepQubitAttributes (){
        this.gateID = IdentityGateID;
        this.controlled=false;
    }
}
