package kpfu.terentyev.quantum.emulator;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by alexandrterentyev on 09.03.15.
 */

//Algorithm is a matrix. Each cell is the QuantumSchemeStepQubitAttributes
// (Map) gates :{gateID:specifications (Quantum gate)}.
// Generally quantum step and algorithm are quantum gates too.


public class QuantumAlgorithm extends QuantumGate {

    int stepsNumber;
    QuantumSchemeStepQubitAttributes [][] algorithmSchemeMatrix;
    String [] mainGateIDs;
    Map <String, QuantumGate> gates;

    public  QuantumAlgorithm(){
        stepsNumber = 0;
    }

    public QuantumAlgorithm(QuantumSchemeStepQubitAttributes[][] algorithmSchemeMatrix, String[] mainGateIDs,
                            Map<String, QuantumGate> gates) {
        this.algorithmSchemeMatrix = algorithmSchemeMatrix;
        this.gates = gates;
        this.mainGateIDs = mainGateIDs;
        qubitsNumber = algorithmSchemeMatrix.length;
        stepsNumber= algorithmSchemeMatrix[0].length;
        size = (int) Math.pow(2, qubitsNumber);
    }

    Complex[][] generateStepMatrix(int step) throws Exception {
        int mainGateIndexesSum = 0;
        int count=0;
        String mainGateID = mainGateIDs[step];
        ArrayList<Number> mainGateQubits = new ArrayList<Number>();
        for (int qubitNum=0; qubitNum<qubitsNumber; qubitNum++){//loop for each qubit
            QuantumSchemeStepQubitAttributes qubitParams = algorithmSchemeMatrix[qubitNum][step];
            if (qubitParams.gateID.equals(mainGateID)){
                mainGateIndexesSum+=qubitNum;
                count++;
                mainGateQubits.add(qubitNum);
            }
        }
        Complex [][] gateMatrix = gates.get(mainGateID).getMatrix();
        Complex [][] result = {{Complex.unit()}};
        if (checkAdjustment(mainGateQubits)){
            Complex centralMatr[][] = {{Complex.unit()}};
            //if qubits is near to each other just multiply identity gates and mainGate matrices (tensors)
            for (int currentQubit=0; currentQubit<qubitsNumber;){//loop for each qubit
                Number ind = currentQubit;
                QuantumSchemeStepQubitAttributes qubitParams = algorithmSchemeMatrix[currentQubit][step];
                if (qubitParams.gateID.equals(mainGateID)){
                    while ( currentQubit<qubitsNumber &&
                            algorithmSchemeMatrix[currentQubit][step].gateID.equals(mainGateID)){
                        currentQubit++;
                        ind = currentQubit;
                    }
                    centralMatr = ComplexMath.tensorMultiplication(centralMatr,centralMatr.length,centralMatr.length,
                            gateMatrix, gateMatrix.length, gateMatrix.length);
                }else if (qubitParams.gateID.equals(QuantumSchemeStepQubitAttributes.IdentityGateID)){
                    Complex [][] gateMatrx = QuantumGate.identityGateMatrix();
                    centralMatr =ComplexMath.tensorMultiplication(centralMatr,centralMatr.length,centralMatr.length,
                            gateMatrx, gateMatrx.length, gateMatrx.length);
                    currentQubit++;
                }else {
                    throw new Exception("Two non trivial gates at step!");
                }
            }

            //Move controlled qubit to bottom if need
            int controlledQubitIndex = -1;
            for (int i=0; i<mainGateQubits.size()-1; i++){
                if (algorithmSchemeMatrix[mainGateQubits.get(i).intValue()][step].controlled)
                    controlledQubitIndex=i;
            }

            ArrayList <Complex[][]> swapMatrices = new ArrayList<Complex[][]>();
            
            if (controlledQubitIndex!=-1) {
                Complex[][] swapGateMatrix = QuantumGate.swapGateMatrix();
                Complex [][] identityMatrx = QuantumGate.identityGateMatrix();
                int lowerQubitIndex = mainGateQubits.get(mainGateQubits.size()-1).intValue();
                for (; controlledQubitIndex<lowerQubitIndex; controlledQubitIndex++){
                    Complex currentSwap[][] = {{Complex.unit()}};
                    for (int i=0; i<qubitsNumber;){
                        if (i==controlledQubitIndex){
                            currentSwap = ComplexMath.tensorMultiplication(currentSwap,
                                    currentSwap.length, currentSwap.length,
                                    swapGateMatrix, swapGateMatrix.length, swapGateMatrix.length);
                            i+=2;
                        }else {
                            currentSwap = ComplexMath.tensorMultiplication(currentSwap, currentSwap.length,
                                    currentSwap.length, identityMatrx,
                                    identityMatrx.length, identityMatrx.length);
                        }
                    }
                    swapMatrices.add(currentSwap);
                }
                result=swapMatrices.get(0).clone();
                for (int i=1 ; i<swapMatrices.size(); i++){
                    result=ComplexMath.squareMatricesMultiplication(result, swapMatrices.get(i), result.length);
                }
                result=ComplexMath.squareMatricesMultiplication(result, centralMatr, result.length);
                for (int i=swapMatrices.size()-1 ; i>=0; i--){
                    result=ComplexMath.squareMatricesMultiplication(result, swapMatrices.get(i), result.length);
                }
            }else {
                result=centralMatr;
            }

        }else{//Group one gate qubits
            //check Type of gravityCenter (maybe needs double)
            int gravityCenter=0;
            if (count>0){
                gravityCenter = mainGateIndexesSum/count;
            }
            int upperQubit, lowerQubit;
            int levelNumber = mainGateQubits.size()/2;
//            ArrayList <Complex[][]> swapMatrices = new ArrayList<Complex[][]>();
            Complex[][] centralMatrix = {{Complex.unit()}}; //matrix perfomed main gate when all qubits are near
            Complex[][] swapGateMatrix = QuantumGate.swapGateMatrix();
            Complex [][] identityMatrx = QuantumGate.identityGateMatrix();

            Complex[][] swapMatrix = null;

            for (int level=0; level< levelNumber; level++){
                //find upper and lower qubits. Upper index is less than lower index
                upperQubit=-1; lowerQubit=-1; //empty
                int upperPlace = gravityCenter-level;
                int lowerPlace = gravityCenter+1+level;
                int upperIndex = upperPlace;
                int lowerIndex = lowerPlace;


                int distance;
                for (; upperIndex>=0; upperIndex--){
                    QuantumSchemeStepQubitAttributes upperQubitParams = algorithmSchemeMatrix[upperIndex][step];
                    if (upperQubit==-1 && upperQubitParams.gateID.equals(mainGateID)){
                        upperQubit=upperIndex;
                        break;
                    }
                }

                for (; lowerIndex<qubitsNumber; lowerIndex++){
                    QuantumSchemeStepQubitAttributes lowerQubitParams = algorithmSchemeMatrix[lowerIndex][step];
                    if (lowerQubit == -1 && lowerQubitParams.gateID.equals(mainGateID)){
                        lowerQubit = lowerIndex;
                        break;
                    }
                }
                distance = Math.max(upperPlace-upperQubit, lowerQubit-lowerPlace);

                //move qubits to gravity center + level
                for (; distance>level; distance--){
                    //form swap matrix
                    Complex currentDistanceSwap[][] = {{Complex.unit()}};
                    for (int i=0; i<qubitsNumber; ){
                        if ((i==upperQubit && upperQubit==upperPlace-distance) ||
                                (i==lowerQubit-1 && lowerQubit==lowerPlace+distance)){
                            //need to swap upper gate
                            currentDistanceSwap = ComplexMath.tensorMultiplication(currentDistanceSwap,
                                    currentDistanceSwap.length, currentDistanceSwap.length,
                                    swapGateMatrix,
                                    swapGateMatrix.length, swapGateMatrix.length);
                            i+=2;
                        }else {
                            currentDistanceSwap = ComplexMath.tensorMultiplication(currentDistanceSwap, currentDistanceSwap.length,
                                    currentDistanceSwap.length, identityMatrx,
                                    identityMatrx.length, identityMatrx.length);
                            i++;
                        }
                    }
//                    swapMatrices.add(currentDistanceSwap);
                    if (swapMatrix == null){
                        swapMatrix = currentDistanceSwap.clone();
                    }else {
                        swapMatrix = ComplexMath.squareMatricesMultiplication(currentDistanceSwap, swapMatrix,
                                swapMatrix.length);
                    }
                }

            }

            //Move controlled qubit to bottom if need
            int controlledQubitIndex = -1;
            for (int i=0; i<mainGateQubits.size()-1; i++){
                if (algorithmSchemeMatrix[mainGateQubits.get(i).intValue()][step].controlled)
                    controlledQubitIndex=i;
            }
            if (controlledQubitIndex!=-1) {
                //project to current positions
                controlledQubitIndex = gravityCenter - (levelNumber - 1) + controlledQubitIndex;
                for (; controlledQubitIndex< gravityCenter+levelNumber; controlledQubitIndex++){
                    Complex currentSwap[][] = {{Complex.unit()}};
                    for (int i=0; i<qubitsNumber;){
                        if (i==controlledQubitIndex){
                            currentSwap = ComplexMath.tensorMultiplication(currentSwap,
                                    currentSwap.length, currentSwap.length,
                                    swapGateMatrix, swapGateMatrix.length, swapGateMatrix.length);
                            i+=2;
                        }else {
                            currentSwap = ComplexMath.tensorMultiplication(currentSwap, currentSwap.length,
                                    currentSwap.length, identityMatrx,
                                    identityMatrx.length, identityMatrx.length);
                        }
                    }
//                    swapMatrices.add(currentSwap);
                    swapMatrix = ComplexMath.squareMatricesMultiplication(currentSwap, swapMatrix, swapMatrix.length);
                }
            }
            //form central matrix after swaps
            for (int i=0; i<qubitsNumber;){
                if (i==gravityCenter-levelNumber/2){
                    centralMatrix = ComplexMath.tensorMultiplication(centralMatrix, centralMatrix.length, centralMatrix.length,
                            gateMatrix, gateMatrix.length, gateMatrix.length);
                    i+=mainGateQubits.size();
                }else {
                    centralMatrix = ComplexMath.tensorMultiplication(centralMatrix, centralMatrix.length, centralMatrix.length,
                            identityMatrx, identityMatrx.length, identityMatrx.length);
                    i++;
                }
            }
            Complex [][] swapConjugateMatrix = ComplexMath.hermitianTransposeForMatrix(swapMatrix,
                    swapMatrix.length, swapMatrix.length);
            result = ComplexMath.squareMatricesMultiplication(centralMatrix, swapMatrix,swapMatrix.length);
            result = ComplexMath.squareMatricesMultiplication(swapConjugateMatrix, result, swapMatrix.length);

            //form common matrix, using matrix associative property, mult all matrices
//            result=swapMatrices.get(0).clone();
//            for (int i=1 ; i<swapMatrices.size(); i++){
//                result=ComplexMath.squareMatricesMultiplication(result, swapMatrices.get(i), result.length);
//            }
//            result=ComplexMath.squareMatricesMultiplication(result, centralMatrix, result.length);
//            for (int i=swapMatrices.size()-1 ; i>=0; i--){
//                result=ComplexMath.squareMatricesMultiplication(result, swapMatrices.get(i), result.length);
//            }
        }
        return result;
    }

    boolean checkAdjustment (ArrayList<Number> listToCheck){
        for (int i=1; i<listToCheck.size(); i++){
            if (listToCheck.get(i).intValue()>listToCheck.get(i-1).intValue()+1){
                return false;
            }
        }
        return true;
    }

    @Override
    public Complex[][] getMatrix() throws Exception {
        Complex [][]result = generateStepMatrix(stepsNumber - 1);
        for (int i=stepsNumber-2; i>=0; i--){
            result=ComplexMath.squareMatricesMultiplication(result, generateStepMatrix(i), result.length);
        }
        return result;
    }
}