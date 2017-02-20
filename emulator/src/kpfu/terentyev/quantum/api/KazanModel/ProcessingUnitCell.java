package kpfu.terentyev.quantum.api.KazanModel;

import kpfu.terentyev.quantum.api.QuantumManager;
import kpfu.terentyev.quantum.api.QuantumManager.Qubit;

/**
 * Created by aleksandrterentev on 29.03.16.
 */
public class ProcessingUnitCell {
    private Qubit qubit;

    void loadQubit (Qubit qubit){
        this.qubit = qubit;
    }

    Qubit unloadQubit (){
        Qubit result = qubit;
        qubit = null;
        return result;
    }

    Qubit getQubit() {
        return qubit;
    }
}
