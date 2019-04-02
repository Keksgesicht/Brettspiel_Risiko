package base.collections;

import java.util.Comparator;

/**
 * @author Jan Braun
 *
 */
public class IntegerComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer i1, Integer i2) {
		return i1.compareTo(i2);
	}

}
