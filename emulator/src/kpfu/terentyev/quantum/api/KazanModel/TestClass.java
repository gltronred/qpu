package kpfu.terentyev.quantum.api.KazanModel;

import kpfu.terentyev.quantum.api.QuantumManager;
import kpfu.terentyev.quantum.emulator.Complex;

/**
 * Created by aleksandrterentev on 17.04.16.
 */
public class TestClass {
    public static void testKazanModelEmulator(){


//        QVM initialization

        double MAX_MEMORY_FREQUENCY = 200, MIN_MEMORY_FREQUENCY = 50, MEMORY_TIME_CYCLE = 50;
        int PROCCESSING_UNITS_COUNT = 3;

        Emulator QVM = new Emulator(MAX_MEMORY_FREQUENCY, MIN_MEMORY_FREQUENCY, MEMORY_TIME_CYCLE, PROCCESSING_UNITS_COUNT);


//        Qubits initialization


        double logicalQubit1Freq = 60, logicalQubit1TimeDelay = 1;
        QuantumMemoryAddress q1Address = new QuantumMemoryAddress(logicalQubit1Freq, logicalQubit1TimeDelay,
                MemoryHalf.HALF_0);
        QuantumMemoryAddress q2Address = new QuantumMemoryAddress(logicalQubit1Freq, logicalQubit1TimeDelay,
                MemoryHalf.HALF_1);
        QVM.initLogicalQubit(q1Address, q2Address);

//        Transistor addresses
        int currentTransisotorIndex = 0;
        ProcessingAddress transistor0_0 = new ProcessingAddress(currentTransisotorIndex, ProcessingUnitCellAddress.Cell0);
        ProcessingAddress transistor0_1 = new ProcessingAddress(currentTransisotorIndex, ProcessingUnitCellAddress.Cell1);
        ProcessingAddress transistor0_c = new ProcessingAddress(currentTransisotorIndex, ProcessingUnitCellAddress.ControlPoint);



//        Transitions
        QVM.load(q1Address, transistor0_0);
        QVM.load(q2Address, transistor0_1);
        QVM.QET(currentTransisotorIndex, Math.PI / 3.0);
        QVM.save(transistor0_0, q1Address);
        QVM.save(transistor0_1, q2Address);




        System.out.print("q1: " + QVM.measure(q1Address) + "\n");
        System.out.print("q2: " + QVM.measure(q2Address) + "\n");


        System.out.print("End testing");
    }

    static void testQuantumManager(){


        try {
            QuantumManager manager = new QuantumManager();
            QuantumManager.Qubit qubit1 = manager.initNewQubit(Complex.zero(), Complex.unit());
            QuantumManager.Qubit qubit2 = manager.initNewQubit();

            Complex u = Complex.unit();
            Complex z = Complex.zero();

            Complex[][] U = {
                    {u, z, z, z},
                    {z, u, z, z},
                    {z, z, u, z},
                    {z, z, z, u},
            };
            manager.performTransitionForQubits(U, qubit1, qubit2);
           
            System.out.print("Qubit 1:" + manager.measure(qubit1) + "\n");
            System.out.print("Qubit 2:" + manager.measure(qubit2) + "\n");
        } catch (Exception e) {

        }


    }



    public static void main(String[] args) {
        testQuantumManager();
    }
}
