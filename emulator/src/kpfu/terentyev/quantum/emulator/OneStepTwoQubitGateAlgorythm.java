package kpfu.terentyev.quantum.emulator;

import kpfu.terentyev.quantum.emulator.Gates.UGate;

import java.util.HashMap;

/**
 * Created by aleksandrterentev on 08.03.16.
 */
public class OneStepTwoQubitGateAlgorythm extends QuantumAlgorithm {
    public OneStepTwoQubitGateAlgorythm(int qubitsInRegister,
                                        int firstQubitPosition,
                                        int secondQubitPosition,
                                        Complex[][] transformationMatrix) throws Exception {
        stepsNumber = 1;
        QuantumSchemeStepQubitAttributes [][] algSheme = new QuantumSchemeStepQubitAttributes[qubitsInRegister][1];
        String gateId = "ControlledGate";
        for (int i = 0; i < qubitsInRegister; i++){
            if (i == secondQubitPosition){
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes(gateId, false);
            }else if (i == firstQubitPosition){
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes(gateId, true);
            }else {
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes();
            }
        }
        gates = new HashMap<String, QuantumGate>();
        mainGateIDs = new String[]{gateId};
        QuantumGate gate = new UGate(2, transformationMatrix);
        gates.put(gateId, gate);
        algorithmSchemeMatrix = algSheme;
    }
}
