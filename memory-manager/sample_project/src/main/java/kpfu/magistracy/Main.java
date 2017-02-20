package kpfu.magistracy;

import com.google.gson.GsonBuilder;
import kpfu.magistracy.controller.QuantumMemoryOperator;
import kpfu.magistracy.controller.execution.commands.CommandTypes;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.service_for_controller.OwnerData;
import kpfu.magistracy.service_for_controller.ServiceManager;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressFromClient;
import kpfu.magistracy.service_for_controller.commands.CommandsFromClientDTO;
import kpfu.magistracy.service_for_controller.commands.LogicalAddressingCommandFromClient;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //testController();
        testWholeCycle();
    }

    private static void testController() {
        OwnerData ownerData1 = new OwnerData("1", System.currentTimeMillis());
        OwnerData ownerData2 = new OwnerData("2", System.currentTimeMillis());

        LogicalQubitAddressForController logicalQubitAddressForController1 = new LogicalQubitAddressForController(1, 0);
        LogicalQubitAddressForController logicalQubitAddressForController2 = new LogicalQubitAddressForController(1, 1);
        LogicalAddressingCommand.Builder builder = new LogicalAddressingCommand.Builder();
        builder.setCommand(CommandTypes.PHASE)
                .setCommandParam(0.9)
                .setFirstQubit_Part1(logicalQubitAddressForController1)
                .setFirstQubit_Part2(logicalQubitAddressForController2);

        List<LogicalAddressingCommand> logicalAddressingCommandList = new ArrayList<>();
        logicalAddressingCommandList.add(builder.build());

        Map<OwnerData, List<LogicalAddressingCommand>> mapWithCommands = new HashMap<>();
        mapWithCommands.put(ownerData1, logicalAddressingCommandList);

        QuantumMemoryOperator quantumMemoryOperator = QuantumMemoryOperator.getOperator();
        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> mapWithResults = quantumMemoryOperator.executeCommands(mapWithCommands);

        for (Map.Entry<OwnerData, Map<LogicalQubitAddressForController, Boolean>> mapEntry : mapWithResults.entrySet()) {
            for (Map.Entry<LogicalQubitAddressForController, Boolean> innerEntry : mapEntry.getValue().entrySet()) {
                System.out.println("OwnerData = " + mapEntry.getKey() + ", LogicalAddress = " + innerEntry.getKey() + ", Value = " + innerEntry.getValue());
            }
        }
    }

    private static void testWholeCycle() {
        ServiceManager serviceManager = ServiceManager.getServiceManager();
        CommandsFromClientDTO commandsFromClientDTO = new CommandsFromClientDTO();
        commandsFromClientDTO.setQubitCount(2);
        List<LogicalAddressingCommandFromClient> commandFromClientList = new LinkedList<>();
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI,
                new LogicalQubitAddressFromClient(1))
        );
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.QET,
                Math.PI / 2,
                new LogicalQubitAddressFromClient(2))
        );
        commandsFromClientDTO.setLogicalAddressingCommandFromClientList(commandFromClientList);
        serviceManager.putCommandsToExecutionQueue("1", new GsonBuilder().create().toJson(commandsFromClientDTO));
        commandsFromClientDTO = new CommandsFromClientDTO();
        commandsFromClientDTO.setQubitCount(1);
        commandFromClientList = new LinkedList<>();
        commandFromClientList.add(new LogicalAddressingCommandFromClient(
                CommandTypes.PHASE,
                Math.PI,
                new LogicalQubitAddressFromClient(1))
        );
        commandsFromClientDTO.setLogicalAddressingCommandFromClientList(commandFromClientList);
        serviceManager.putCommandsToExecutionQueue("2", new GsonBuilder().create().toJson(commandsFromClientDTO));
    }
}
