package kpfu.terentyev.quantum.emulator;

import kpfu.terentyev.quantum.emulator.Gates.UGate;
import kpfu.terentyev.quantum.emulator.QuantumAlgorithm;

import java.util.HashMap;
import java.util.List;

/**
 * Created by aleksandrterentev on 02.04.16.
 */

/**
 * Algorythm matrix must math to qubits order in algorythm scheme
 * */
public class OneStepAlgorythm extends QuantumAlgorithm {
    public OneStepAlgorythm(int qubitsInRegister,
                                        List<Integer> gateQubitIndexes,
                                        Complex[][] transformationMatrix) throws Exception {
        stepsNumber = 1;
        QuantumSchemeStepQubitAttributes [][] algSheme = new QuantumSchemeStepQubitAttributes[qubitsInRegister][1];
        String gateId = "Gate";
        for (int i = 0; i < qubitsInRegister; i++){
            if (gateQubitIndexes.contains(new Integer(i))){
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes(gateId, false);
            }else {
                algSheme[i][0] = new QuantumSchemeStepQubitAttributes();
            }
        }
        gates = new HashMap<String, QuantumGate>();
        mainGateIDs = new String[]{gateId};
        QuantumGate gate = new UGate(gateQubitIndexes.size(), transformationMatrix);
        gates.put(gateId, gate);
        algorithmSchemeMatrix = algSheme;
        qubitsNumber = qubitsInRegister;
        size = (int) Math.pow(2, qubitsNumber);
    }
}
