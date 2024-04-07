//I am taking a set of triples

import java.util.*;

// Class to represent a graph node
class GraphNode {
    String value;
    List<String> adjacentNodes;

    public GraphNode(String value) {
        this.value = value;
        this.adjacentNodes = new ArrayList<>();
    }
}

// Class to represent an undirected graph
class Graph {
    Map<String, GraphNode> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    // Method to add an edge between two nodes (undirected)
    public void addEdge(String node1, String node2) {
        if (!nodes.containsKey(node1)) {
            nodes.put(node1, new GraphNode(node1));
        }
        if (!nodes.containsKey(node2)) {
            nodes.put(node2, new GraphNode(node2));
        }
        nodes.get(node1).adjacentNodes.add(node2);
        nodes.get(node2).adjacentNodes.add(node1); // Adding for undirected graph
    }

    // Method to perform depth-first search
    private void dfs(String node, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String adj : nodes.get(node).adjacentNodes) {
            if (!visited.contains(adj)) {
                dfs(adj, visited, component);
            }
        }
    }

    // Method to calculate components
    public List<List<String>> getComponents() {
        Set<String> visited = new HashSet<>();
        List<List<String>> components = new ArrayList<>();
        for (String node : nodes.keySet()) {
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs(node, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    // Method to check if an edge exists in all existing graphs
    public boolean edgeExistsInAllGraphs(String node1, String node2, List<Graph> graphs) {
        for (Graph graph : graphs) {
            if (!graph.edgeExists(node1, node2)) {
                return false;
            }
        }
        return true;
    }

    // Method to check if an edge exists
    public boolean edgeExists(String node1, String node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            return false;
        }
        return nodes.get(node1).adjacentNodes.contains(node2) || nodes.get(node2).adjacentNodes.contains(node1);
    }

    // Method to print the graph
    public void printGraph() {
        for (GraphNode node : nodes.values()) {
            System.out.print("Node " + node.value + " --> ");
            for (String adj : node.adjacentNodes) {
                System.out.print(adj + " ");
            }
            System.out.println();
        }
    }

    // Method to get the total number of nodes
    public int numberOfNodes() {
        return nodes.size();
    }

    // Method to get the number of components
    public int numberOfComponents() {
        return getComponents().size();
    }
}

public class Main {
    public static void main(String[] args) {
        // List of created graphs
        List<Graph> graphs = new ArrayList<>();

        // Sample input strings
        String[] inputStrings = {"A,B,C", "D,E,F", "J,G,C", "F,H,Q", "A,I,C","D,X,Q","D,X,F","A,M,C"};

        // Processing input strings
        for (String str : inputStrings) {
            String[] tuple = str.split(",");
            String node1 = tuple[0];
            String node2 = tuple[2];

            boolean edgeExistsInAllGraphs = true;
            for (Graph graph : graphs) {
                if (!graph.edgeExists(node1, node2)) {
                    edgeExistsInAllGraphs = false;
                    break;
                }
            }

            if (edgeExistsInAllGraphs) {
                Graph newGraph = new Graph();
                newGraph.addEdge(node1, node2);
                graphs.add(newGraph);
            } else {
                for (Graph graph : graphs) {
                    if (!graph.edgeExists(node1, node2)) {
                        graph.addEdge(node1, node2);
                        break;
                    }
                }
            }
        }

        // Printing details of each graph
        for (int i = 0; i < graphs.size(); i++) {
            Graph graph = graphs.get(i);
            System.out.println("Graph-" + (i + 1));
            graph.printGraph();
            System.out.println("Number of nodes: " + graph.numberOfNodes());
            System.out.println("Number of components: " + graph.numberOfComponents());
            System.out.println();
        }
    }
}

// Input:
// String[] inputStrings = {"A,B,C", "D,E,F", "J,G,C", "F,H,Q", "A,I,C","D,X,Q","D,X,F","A,M,C"};

//Output:
// Graph-1
// Node A --> C 
// Node Q --> F D 
// Node C --> A J 
// Node D --> F Q 
// Node F --> D Q 
// Node J --> C 
// Number of nodes: 6
// Number of components: 2

// Graph-2
// Node A --> C 
// Node C --> A 
// Node D --> F 
// Node F --> D 
// Number of nodes: 4
// Number of components: 2

// Graph-3
// Node A --> C 
// Node C --> A 
// Number of nodes: 2
// Number of components: 1
