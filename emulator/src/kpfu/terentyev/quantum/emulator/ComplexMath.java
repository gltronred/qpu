package kpfu.terentyev.quantum.emulator;

/**
 * Created by alexandrterentyev on 25.03.15.
 */
public class ComplexMath {
    public static Complex[][] tensorMultiplication (Complex [][] firstMatrix, int firstMatrixHeight,
            int firstMatrixWidth, Complex[][] secondMatrix,int secondMatrixHeight, int secondMatrixWidth){
        Complex [][] result = new Complex[firstMatrixHeight*secondMatrixHeight][firstMatrixWidth*secondMatrixWidth];
        for (int iFirst = 0; iFirst< firstMatrixHeight; iFirst++){
            for (int jFirst=0; jFirst < firstMatrixWidth; jFirst++){
                for (int iSecond=0; iSecond<secondMatrixHeight; iSecond++){
                    for (int jSecond=0; jSecond<secondMatrixWidth; jSecond++){
                        result [iFirst*secondMatrixHeight+iSecond][jFirst*secondMatrixWidth+jSecond] =
                                Complex.mult(firstMatrix[iFirst][jFirst],secondMatrix[iSecond][jSecond]);
                    }
                }
            }
        }
        return result;
    }

    public static Complex[][] multiplication (Complex a, Complex [][] matrix, int size){
        Complex [][] result = new Complex[size][size];
        for (int i=0; i<size;i++) {
            for (int j=0; j<size; j++) {
                result [i][j] = Complex.mult(a, matrix[i][j]);
            }
        }
        return result;
    }

    public static Complex[] multiplication (Complex [][] matrix, int size, Complex[] vector){
        Complex [] result = new Complex[size];
        for (int i=0; i<size;i++) {
            Complex sum = Complex.zero();
            for (int j=0; j<size; j++) {
                sum = Complex.add(sum, Complex.mult(matrix[i][j], vector[j]));
            }
            result[i]=sum;
        }
        return result;
    }

    public static Complex[] tensorMultiplication(Complex[] a, Complex[] b){
        Complex [] result = new Complex[a.length*b.length];
        for (int i=0; i< a.length; i++){
            for (int j=0; j<b.length; j++){
                result[i*b.length+j] = Complex.mult(a[i], b[j]);
            }
        }
        return result;
    }

    public static Complex[][] ketBraTensorMultiplication (Complex[] ket, Complex[] bra){
        Complex [][] result = new Complex[ket.length][bra.length];

        for (int i = 0; i < ket.length; i++){
            for (int j = 0; j < bra.length; j++){
                result [i][j] = Complex.mult(ket[i], bra[j]);
            }
        }

        return result;
    }

    public  static Complex[][] multiplication(Complex[][] matrixA, int heightA, int widthA,
                                              Complex [][] matrixB, int heightB, int widthB){
        Complex [][]result = new Complex[heightA][widthB];
        for (int i=0; i<heightA; i++){
            for (int j=0; j<widthB; j++){
                Complex sum = Complex.zero();
                for (int z=0; z<widthA; z++){
                    sum = Complex.add(sum, Complex.mult(matrixA[i][z],matrixB[z][j]));
                }
                result [i][j]=sum;
            }
        }
        return result;
    }

    public static Complex [][] zeroMatrix (int height, int width){
        Complex [] [] result = new Complex [height][width];
        for (int i=0 ; i<height; i++){
            for (int j = 0; j<width; j++){
                result [i][j] = Complex.unit().zero();
            }
        }
        return result;
    }

    public  static Complex[][] squareMatricesMultiplication (Complex[][] matrixA, Complex [][] matrixB, int size){
        Complex [][]result = new Complex[size][size];
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                Complex sum = Complex.zero();
                for (int z=0; z<size; z++){
                    sum = Complex.add(sum, Complex.mult(matrixA[i][z],matrixB[z][j]));
                }
                result [i][j]=sum;
            }
        }
        return result;
    }
    public static  Complex[][] hermitianTransposeForMatrix (Complex[][] matrix, int height, int width){
        Complex [][] result = new Complex[width][height];
        for (int i=0; i<height; i++)
            for (int j=0; j<width; j++)
                result[j][i] = matrix[i][j].conjugate();
        return result;
    }

    public static Complex trace (Complex[][] matrix, int size){
        Complex result = Complex.zero();
        for (int i=0; i < size; i++){
            result = Complex.add(result, matrix[i][i]);
        }

        return result;
    }
}
