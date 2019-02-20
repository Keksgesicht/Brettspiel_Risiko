package resources.database;

/**
 * @author Jan Braun
 *
 */
public class Relation<T> {
	
	T[] ts;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Relation(T... ts) {
		this.ts = ts;
	}

}
