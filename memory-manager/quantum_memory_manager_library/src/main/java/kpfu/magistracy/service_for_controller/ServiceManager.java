package kpfu.magistracy.service_for_controller;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import kpfu.magistracy.controller.QuantumMemoryOperator;
import kpfu.magistracy.controller.execution.commands.CommandTypes;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressFromClient;
import kpfu.magistracy.service_for_controller.commands.CommandsFromClientDTO;
import kpfu.magistracy.service_for_controller.commands.LogicalAddressingCommandFromClient;
import kpfu.magistracy.service_for_controller.results.TopLevelResult;

import java.util.*;

public class ServiceManager {

    private static final String exceptionStringJsonIsNotValid = "Json is not valid";
    private static final String exceptionStringQubitCount = "This machine does not support creation of such qubit count";

    private static ServiceManager mServiceManager;
    private QuantumMemoryOperator mQuantumMemoryOperator;
    //map for saving all commands list from all users
    private HashMap<OwnerData, List<LogicalAddressingCommand>> mCommandsForControllerMap;
    //map for saving correspondence between qubit address from user and address, which will be send to controller
    private LinkedHashMap<LogicalQubitAddressForController, LogicalQubitAddressFromClient> mAddressesCorrespondenceMap;
    //need to retrieve user's commands list in a right order
    private List<OwnerData> mOwnerDataList;
    //thread, in which will be executed all commands
    private Thread mThreadForCommandsExecuting;

    private ServiceManager() {
        mCommandsForControllerMap = new HashMap<>();
        mAddressesCorrespondenceMap = new LinkedHashMap<>();
        mOwnerDataList = new LinkedList<>();
        mQuantumMemoryOperator = QuantumMemoryOperator.getOperator();
    }

    //call this method to start work with quantum machine
    public static ServiceManager getServiceManager() {
        if (mServiceManager == null) {
            mServiceManager = new ServiceManager();
        }
        return mServiceManager;
    }

    //call this method to execute commands
    public void putCommandsToExecutionQueue(String userId, String commandsString) {
        try {
            OwnerData ownerData = new OwnerData(userId, System.currentTimeMillis());
            List<LogicalAddressingCommand> commandsListForController = checkCommandsAndTransformForController(ownerData, commandsString);
            mCommandsForControllerMap.put(ownerData, commandsListForController);
            mOwnerDataList.add(ownerData);
            executeNextCommand();
        } catch (IllegalArgumentException e) {
            System.out.println("UserId = " + userId + ", error caught: " + e.getMessage());
            //todo сказать пользователю, что json составлен неверно
        }
    }

    /**
     * check every command if it is composed in a right way, decompose logical qubit into two physical qubit
     *
     * @param ownerData      user data, from which commands were sent
     * @param commandsString json with commnads in String
     * @return list in which every command logical qubit decomposed into two physical qubit
     * @throws IllegalArgumentException if even one command composed with error
     */
    private List<LogicalAddressingCommand> checkCommandsAndTransformForController(OwnerData ownerData, String commandsString) throws IllegalArgumentException {
        CommandsFromClientDTO commandsFromClientDTO;
        try {
            commandsFromClientDTO = new Gson().fromJson(commandsString, CommandsFromClientDTO.class);
        } catch (JsonSyntaxException exception) {
            throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
        }
        List<LogicalAddressingCommandFromClient> commandsFromClientList = commandsFromClientDTO.getLogicalAddressingCommandFromClientList();
        //check if memory can execute such count of commands and qubits
        if (commandsFromClientDTO.getQubitCount() > mQuantumMemoryOperator.getQubitsMaxCount() || commandsFromClientList.size() > mQuantumMemoryOperator.getCommandsMaxCount()) {
            throw new IllegalArgumentException(exceptionStringQubitCount);
        }
        List<LogicalAddressingCommand> commandsListForController = new LinkedList<>();
        for (LogicalAddressingCommandFromClient commandFromClient : commandsFromClientList) {
            if (!commandComposedRight(commandFromClient)) {
                throw new IllegalArgumentException(exceptionStringJsonIsNotValid);
            } else {
                //set owner data by myself
                commandFromClient.getQubit_1().setOwnerData(ownerData);
                if (commandFromClient.getQubit_2() != null) {
                    commandFromClient.getQubit_2().setOwnerData(ownerData);
                }
                List<LogicalQubitAddressForController> addressesForController = getAddressesForControllerFromLocalClientAddress(commandFromClient.getQubit_1());
                //fill command for controller with all necessary data
                LogicalAddressingCommand.Builder commandBuilder = new LogicalAddressingCommand.Builder();
                commandBuilder
                        .setCommand(commandFromClient.getCommandType())
                        .setCommandParam(commandFromClient.getCommandParam())
                        .setFirstQubit_Part1(addressesForController.get(0))
                        .setFirstQubit_Part2(addressesForController.get(1));
                if (commandFromClient.getCommandType() == CommandTypes.CQET) {
                    addressesForController = getAddressesForControllerFromLocalClientAddress(commandFromClient.getQubit_2());
                    commandBuilder.setSecondQubit_Part1(addressesForController.get(0));
                    commandBuilder.setSecondQubit_Part2(addressesForController.get(1));
                }
                commandsListForController.add(commandBuilder.build());
            }
        }
        return commandsListForController;
    }

    private boolean commandComposedRight(LogicalAddressingCommandFromClient commandFromClient) {
        if (commandFromClient.getQubit_1() == null || commandFromClient.getCommandParam() == null || commandFromClient.getCommandType() == null) {
            return false;
        } else if (commandFromClient.getCommandType() == CommandTypes.CQET && commandFromClient.getQubit_2() == null) {
            return false;
        }
        return true;
    }

    private List<LogicalQubitAddressForController> getAddressesForControllerFromLocalClientAddress(LogicalQubitAddressFromClient qubitAddressFromClient) {
        List<LogicalQubitAddressForController> addressesForController = getGlobalLogicalQubitAddressFromLocalClient(qubitAddressFromClient);
        if (addressesForController.isEmpty()) {
            //init two physical addresses with the same globalId and different memory part
            Long currentTimeInMillis = System.currentTimeMillis();
            addressesForController.add(new LogicalQubitAddressForController(currentTimeInMillis, 0));
            addressesForController.add(new LogicalQubitAddressForController(currentTimeInMillis, 1));
        }
        mAddressesCorrespondenceMap.put(addressesForController.get(0), qubitAddressFromClient);
        mAddressesCorrespondenceMap.put(addressesForController.get(1), qubitAddressFromClient);
        return addressesForController;
    }

    //look for in the list have already been initialized qubit
    //if not initialized returns empty list, else two physical qubit address for controller
    private List<LogicalQubitAddressForController> getGlobalLogicalQubitAddressFromLocalClient(LogicalQubitAddressFromClient qubitAddressFromClient) {
        List<LogicalQubitAddressForController> logicalQubitAddressForControllers = new LinkedList<>();
        for (LogicalQubitAddressForController key : mAddressesCorrespondenceMap.keySet()) {
            if (mAddressesCorrespondenceMap.get(key).equals(qubitAddressFromClient)) {
                if (key.getMemoryPart() == 0) {
                    logicalQubitAddressForControllers.add(0, key);
                } else {
                    logicalQubitAddressForControllers.add(key);
                }
            }
        }
        return logicalQubitAddressForControllers;
    }

    private void executeNextCommand() {
        if (mThreadForCommandsExecuting == null) {
            mThreadForCommandsExecuting = new Thread(() -> {
                while (!mCommandsForControllerMap.isEmpty()) {
                    int commandsMaxCount = mQuantumMemoryOperator.getCommandsMaxCount();
                    Map<OwnerData, List<LogicalAddressingCommand>> dataForController = new HashMap<>();
                    int commandsCount = 0;
                    //fill map which contains the largest possible number of commands that quantum machine can run at one time
                    for (OwnerData ownerData : mOwnerDataList) {
                        List<LogicalAddressingCommand> commandsList = mCommandsForControllerMap.get(ownerData);
                        if (commandsList != null) {
                            commandsCount += commandsList.size();
                            if (commandsCount <= commandsMaxCount) {
                                dataForController.put(ownerData, commandsList);
                            } else {
                                break;
                            }
                        }
                    }
                    Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> results = mQuantumMemoryOperator.executeCommands(dataForController);
                    for (OwnerData ownerData : results.keySet()) {
                        Map<LogicalQubitAddressForController, Boolean> measureResultsWithLogicalAddresses = results.get(ownerData);
                        Map<LogicalQubitAddressFromClient, TopLevelResult> measureResultsWithClientAddresses = new HashMap<>();
                        for (LogicalQubitAddressForController logicalQubitAddressForController : measureResultsWithLogicalAddresses.keySet()) {
                            LogicalQubitAddressFromClient logicalQubitAddressFromClient = mAddressesCorrespondenceMap.get(logicalQubitAddressForController);
                            if (logicalQubitAddressFromClient != null) {
                                //fill TopLevelResult with results of two measured qubits with the same globalId and different memory part
                                TopLevelResult oneLoqicalQubitResult = measureResultsWithClientAddresses.get(logicalQubitAddressFromClient);
                                if (oneLoqicalQubitResult == null) {
                                    oneLoqicalQubitResult = new TopLevelResult();
                                    measureResultsWithClientAddresses.put(logicalQubitAddressFromClient, oneLoqicalQubitResult);
                                }
                                if (logicalQubitAddressForController.getMemoryPart() == 0) {
                                    oneLoqicalQubitResult.setQubit_1MeasureResult(measureResultsWithLogicalAddresses.get(logicalQubitAddressForController));
                                } else {
                                    oneLoqicalQubitResult.setQubit_2MeasureResult(measureResultsWithLogicalAddresses.get(logicalQubitAddressForController));
                                }
                            }
                        }
                        LinkedHashMap<LogicalQubitAddressFromClient, Integer> resultsForSending = new LinkedHashMap<>();
                        for (LogicalQubitAddressFromClient addressFromClient : measureResultsWithClientAddresses.keySet()) {
                            //transform measure result of two physical qubit to one logical qubit measure result
                            TopLevelResult result = measureResultsWithClientAddresses.get(addressFromClient);
                            if (!result.getQubit_1MeasureResult() && result.getQubit_2MeasureResult()) {
                                resultsForSending.put(addressFromClient, 0);
                            } else if (result.getQubit_1MeasureResult() && !result.getQubit_2MeasureResult()) {
                                resultsForSending.put(addressFromClient, 1);
                            } else {
                                //both physical qubit are 0 or 1 - error
                            }
                        }
                        printResults(ownerData, resultsForSending);
                        //todo вернуть пользователю по socket с помощью OwnerData
                        mCommandsForControllerMap.remove(ownerData);
                        mOwnerDataList.remove(ownerData);
                    }
                }
                mThreadForCommandsExecuting = null;
            });
            mThreadForCommandsExecuting.run();
        }
    }

    private void printResults(OwnerData ownerData, LinkedHashMap<LogicalQubitAddressFromClient, Integer> results) {
        System.out.println("UserId = " + ownerData.getUserId() + ", results:");
        for (LogicalQubitAddressFromClient addressFromClient : results.keySet()) {
            System.out.println("AddressFromClient = " + addressFromClient.getLogicalQubitAddress() + " , result = " + results.get(addressFromClient));
        }
    }
}
