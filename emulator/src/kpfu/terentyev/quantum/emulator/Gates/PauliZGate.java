package kpfu.terentyev.quantum.emulator.Gates;

import kpfu.terentyev.quantum.emulator.Complex;
import kpfu.terentyev.quantum.emulator.QuantumGate;

/**
 * Created by alexandrterentyev on 07.04.15.
 */
public class PauliZGate extends QuantumGate {
    public PauliZGate (){
        this.qubitsNumber=1;
        this.size=2;
    }
    @Override
    public Complex[][] getMatrix() {
        Complex result [][] = {
                {new Complex(0,1),Complex.zero()},
                {Complex.zero(),new Complex(0, -1)}
        };
        return result;
    }
}
