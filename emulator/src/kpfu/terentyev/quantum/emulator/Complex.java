package kpfu.terentyev.quantum.emulator;

/**
 * Created by alexandrterentyev on 09.03.15.
 */
public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex sqr(){
        return new Complex(real*real - imaginary*imaginary, -2 * real * imaginary);
    }

    public double mod(){
        return Math.sqrt(norma());
    }

    public double norma(){
        return real*real + imaginary*imaginary;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    @Override
    public String toString(){
        String result = "";
        if (real!=0.0)
            result = result + this.real;
        if (imaginary<0){
            result= result+imaginary+"i";
        }else if (imaginary>0){
            result=result+"+"+imaginary+"i";
        }else if (imaginary==0.0){
            if (real==0.0){
                result="0.0";
            }
        }
        return result;
    }

    public static Complex add(Complex one, Complex two) {
        return new Complex(one.getReal() + two.getReal(),
                one.getImaginary() + two.getImaginary());
    }

    public static Complex sub(Complex one, Complex two) {
        return new Complex(one.getReal() - two.getReal(),
                one.getImaginary() - two.getImaginary());
    }

    public static Complex devide (Complex one, Complex two){
        return new Complex(
                (one.real*two.real + one.imaginary+two.imaginary)/
                (two.real*two.real +two.imaginary*two.imaginary),

                (two.real*one.imaginary-two.imaginary*one.real)/
                        (two.real*two.real +two.imaginary*two.imaginary)
                );
    }

    public static Complex mult (Complex one, Complex two){
        return new Complex(
                one.getReal()*two.getReal()-one.getImaginary()*two.getImaginary(),

                one.getReal()*two.getImaginary()+one.getImaginary()*two.getReal());
    }

    public Complex conjugate (){
        return new Complex(real, -imaginary);
    }

    public static Complex zero (){
        return new Complex (0.0,0.0);
    }
    public static Complex unit (){
        return new Complex (1.0, 0.0);
    }
}
