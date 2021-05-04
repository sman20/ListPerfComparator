package sv.projects.listperfcomp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceComparatorTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void populateData() {
        PerformanceComparator.list1 = new ArrayList<>();
        PerformanceComparator.list1.add(0, 0);
        PerformanceComparator.list1.add(1, 1);
        PerformanceComparator.list1.add(2, 2);
        PerformanceComparator.list2 = new ArrayList<>();
        PerformanceComparator.list2.add(0, 9);
        PerformanceComparator.list2.add(1, 8);
        PerformanceComparator.list2.add(2, 7);
        PerformanceComparator.listIniPars = new int[]{3, 0, 10};
    }

    //    @Disabled
    @Test
    void testCompare_EmptyList1_NEG() {
        setupStreams();
        PerformanceComparator.list1 = Collections.EMPTY_LIST;
        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
        assertEquals("WARNING - The 1st list to compare has no data ! [[]]", outContent.toString().trim());
        restoreStreams();
    }

    @Test
    void testCompare_NullList1_NEG() {
        setupStreams();
        PerformanceComparator.list1 = new ArrayList<>();
        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
        assertEquals("WARNING - The 1st list to compare has no data ! [[]]", outContent.toString().trim());
        restoreStreams();
    }

    @Test
    void testCompare_NullParList_NEG() {
        setupStreams();
        PerformanceComparator.listIniPars = null;
        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
        assertEquals("WARNING - Three list configuration pars are not correct ! Actual: [null]", outContent.toString().trim());
        restoreStreams();
    }

    @Test
    void testCompare_WrongSizeParList_NEG() {
        setupStreams();
        PerformanceComparator.listIniPars = new int[]{3, 4};
        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
        assertEquals("WARNING - Three list configuration pars are not correct ! Actual: [[3, 4]]", outContent.toString().trim());
        restoreStreams();
    }

    @Test
    void testCompare_WrongPar1_NEG() {
        setupStreams();
        PerformanceComparator.listIniPars = new int[]{0, 3, 4};
        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
        assertEquals("WARNING - Three list configuration pars are not correct ! Actual: [[0, 3, 4]]", outContent.toString().trim());
        restoreStreams();
    }

    @Test
    void testCompare_WrongPar2And3_NEG() {
        setupStreams();
        PerformanceComparator.listIniPars = new int[]{10, 7, 2};
        PerformanceComparator.testAndPrint(PerformanceComparator.Action.ACCESS_MEMBER);
        assertEquals("WARNING - Three list configuration pars are not correct ! Actual: [[10, 7, 2]]", outContent.toString().trim());
        restoreStreams();
    }

    @Test
    void testPopulateList() {
        PerformanceComparator.populateList(PerformanceComparator.list1);
        assertNotEquals("[0, 1, 2]", Arrays.toString(PerformanceComparator.list1.toArray()));
    }

    @Test
    void testPopulateList_Size() {
        PerformanceComparator.list1 = new ArrayList<>();
        PerformanceComparator.populateList(PerformanceComparator.list1);
        assertEquals(3, PerformanceComparator.list1.size());
    }

    @Test
    void testPopulateList_Limits() {
        PerformanceComparator.list2 = new ArrayList<>();
        PerformanceComparator.populateList(PerformanceComparator.list2);
        for (int integer : PerformanceComparator.list2) {
            assertTrue(integer >= 0 && integer < 10);
        }
    }

    @Test
    void testSetList1() {
        List<Integer> list = new ArrayList<>();
        list.add(0, 3);
        list.add(1, 4);
        list.add(2, 5);
        PerformanceComparator.setList1(list);
        assertEquals("[3, 4, 5]", Arrays.toString(PerformanceComparator.list1.toArray()));
    }

    @Test
    void testSetList2() {
        List<Integer> list = new ArrayList<>();
        list.add(0, 6);
        list.add(1, 5);
        list.add(2, 4);
        PerformanceComparator.setList2(list);
        assertEquals("[6, 5, 4]", Arrays.toString(PerformanceComparator.list2.toArray()));
    }

    @Test
    void testSetListIniPars() {
        int[] newPars = new int[]{3, 14, 23};
        PerformanceComparator.setCorrectListIniPars(newPars);
        assertArrayEquals(newPars, PerformanceComparator.listIniPars);
    }

    public void setupStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}