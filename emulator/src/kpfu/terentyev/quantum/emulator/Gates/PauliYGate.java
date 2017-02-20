package kpfu.terentyev.quantum.emulator.Gates;

import kpfu.terentyev.quantum.emulator.QuantumGate;
import kpfu.terentyev.quantum.emulator.Complex;

/**
 * Created by alexandrterentyev on 07.04.15.
 */
public class PauliYGate extends QuantumGate {
    public PauliYGate (){
        this.qubitsNumber=1;
        this.size=2;
    }
    @Override
    public Complex[][] getMatrix() {
        Complex result [][] = {
                {Complex.zero(),new Complex(0, -1)},
                {new Complex(0,1),Complex.zero()}
        };
        return result;
    }
}
