package kpfu.terentyev.quantum.api.KazanModel;

import kpfu.terentyev.quantum.api.QuantumManager;
import kpfu.terentyev.quantum.emulator.Complex;
import kpfu.terentyev.quantum.emulator.OneStepOneQubitGateAlgorythm;

/**
 * Created by aleksandrterentev on 29.03.16.
 */
public class QuantumProccessorHelper extends QuantumManager {
    void physicalQET (Qubit a, Qubit b, double thetaPhaseInRadians) throws Exception {
        RegisterInfo regInfo = checkAndMergeRegistersIfNeedForQubits(a, b);
        Complex[][] matrix = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), new Complex(Math.cos(thetaPhaseInRadians/2), 0), new Complex(0, Math.sin(thetaPhaseInRadians/2)), Complex.zero()},
                {Complex.zero(), new Complex(0, Math.sin(thetaPhaseInRadians/2)), new Complex(Math.cos(thetaPhaseInRadians/2), 0), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()}
        };
        performTransitionForQubits(matrix, regInfo, a, b);
    }
    void physicalCQET (Qubit a, Qubit control, Qubit b, double thetaInRadians) throws Exception {
        RegisterInfo regInfo = checkAndMergeRegistersIfNeedForQubits(a, control, b);
        int minAddressOfAB  = Math.min(qubitAddressInRegister(a), qubitAddressInRegister(b));
        int maxAddressOfAB  = Math.max(qubitAddressInRegister(a), qubitAddressInRegister(b));
        Complex z = Complex.zero();
        Complex u = Complex.unit();
        Complex c =  new Complex(Math.cos(thetaInRadians/2), 0);
        Complex is = new Complex(0, Math.sin(thetaInRadians/2));

        if (qubitAddressInRegister(control) < qubitAddressInRegister(a) &&
                qubitAddressInRegister(control) < qubitAddressInRegister(b)) {
            //control qubit is first
            Complex[][] matrix  = {
                    {u, z, z, z, z,z,z,z},
                    {z, c, is, z, z,z,z,z},
                    {z, is, c, z, z,z,z,z},
                    {z, z, z, u, z,z,z,z},

                    {z,z,z,z, u, z, z, z},
                    {z,z,z,z, z, u, z, z},
                    {z,z,z,z, z, z, u, z},
                    {z,z,z,z, z, z, z, u}
            };
            performTransitionForQubits(matrix,
                    regInfo, a, b, control);
        }else if (qubitAddressInRegister(control) > minAddressOfAB &&
                qubitAddressInRegister(control) < maxAddressOfAB){
//            //control qubit is between a and b
            Complex[][] matrix  = {
                    {z,z, u, z, z, z, z,z},
                    {z,z, z, c, is, z,z,z},

                    { u, z, z,z,z,z, z, z},
                    { z, u, z,z,z,z, z, z},

                    {z,z, z, is, c, z, z,z},
                    {z,z, z, z, z, u, z,z},

                    {z, z, z,z,z,z, u, z},
                    {z, z, z,z,z,z, z, u}
            };
            performTransitionForQubits(matrix,
                    regInfo, a, b, control);
        }else{
            //control qubit is last
            Complex[][] matrix  = {
                    {z,z,z,z, u, z, z, z},
                    {u, z, z, z, z,z,z,z},

                    {z,z,z,z, z, c, is, z},
                    { z, u, z, z, z,z,z,z,},

                    {z,z,z,z, z, is, c, z},
                    {z, z, u, z, z,z,z,z},

                    {z,z,z,z, z, z, z, u},
                    {z, z, z, u, z,z,z,z}
            };

            performTransitionForQubits(matrix,
                    regInfo, a, b, control);
        }
    }
    void physicalPHASE (Qubit a, Qubit b, double thetaInRadians) throws Exception {
        RegisterInfo regInfo = checkAndMergeRegistersIfNeedForQubits(a, b);
        Complex[][] matrix = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), new Complex(Math.cos(-thetaInRadians/2.0), Math.sin(-thetaInRadians/2.0)), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), new Complex(Math.cos(thetaInRadians/2.0), Math.sin(thetaInRadians/2.0)), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()}
        };
        performTransitionForQubits(matrix, regInfo, a, b);
    }

    void mergeQubits (Qubit ...qubits) throws Exception {
        checkAndMergeRegistersIfNeedForQubits(qubits);
    }
}
