package sv.projects.listperfcomp;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Entry public access point to the functionality of the List Concurrent Performance Comparator
 * (see {@link PerformanceComparator}) that measures performance of different types of lists -
 * <code>CopyOnWriteArrList, SynchronizedRandomAccessList, ArrayList, LinkedList,</code> ... -
 * in the case when the Lists are accessed by two concurrent threads performing
 * <code>get()</code> or <code>add()</code> actions.
 */

public class Main {
    static List<Integer> copyOnWriteArrList = new CopyOnWriteArrayList<>();
    static List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
    static List<Integer> arrayList = new ArrayList<>();
    static List<Integer> linkedList = new LinkedList<>();

    public static void main(String[] args) {

        System.out.println("~~~~~ List Concurrent Performance Comparator [v." + PerformanceComparator.getVersion() + "] ~~~~~");
        boolean toExit = false;
        printMenu();
        try (Scanner choice = new Scanner(System.in)) {
            while (!toExit) {
                switch (getIntInput(choice)) {
                    case 0:
                        PerformanceComparator.printAllParsAndLists();
                        break;
                    case 1:
                        System.out.print("Enter number of testing cycles of the lists. ");
                        PerformanceComparator.setCorrectNumberOfTestCycles(getIntInput(choice));
                        break;
                    case 2:
                        promptAndStoreListParsInput(choice);
                        break;
                    case 3:
                        promptToSelectLists(choice);
                        PerformanceComparator.resetBothLists();
                        printMenu();
                        break;
                    case 4:
                        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
                        break;
                    case 5:
                        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ADD_MEMBER);
                        break;
                    case 9:
                        toExit = true;
                        break;
                    default:
                        System.out.println("Expected: ");
                        printMenu();
                }
            }
        }
    }

    private static void promptAndStoreListParsInput(Scanner listParsChoice) {
        System.out.print("The NUMBER of elements in the lists. ");
        int elems = getIntInput(listParsChoice);
        System.out.print("The LOWEST limit of random value integers to add to the lists. ");
        int lowLimit = getIntInput(listParsChoice);
        System.out.print("The HIGHEST limit of random value integers to add to the lists. ");
        int highLimit = getIntInput(listParsChoice);
        PerformanceComparator.setCorrectListIniPars(new int[] {elems, lowLimit, highLimit});
    }

    private static void promptToSelectLists(Scanner listChoice) {
        int numberOfListsThatSet = 0;
        printSelectListsMenu();
        while (numberOfListsThatSet != 2) {
            switch (getIntInput(listChoice)) {
                case 0:
                    PerformanceComparator.printAllParsAndLists();
                    break;
                case 1:
                    numberOfListsThatSet = setListAndGetNumberOfListsThatSet(numberOfListsThatSet, copyOnWriteArrList);
                    break;
                case 2:
                    numberOfListsThatSet = setListAndGetNumberOfListsThatSet(numberOfListsThatSet, syncList);
                    break;
                case 3:
                    numberOfListsThatSet = setListAndGetNumberOfListsThatSet(numberOfListsThatSet, arrayList);
                    break;
                case 4:
                    numberOfListsThatSet = setListAndGetNumberOfListsThatSet(numberOfListsThatSet, linkedList);
                    break;
                default:
                    System.out.println("Expected: ");
                    printSelectListsMenu();
            }
        }
    }

    private static int setListAndGetNumberOfListsThatSet(int nmbOfLists, List<Integer> list) {
        if (nmbOfLists == 0)
            PerformanceComparator.setList1(list);
        else
            PerformanceComparator.setList2(list);
        nmbOfLists++;
        return nmbOfLists;
    }

    private static int getIntInput(Scanner intReader) {
        System.out.print("Enter your choice : ");
        int choice = intReader.nextInt();
        intReader.nextLine();                 // NOTE, needed to finalize input
        return choice;
    }

    private static void printMenu() {
        System.out.println("0 - SHOW current configuration (test cycles, list parameters, list types and data)");
        System.out.println("1 - update number of TEST CYCLES");
        System.out.println("2 - update list PARAMETERS");
        System.out.println("3 - update list TYPES and list DATA");
        System.out.println("4 - test and show average time of ACCESSING all the elements of the lists");
        System.out.println("5 - test and show average time of ADDING new elements to the lists");
        System.out.println("9 - exit");
//        List<String> stringList = new ArrayList<>();
//        stringList.add("FF");
//        stringList.add("BB");
//        stringList.add("##");
//        stringList.add("bb");
//        stringList.add("AA");
//        stringList.add("aa");
//        System.out.println(stringList);
//        stringList.sort(Comparator.naturalOrder());
//        System.out.println("SORTED: " + stringList);
//        System.out.println("1st elem: " + stringList.get(0));
//        maltem();
    }

    private static void printSelectListsMenu() {
        System.out.println("1|2|3|4 - choose CopyOnWriteArrList | SynchronizedRandomAccessList | ArrayList | LinkedList");
    }

 /*   private static void maltem() {
        ArrayList<String> pwdList = new ArrayList<>(
                Arrays.asList("Ib#", "Blo","F09", "Hkf", "Ev!", "K%2", "Awe",
                        "Cve", "Lee", "J61", "Gvc", "Dde")
        );

        List<String> skillList = List.of("Spring", "Java EE 8+", "Hibernate",
                "JUnit", "Play");

        List<String> mySkills = new ArrayList<>();
        mySkills.add("Java EE 8+");
        mySkills.add("JUnit");
        mySkills.add("Spring 5");
        mySkills.add("Hibernate");
        mySkills.add("Lucene");
        final int[] javaExperience = {0};
        final int[] curiosity = {0};
        mySkills.forEach(skill -> {
            if(skillList.contains(skill)) {
                javaExperience[0]++;
            } else {
                curiosity[0]++;
            }
        });

        System.out.println("Exp & curio: " + javaExperience[0] + curiosity[0]);

        if (javaExperience[0] + curiosity[0] > 4) {
            pwdList.sort(Comparator.naturalOrder());
            String thePassword = "";
            for(int i = 0; i < 5; i++) {
                thePassword += pwdList.get(i).substring(1);
            }
            System.out.println(
                    "Give this password to Nathalie our HR manager: " + thePassword
            );
        }
    }*/
}
