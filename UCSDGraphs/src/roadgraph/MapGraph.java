/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import geography.GeographicPoint;
import geography.RoadSegment;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private Map<GeographicPoint, List<GeographicPoint>> adjListsMap;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		adjListsMap = new HashMap<>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		// returns the number of vertices
		return adjListsMap.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//returns a HashSet of the vertices in the graph in terms of their GeographicPoint
		return adjListsMap.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		// returns the number of edges in the graph
		int numEdges = 0;
		for (GeographicPoint vertex : adjListsMap.keySet()) {
			numEdges += adjListsMap.get(vertex).size();
		}
		return numEdges;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// adds a vertex to the graph at location GeographicPoint
		// returns true if successful, false if unsuccessful
		if (location == null || adjListsMap.containsKey(location)) {
			return false;
		}
		adjListsMap.put(location, new LinkedList<GeographicPoint>());
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//adds a directed edge between two GeographicPoints that are already in the graph
		//throws IllegalArgumentException if any of the arguments are null or if the length is less than 0
		if (from == null || to == null || roadName == null || roadType == null || length < 0) {
			throw new IllegalArgumentException();
		}
		if (!adjListsMap.containsKey(from) || !adjListsMap.containsKey(to)) {
			throw new IllegalArgumentException();
		}
		// get the list of edges for the from vertex and add the to vertex to it
		adjListsMap.get(from).add(to);

	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		// Implement the breadth-first search algorithm
		Queue<GeographicPoint> queue = new LinkedList<>();
		Set<GeographicPoint> visited = new HashSet<>();
		Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

		queue.add(start);
		visited.add(start);

		while (!queue.isEmpty()) {
			GeographicPoint current = queue.poll();
			nodeSearched.accept(current);

			if (current.equals(goal)) {
				// Reconstruct the path from start to goal
				return reconstructPath(start, goal, parentMap);
			}

			List<GeographicPoint> neighbors = adjListsMap.get(current);
			if (neighbors != null) {
				for (GeographicPoint neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						visited.add(neighbor);
						parentMap.put(neighbor, current);
						queue.add(neighbor);
					}
				}
			}
		}

		// No path found from start to goal
		return null;
	}

	// Helper method to reconstruct the path from start to goal
	private List<GeographicPoint> reconstructPath(GeographicPoint start, GeographicPoint goal,
												  Map<GeographicPoint, GeographicPoint> parentMap) {
		List<GeographicPoint> path = new ArrayList<>();
		GeographicPoint current = goal;

		while (current != null) {
			path.add(0, current);
			current = parentMap.get(current);
		}

		return path;
	}
	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		// Initialize the priority queue, visited set, parent hashmap and distance hashmap
		Map<GeographicPoint, Double> distanceMap = new HashMap<>();
		PriorityQueue<GeographicPoint> queue = new PriorityQueue<>(Comparator.comparingDouble(point -> distanceMap.get(point)));
		Set<GeographicPoint> visited = new HashSet<>();
		Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

		// add counter for nodes
		int nodes = 0;

		// Initialize the distance map
		for (GeographicPoint vertex : adjListsMap.keySet()) {
			distanceMap.put(vertex, Double.POSITIVE_INFINITY);
		}
		distanceMap.put(start, 0.0);

		queue.add(start);

		while (!queue.isEmpty()) {
			// add counter for nodes
			nodes++;
			GeographicPoint current = queue.poll();
			nodeSearched.accept(current);

			// print out the details of the current node
			System.out.println("DIJKSTRA visiting[NODE at location (Lat: " + current.getX() 
        + "\nLon: " + current.getY() + ") intersects streets: " + adjListsMap.get(current) + "]");

			if (!visited.contains(current)) {
				visited.add(current);

				if (current.equals(goal)) {
					System.out.println("Number of nodes visited: " + nodes);
					// Reconstruct the path from start to goal
					return reconstructPath(start, goal, parentMap);
				}

				List<GeographicPoint> neighbors = adjListsMap.get(current);
				if (neighbors != null) {
					for (GeographicPoint neighbor : neighbors) {
						if (!visited.contains(neighbor)) {
							double distance = distanceMap.get(current) + current.distance(neighbor);
							if (distance < distanceMap.get(neighbor)) {
								queue.remove(neighbor);
								distanceMap.put(neighbor, distance);
								parentMap.put(neighbor, current);
								queue.add(neighbor);
							}
						}
					}
				}
			}
		}

		// No path found from start to goal
		return null;
	}


	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		// Initialize the priority queue to consider both actual distance traveled and estimated distance to the goal
		Map<GeographicPoint, Double> distanceMap = new HashMap<>();
		PriorityQueue<GeographicPoint> queue = new PriorityQueue<>(Comparator.comparingDouble(point -> distanceMap.get(point) + point.distance(goal)));
		Set<GeographicPoint> visited = new HashSet<>();
		Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

		// Initialize the distance map with infinity values
		for (GeographicPoint vertex : adjListsMap.keySet()) {
			distanceMap.put(vertex, Double.POSITIVE_INFINITY);
		}
		distanceMap.put(start, 0.0);

		queue.add(start);

		while (!queue.isEmpty()) {
			GeographicPoint current = queue.poll();

			// Only process the current node if it has not been visited
			if (!visited.contains(current)) {
				visited.add(current);
				nodeSearched.accept(current);

				// Print out the details of the current node
				System.out.println("A* visiting[NODE at location (Lat: " + current.getX() 
					+ "\nLon: " + current.getY() + ") intersects streets: " + adjListsMap.get(current) + "]");
				System.out.println("Actual = " + distanceMap.get(current) + ", Pred: " + (distanceMap.get(current) + current.distance(goal)));

				if (current.equals(goal)) {
					System.out.println("Number of nodes visited: " + visited.size());
					return reconstructPath(start, goal, parentMap);
				}

				List<GeographicPoint> neighbors = adjListsMap.get(current);
				for (GeographicPoint neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						double distance = distanceMap.get(current) + current.distance(neighbor);
						if (distance < distanceMap.get(neighbor)) {
							distanceMap.put(neighbor, distance);
							parentMap.put(neighbor, current);
							// Re-insert the neighbor into the queue with updated priority
							queue.remove(neighbor); // Remove the old instance with outdated priority
							queue.add(neighbor);
						}
					}
				}
			}
		}

		// No path found from start to goal
		return null;
	}


	
	
	public static void main(String[] args)
	{
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		// test for end of the week quiz
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
	}
}
