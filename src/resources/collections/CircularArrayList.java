package resources.collections;

import java.util.ArrayList;

/**
 * @author Jan Braun
 *
 */
@SuppressWarnings("serial")
public class CircularArrayList<E> extends ArrayList<E> {
	
	@Override
	public E get(int index) {
		while(index < 0) index += size();
        return super.get(index % size());
    }

}
