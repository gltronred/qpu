package kpfu.terentyev.quantum.emulator;

/**
 * Created by alexandrterentyev on 25.02.15.
 */

public abstract class QuantumGate {
    protected int qubitsNumber;
    protected int size;


    @Override
    public String toString(){
        Complex [][] matrix = new Complex[0][];
        try {
            matrix = this.getMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = "";
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                result= result + matrix[i][j]+" ";
            }
            result = result + "\n";
        }
        return result;
    }

    public abstract Complex [][] getMatrix () throws Exception;


    //Gate matrices
    public static Complex [][] identityGateMatrix(){
        Complex result [][] = {
                {Complex.unit(),Complex.zero()},
                {Complex.zero(),Complex.unit()}
        };
        return result;
    }
    public static Complex [][] hadamardGateMatrix (){
        Complex result [][] = {
                {new Complex(1/Math.sqrt(2), 0),new Complex(1/Math.sqrt(2),0)},
                {new Complex(1/Math.sqrt(2), 0),new Complex(-1/Math.sqrt(2),0)}
        };
        return result;
    }
    public static Complex [][] pauliXGateMatrix (){
        Complex result [][] = {
                {Complex.zero(),Complex.unit()},
                {Complex.unit(),Complex.zero()}
        };
        return result;
    }
    public static Complex [][] pauliYGateMatrix (){
        Complex result [][] = {
                {Complex.zero(),new Complex(0, -1)},
                {new Complex(0,1),Complex.zero()}
        };
        return result;
    }
    public static Complex [][] pauliZGateMatrix (){
        Complex result [][] = {
                {new Complex(0,1),Complex.zero()},
                {Complex.zero(),new Complex(0, -1)}
        };
        return result;
    }
    public static Complex [][] swapGateMatrix(){
        Complex result [][] = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero()},
                {Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()}
        };
        return result;
    }
    public static Complex [][] controlledNOTGateMatrix(){
        Complex result [][] = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()},
                {Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero()}
        };
        return result;
    }
    public static Complex [][] controlledUGateMatrix(Complex[][] uMatrix) throws Exception {
        if (uMatrix.length!=2 || (uMatrix.length==2 && (uMatrix[0].length!=2 || uMatrix[1].length!=2))){
            throw new Exception();
        }
        Complex result [][] = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), uMatrix[0][0], uMatrix[0][1]},
                {Complex.zero(), Complex.zero(), uMatrix[1][0], uMatrix[1][1]},
        };
        return result;
    }
    public static Complex [][] toffoliGateMatrix(){
        Complex result [][] = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero()}
        };
        return result;
    }
    public static Complex [][] fredkinGateMatrix(){
        Complex result [][] = {
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero()},
                {Complex.unit(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.unit(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.unit(), Complex.zero(), Complex.zero()},
                {Complex.zero(), Complex.zero(), Complex.zero(), Complex.zero(),
                        Complex.zero(), Complex.zero(), Complex.zero(), Complex.unit()}
        };
        return result;
    }
}
