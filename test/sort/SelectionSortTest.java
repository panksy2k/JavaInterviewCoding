package sort;

import com.pankaj.sort.SelectionSort;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by pankajpardasani on 27/01/2017.
 */
public class SelectionSortTest {

    @Test
    public void testSort() {
        Integer[] sortedInt = SelectionSort.<Integer>sort(getTestUnsortedIntegers());
        Assert.assertArrayEquals(new Integer[] {1, 12, 51, 91, 98, 100, 101}, sortedInt);
    }

    private Integer[] getTestUnsortedIntegers() {
        return new Integer[] {51, 91, 12, 100, 98, 101, 1};
    }
}
