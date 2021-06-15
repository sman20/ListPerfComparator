package sv.projects.listperfcomp;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Entry point to the List Concurrent Performance Comparator that measures performance of different types of lists -
 * <code>CopyOnWriteArrList, SynchronizedRandomAccessList, ArrayList, LinkedList,</code> ...
 * - in the case when the Lists are accessed by two concurrent threads performing
 * <code>get()</code> or <code>add()</code> actions.
 <br>
 The Comparator has got an interactive command-line menu through which it is possible to :
 <ul>
 <li>show current configuration on the screen</li>
 <li>update current configuration</li>
 <li>run tests and show average results for lists under tests</li>
 <li>the following list types can be selected for test :
 <ul style="list-style-type: circle;">
 <li><code>CopyOnWriteArrList</code></li>
 <li><code>SynchronizedRandomAccessList</code></li>
 <li><code>ArrayList</code></li>
 <li><code>LinkedList</code></li>
 </ul>
 </li>
 </ul>
 <h3>Concurrent performance test description</h3>
 <h4>Mechanism of retrieving data</h4>
 Two <code>Callable</code> threads perform a selected action (<code>access</code> a member or <code>add</code> a member)
 on first and second halves of a list respectively.
 Submitting the threads to <code>newFixedThreadPool</code> and starting them simultaneously via <code>CountDownLatch</code>.
 The threads will compete to perform the action on the assigned part of the list.
 <br><br>
 <h4>Further data processing</h4>
 The results time is aggregated separately for each thread of each list under test. Then averages are calculated in <code>ms</code>
 and formatted results are printed out.
  <br><i>Example:</i><br>
 <pre>          CopyOnWriteArrayList, size [1000]  -  action: get()  |  thread 1 :       2 ms  |  thread 2 :       2 ms
  SynchronizedRandomAccessList, size [1000]  -  action: get()  |  thread 1 :      11 ms  |  thread 2 :      13 ms</pre>
 <br>
 <h3>JUnit tests</h3>
 JUnit tests are covering a part of functionality.

 @author S.V.
 @version 1.0.1
 @since 2021-05-01
 */

public class PerformanceComparator {
    private final static String version = "0.1";
    static List<Integer> list1;
    static List<Integer> list2;
    static int[] listIniPars;       // [listSize, valueFrom, valueTo]
    static int numberOfTestCycles = 1;  // default value

    enum Action {
        ACCESS_MEMBER("get()"),
        ADD_MEMBER("add()");

        private String name;

        Action(String actionName) {
            this.name = actionName;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    static void testAndPrint(Action action) {
        if (isInitialDataValid()) {
            testListsPerfAndPrint(action);
        }
    }

    private static boolean isInitialDataValid() {       // NOTE, the order is important here
        return isListWithData(list1, "1st") && isListWithData(list2, "2nd") && isIniParsCorrect() && isTestCyclesCorrect() && areParsAndListsSynced();
    }

    private static void testListsPerfAndPrint(Action action) {
        printTestResults(list1, action, getAvgListPerformanceInMsAndReset(action, list1));
        printTestResults(list2, action, getAvgListPerformanceInMsAndReset(action, list2));
    }

    private static boolean isIniParsCorrect() {
        if (listIniPars == null || listIniPars.length != 3 || listIniPars[0] < 1 || listIniPars[1] >= listIniPars[2]) {
            System.out.println("WARNING - Three list configuration pars are not correct ! Actual: [" + Arrays.toString(listIniPars) + "]");
            return false;
        }
        return true;
    }

    private static boolean areParsAndListsSynced() {
        if (list1.size() != 0 && list2.size() != 0 && (list1.size() != listIniPars[0] || list2.size() != listIniPars[0])) {
            System.out.println("WARNING - The list ini par " + listIniPars[0] + " not synced with list sizes : [" + list1.size() + " and " + list2.size() + "]");
            return false;
        }
        return true;
    }

    private static boolean isListWithData(List<Integer> list, String listName) {
        if (list == null || list.isEmpty()) {
            System.out.println("WARNING - The " + listName + " list to compare has no data ! [" + list + "]");
            return false;
        }
        return true;
    }

    private static boolean isTestCyclesCorrect() {
        if (numberOfTestCycles < 1) {
            System.out.println("WARNING - The number of test cycles is incorrect: [" + numberOfTestCycles + "]");
            return false;
        }
        return true;
    }

    private static void printTestResults(List<Integer> list, Action action, long[] testResult) {
        System.out.format("%30s, size [%d]  -  action: %s  |  thread 1 : %7d ms  |  thread 2 : %7d ms\n", list.getClass().getSimpleName(), list.size(), action, testResult[0], testResult[1]);
    }

    private static long[] getAvgListPerformanceInMsAndReset(Action action, List<Integer> list) {
        long[] averageResults = new long[]{0L, 0L};
        long[] currentResults;
        for (int i = 0; i < numberOfTestCycles; i++) {
            currentResults = calcTimeToPerformActionOnAllElem(list, action);
            averageResults[0] += currentResults[0];
            averageResults[1] += currentResults[1];
        }
        if (list.size() != listIniPars[0]) {
            resetList(list);
        }
        averageResults[0] = (averageResults[0] / numberOfTestCycles) / 1000;
        averageResults[1] = (averageResults[1] / numberOfTestCycles) / 1000;
        return averageResults;
    }

    private static long[] calcTimeToPerformActionOnAllElem(List<Integer> list, Action action) {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Long> computationResult1 = executor.submit(new ListRunner(list, 0, listIniPars[0] / 2, latch, action));
        Future<Long> computationResult2 = executor.submit(new ListRunner(list, listIniPars[0] / 2, listIniPars[0], latch, action));
        latch.countDown();
        executor.shutdown();
        long[] threadsResult = new long[2];
        try {
            threadsResult = new long[]{computationResult1.get(), computationResult2.get()};
        } catch (ExecutionException e) {
            System.out.println("Failed to retrieve a result from a thread. " + e);
        } catch (InterruptedException e) {
            System.out.println("Thread execution was interrupted. " + e);
        }
        return threadsResult;
    }

     private static void resetList(List<Integer> list) {
        list.clear();
        populateList(list);
    }

    static void resetBothLists() {
        if (isIniParsCorrect()) {
            resetList(list1);
            resetList(list2);
        }
    }

    static void populateList(List<Integer> list) {
        for (int i = 0; i < listIniPars[0]; i++) {
            // random int from listIniPars[1] included till listIniPars[2] excluded
            int randomValue = (int) (Math.random() * (listIniPars[2] - listIniPars[1]) + listIniPars[1]);
            list.add(i, randomValue);
        }
    }

    static void printAllParsAndLists() {
        System.out.println("The number of test cycles : [" + numberOfTestCycles + "]");
        System.out.print("Parameters of the Lists: ");
        System.out.println(Arrays.toString(PerformanceComparator.listIniPars));
        printListTypeAndContent("List 1", list1);
        printListTypeAndContent("List 2", list2);
    }

    private static void printListTypeAndContent(String listName, List<Integer> list) {
        System.out.print(listName + " - ");
        if (list == null)
            System.out.println("null");
        else {
            System.out.print("["+ list.getClass().getSimpleName() + "] - ");
            printAllListIntegers(list);
        }
    }

    private static void printAllListIntegers(List<Integer> list) {
        System.out.print("size:" + list.size() + " | ");
        for (Integer item : list) {
            System.out.print(item + "|");
        }
        System.out.println();
    }

    static void setList1(List<Integer> list1) {
        PerformanceComparator.list1 = list1;
    }

    static void setList2(List<Integer> list2) {
        PerformanceComparator.list2 = list2;
    }

    static void setCorrectListIniPars(int[] listIniPars) {
        int[] currentListIniPars = PerformanceComparator.listIniPars;
        PerformanceComparator.listIniPars = listIniPars;
        if (!isIniParsCorrect()) {
            PerformanceComparator.listIniPars = currentListIniPars;
        }
    }

    static String getVersion() {
        return new String(version);
    }

    static void setCorrectNumberOfTestCycles(int numberOfTestCycles) {
        int currentCycles = PerformanceComparator.numberOfTestCycles;
        PerformanceComparator.numberOfTestCycles = numberOfTestCycles;
        if (!isTestCyclesCorrect()) {
            PerformanceComparator.numberOfTestCycles = currentCycles;
        }
    }
}
