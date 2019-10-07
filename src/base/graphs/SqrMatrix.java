package base.graphs;

import java.util.ArrayList;
import java.util.List;

class SqrMatrix<Edge extends Comparable<Edge>> {
	
	private final Edge NaN;
	private List<ArrayList<Edge>> matrix;
	
	/**
	 * creates a square matrix
	 * @param NaN when a new row and column gets added this value fills up the empty values
	 */
	SqrMatrix(Edge NaN) {
		this.NaN = NaN;
		matrix = new ArrayList<ArrayList<Edge>>();
	}
	
	/**
	 * adds a new row and column to the matrix
	 */
	void addNode() {
		for (List<Edge> alk : matrix) {
			alk.add(NaN);
		} ArrayList<Edge> newRow = new ArrayList<Edge>();
		for(int i=0; i <= matrix.size(); i++) {
			newRow.add(NaN);
		} matrix.add(newRow);		
	}
	
	/**
	 * sets the value weight to a specific position in the matrix 
	 * @param row the row count of the specific position
	 * @param column the column count of the specific position
	 * @param weight the value that gets set
	 */
	void setWeight(int row, int column, Edge weight) {
		matrix.get(row).set(column, weight);
	}
	
	/**
	 * @param row the row count of the specific position
	 * @param column the column count of the specific position
	 * @return the value at a specific position in the matrix 
	 */
	Edge getWeight(int row, int column) {
		return matrix.get(row).get(column);
	}

	/**
	 * @param row
	 * @return
	 */
	List<Edge> getAdjazenzListe(int row) {
		return matrix.get(row);
	}

}
