/**
 * 
 */
package base.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jan Braun
 */
public class Graph<Edge extends Comparable<Edge>, Node> {

	private final Edge NaN;
	private Map<Node, Integer> nodes;
	private HashMap<String, SqrMatrix<Edge>> adjazenzmatrizen;
	private Integer size = 0;

	/**
	 * @param NaN the value that represants that there is no Edge between two Nodes
	 */
	public Graph(Edge NaN) {
		this.NaN = NaN;
		nodes = new HashMap<Node, Integer>();
		adjazenzmatrizen = new HashMap<String, SqrMatrix<Edge>>();
	}

	/**
	 * @param NaN   the value that represants that there is no Edge between two
	 *              Nodes
	 * @param nodes the list of all nodes
	 */
	public Graph(Edge NaN, Collection<Node> nodes) {
		this.NaN = NaN;
		this.nodes = new HashMap<Node, Integer>();
		adjazenzmatrizen = new HashMap<String, SqrMatrix<Edge>>();
		for (Node n : nodes)
			addNode(n);
	}

	/**
	 * adds a new node to this Graph
	 * 
	 * @param newNode the new node
	 */
	public void addNode(Node newNode) {
		for (SqrMatrix<Edge> m : adjazenzmatrizen.values()) {
			m.addNode();
		}
		nodes.put(newNode, size++);
	}

	/**
	 * sets the value weight to a specific position in a specific matrix
	 * 
	 * @param name   the name of the adjazenzmatrize
	 * @param row    the row count of the specific position
	 * @param column the column count of the specific position
	 * @param weight the value that gets set
	 */
	public void setWeight(String name, int row, int column, Edge weight) {
		if (adjazenzmatrizen.containsKey(name))
			adjazenzmatrizen.get(name).setWeight(row, column, weight);
		else {
			SqrMatrix<Edge> newAJM = new SqrMatrix<Edge>(NaN);
			for (int i = 0; i < nodes.size(); i++) {
				newAJM.addNode();
			}
			newAJM.setWeight(row, column, weight);
			adjazenzmatrizen.put(name, newAJM);
		}
	}

	/**
	 * sets the value weight to a specific position in a specific matrix
	 * 
	 * @param name   the name of the adjazenzmatrize
	 * @param row    the row count of the specific position
	 * @param column the column count of the specific position
	 * @param weight the value that gets set
	 */
	public void setWeight(String name, Node row, Node column, Edge weight) {
		if (!nodes.containsKey(row))
			addNode(row);
		if (!nodes.containsKey(column))
			addNode(column);
		setWeight(name, nodes.get(row), nodes.get(column), weight);
	}

	/**
	 * @param name   the name of the adjazenzmatrize
	 * @param row    the row count of the specific position
	 * @param column the column count of the specific position
	 * @return the value at a specific position in the specific adjazenzmatrix
	 */
	public Edge getWeight(String name, int row, int column) {
		return adjazenzmatrizen.containsKey(name) ? adjazenzmatrizen.get(name).getWeight(row, column) : NaN;
	}

	/**
	 * @param name   the name of the adjazenzmatrize
	 * @param row    the row count of the specific position
	 * @param column the column count of the specific position
	 * @return the value at a specific position in the specific adjazenzmatrix
	 */
	public Edge getWeight(String name, Node row, Node column) {
		if (!nodes.containsKey(row) || !nodes.containsKey(column))
			return NaN;
		return getWeight(name, nodes.get(row), nodes.get(column));
	}

	public List<Node> getAdjazenzListe(String name, Node row) {
		if(!adjazenzmatrizen.containsKey(name))
			return null;
		List<Node> neighboors = new ArrayList<Node>();
		List<Edge> adjRow = adjazenzmatrizen.get(name).getAdjazenzListe(nodes.get(row));
		for (int i = 0; i < adjRow.size(); i++) {
			final Integer j = new Integer(i);
			if (adjRow.get(j) != NaN)
				neighboors.add(nodes.keySet().stream().filter(n -> nodes.get(n).equals(j)).findFirst().get());
		}
		return neighboors;
	}

	/**
	 * @return the number of nodes in this graph
	 */
	public int getNumberOfNodes() {
		return size;
	}

	/**
	 * @return the list of all nodes
	 */
	public Set<Node> getNodes() {
		return nodes.keySet();
	}

}
