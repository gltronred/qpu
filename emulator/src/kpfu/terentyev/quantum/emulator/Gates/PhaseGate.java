package kpfu.terentyev.quantum.emulator.Gates;

import kpfu.terentyev.quantum.emulator.Complex;
import kpfu.terentyev.quantum.emulator.QuantumGate;

/**
 * Created by aleksandrterentev on 08.03.16.
 */
public class PhaseGate extends QuantumGate {

    private double thetaPhaseInRadians;

    public PhaseGate(double thetaPhaseInRadians){
        qubitsNumber = 1;
        size = 2;
        this.thetaPhaseInRadians = thetaPhaseInRadians;
    }

    @Override
    public Complex[][] getMatrix() throws Exception {
        Complex [][] result = {
                {new Complex(Math.cos(-thetaPhaseInRadians/2.0), Math.sin(-thetaPhaseInRadians/2.0)), Complex.zero()},
                {Complex.zero(), new Complex(Math.cos(thetaPhaseInRadians/2.0), Math.sin(thetaPhaseInRadians/2.0))},
        };
        return result;
    }
}
