# QuantumMemoryManager

###Для чего предназначено:
Данный проект создан в формате _server - side_ для дальнейшей реализации связи _client - server_ и адаптации стандартных операторов из квантовых вычислений к командам самого квантового вычислителя

###Поддерживаемый функционал:
* Выполнение на эмуляторе следующих комманд:
  * QET
  * CQET
  * PHASE
  * INIT (Подстановка команды происходит во время выполнения в случае отсутствия кубита в памяти)
  * MEASURE (Данная команда выполняется для всех используемых кубитов после выполнения установленного набора комманд
* Поддержка обработки комманд от нескольких пользователей одновременно

###Пример использования:

#####Получение класса для взаимодействия с квантовым вычислителем:
```java
ServiceManager serviceManager = ServiceManager.getServiceManager();
```

#####Пример создания команды, использующую 2 кубита. К первому применяется команда _PHASE_ со значением параметра _Math.PI_,а ко второму - _QET_ со значением параметра _Math.PI / 2_:
```java
CommandsFromClientDTO commandsFromClientDTO = new CommandsFromClientDTO();
        commandsFromClientDTO.setQubitCount(2);
List<LogicalAddressingCommandFromClient> commandFromClientList = new LinkedList<LogicalAddressingCommandFromClient>();
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
```

#####Пример отправки вышеописанных команд на выполнение в _ServiceManager_
```java
serviceManager.putCommandsToExecutionQueue("1", new GsonBuilder().create().toJson(commandsFromClientDTO)); // 1 - userID
```
######Полный цикл выполнения можно посмотреть в
```java
Main.testWholeCycle()
```

###Запуск проекта
На данный момент необходимо импортировать проект в IntelliJ IDEA и запустить
```java
Main.main(String[] args)
```

###Сборка _.jar_ 
Сборка осуществляется при помощи _gradle_. Для получения _.jar_ файла необходимо выполнить в командной строке
```
./gradlew clean assemble
```
Требуемый файл - *quantum_memory_manager_library/build/libs/quantum_memory_manager_library.jar*
