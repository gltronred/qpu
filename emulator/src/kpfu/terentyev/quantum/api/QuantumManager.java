package kpfu.terentyev.quantum.api;


import kpfu.terentyev.quantum.emulator.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by aleksandrterentev on 24.01.16.
 */

public class QuantumManager {

    public static class Qubit {
        // Можно было сделать его частным случаем регистра, но пока удобнее хранить идентификаторы регистров и их номер в регистре
        String registerAddress;
        int addressInRegister;
        Qubit (String registerAddress, int addressInRegister){
            this.registerAddress = registerAddress;
            this.addressInRegister = addressInRegister;
        }
    }

    protected static class RegisterInfo{
        public QuantumRegister register;
        public ArrayList <Qubit> qubits;

        RegisterInfo(ArrayList<Qubit> qubits, QuantumRegister register) {
            this.qubits = qubits;
            this.register = register;
        }
    }

    // This class must contain quantum registeres
    protected HashMap <String, RegisterInfo> registers = new HashMap<String, RegisterInfo>();

    protected static final String qubitDestroyedRegisterAddress = "Qubit destroyed";

//    Qubit creation
    public Qubit initNewQubit() throws Exception {
        return initNewQubit (Complex.unit(), Complex.zero());
    }

    public Qubit initNewQubit(Complex alpha, Complex beta) throws Exception {
        QuantumRegister newRegister = new QuantumRegister(1, new Complex[]{alpha, beta});
        String registerID = Double.toString(new Date().getTime() + new Random().nextDouble());
        Qubit newQubit = new Qubit(registerID, 0);
        ArrayList<Qubit> qubits = new ArrayList<Qubit>();
        qubits.add(newQubit);
        registers.put(registerID, new RegisterInfo(qubits, newRegister));
        return newQubit;
    }


    //Service functions
    protected RegisterInfo checkAndMergeRegistersIfNeedForQubits (Qubit... qubits) throws Exception {
        ArrayList<String> usedRegisterAddresses = new ArrayList<String>();
        for (Qubit qubit: qubits) {
            if (!usedRegisterAddresses.contains(qubit.registerAddress)){
                usedRegisterAddresses.add(qubit.registerAddress);
            }
        }

        if (usedRegisterAddresses.size()==1){
            return registers.get(usedRegisterAddresses.get(0));
        }

        //       Create new register merged registers
        Complex[][] newRegisterConfiguration = {{Complex.unit()}};
        ArrayList <Qubit> newRegisterQubits = new ArrayList<Qubit>();
        String newRegisterAddress = Double.toString(new Date().getTime());

        for (String registerAddress: usedRegisterAddresses) {
            RegisterInfo currentRegisterInfo = registers.get(registerAddress);
            int tempSize = newRegisterConfiguration.length;
            int currentSize = currentRegisterInfo.register.getDensityMatrix().length;
            newRegisterConfiguration = ComplexMath.tensorMultiplication(newRegisterConfiguration, tempSize, tempSize, currentRegisterInfo.register.getDensityMatrix(), currentSize, currentSize);
            for (Qubit qubit: currentRegisterInfo.qubits){
                newRegisterQubits.add(qubit);
                qubit.registerAddress = newRegisterAddress;
                qubit.addressInRegister = newRegisterQubits.size()-1;
            }
            registers.remove(registerAddress);
        }

        QuantumRegister newRegister = new QuantumRegister(newRegisterQubits.size(), newRegisterConfiguration);

        RegisterInfo newRegisterInfo =   new RegisterInfo(newRegisterQubits, newRegister);
        registers.put(newRegisterAddress, newRegisterInfo);

        return newRegisterInfo;
    }

    protected int qubitAddressInRegister(Qubit q){
        return q.addressInRegister;
    }

    protected void performTransitionForQubits (Complex[][] transitionMatrix,
                                     RegisterInfo mergedRegisterInfo, Qubit ... qubits) throws Exception {
        ArrayList<Integer> qubitIndexes = new ArrayList<Integer>();
        for (Qubit q: qubits
             ) {
            qubitIndexes.add(q.addressInRegister);
        }

        OneStepAlgorythm alg = new OneStepAlgorythm(mergedRegisterInfo.qubits.size(),
                qubitIndexes, transitionMatrix);
        mergedRegisterInfo.register.performAlgorythm(alg);
    }

    public void performTransitionForQubits (Complex[][] transitionMatrix, Qubit ... qubits) throws Exception {
        RegisterInfo info = checkAndMergeRegistersIfNeedForQubits(qubits);
        performTransitionForQubits(transitionMatrix, info, qubits);
    }

    // Operations
    public int measure (Qubit qubit) throws Exception {
        RegisterInfo regInfo = registers.get(qubit.registerAddress);
        int result = regInfo.register.measureQubit(qubit.addressInRegister);
//        int qubitPosition = regInfo.qubits.indexOf(qubit);
//        regInfo.qubits.remove(qubitPosition);
//        for (int i=qubitPosition; i< regInfo.qubits.size(); i++){
//            regInfo.qubits.get(i).addressInRegister --;
//        }
//        qubit.registerAddress = qubitDestroyedRegisterAddress;
//        TODO: remove register if qubits count is 0
        return result;
    }
}
