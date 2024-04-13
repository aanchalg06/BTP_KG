import java.util.*;

// Class to represent a graph node
class GraphNode {
    List<String> values;
    List<String> adjacentNodes;

    public GraphNode(List<String> values) {
        this.values = values;
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
    public void addEdge(List<String> node1, List<String> node2) {
        String key1 = getKey(node1);
        String key2 = getKey(node2);

        if (!nodes.containsKey(key1)) {
            nodes.put(key1, new GraphNode(node1));
        }
        if (!nodes.containsKey(key2)) {
            nodes.put(key2, new GraphNode(node2));
        }
        nodes.get(key1).adjacentNodes.add(key2);
        nodes.get(key2).adjacentNodes.add(key1); // Adding for undirected graph
    }

    // Helper method to get a string key from a list of values
    private String getKey(List<String> values) {
        StringBuilder keyBuilder = new StringBuilder();
        for (String value : values) {
            keyBuilder.append(value.toLowerCase()).append("_");
        }
        return keyBuilder.toString();
    }

    // Method to perform depth-first search
    private void dfs(String key, Set<String> visited, List<String> component) {
        visited.add(key);
        component.addAll(nodes.get(key).values);
        for (String adjKey : nodes.get(key).adjacentNodes) {
            if (!visited.contains(adjKey)) {
                dfs(adjKey, visited, component);
            }
        }
    }

    // Method to calculate components
    public List<List<String>> getComponents() {
        Set<String> visited = new HashSet<>();
        List<List<String>> components = new ArrayList<>();
        for (String key : nodes.keySet()) {
            if (!visited.contains(key)) {
                List<String> component = new ArrayList<>();
                dfs(key, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    // Method to check if an edge exists
    public boolean edgeExists(List<String> node1, List<String> node2) {
        String key1 = getKey(node1);
        String key2 = getKey(node2);
        return nodes.containsKey(key1) && nodes.containsKey(key2) &&
               nodes.get(key1).adjacentNodes.contains(key2);
    }

    // Method to print the graph
    public void printGraph() {
        for (String key : nodes.keySet()) {
            List<String> values = nodes.get(key).values;
            System.out.print("Node [");
            for (int i = 0; i < values.size(); i++) {
                System.out.print(values.get(i));
                if (i < values.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("] --> ");
            for (String adjKey : nodes.get(key).adjacentNodes) {
                List<String> adjValues = nodes.get(adjKey).values;
                System.out.print("[");
                for (int i = 0; i < adjValues.size(); i++) {
                    System.out.print(adjValues.get(i));
                    if (i < adjValues.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("] ");
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

    // Method to collect all pos elements present in the graph nodes
    public Set<String> collectPosElements() {
        Set<String> posElements = new HashSet<>();
        for (String key : nodes.keySet()) {
            List<String> values = nodes.get(key).values;
            posElements.addAll(values);
        }
        return posElements;
    }

    // Method to count the number of pos elements in the graph nodes
    public int countPosElements() {
        Set<String> posElements = collectPosElements();
        return posElements.size();
    }
}

public class Main {
    static double calculate_d(Graph graph, int n_ideal, int n_nonideal) {
    double numberOfNodes = (double) graph.numberOfNodes();
    double numberOfComponents = (double) graph.numberOfComponents();

    double term1 = Math.abs(1.0 - numberOfNodes / (double) n_nonideal);
    double term2 = Math.abs(n_ideal - n_nonideal);
    double term3 = Math.abs(1.0 - numberOfComponents);
    
    System.out.println(term1 + term2 + term3);

    return term1 + term2 + term3;
}

    
    public static void main(String[] args) {
        // List of created graphs
        List<Graph> graphs = new ArrayList<>();

        // Sample input strings
        String[] pos = {"Me", "a 35.1 minute long album", "Wharton Tiers", "that", "the album", "it"};
        String[][] inputStrings = {
            {"Turn Me On", "is", "a 35.1 minute long album produced by Wharton Tiers"},
            {"Turn Me On", "is", "a 35.1 minute long album"},
            {"a 35.1 minute long album", "be produced", "by Wharton Tiers"},
            {"Wharton Tiers", "was followed", "by the album entitled Take it Off"},
            {"the album", "be entitled", "Take it Off"},
            {"Turn Me On", "are", "a 35.1 minute long album produced by Wharton Tiers"}
        };

        // Processing input strings
        for (String[] tuple : inputStrings) {
            String node1 = tuple[0];
            String relation = tuple[1];
            String node2 = tuple[2];

            List<String> nodeList1 = new ArrayList<>();
            List<String> nodeList2 = new ArrayList<>();

            // Find matching elements in pos array for node1
            for (String word : pos) {
                if (node1.toLowerCase().contains(word.toLowerCase())) {
                    nodeList1.add(word);
                }
            }

            // Find matching elements in pos array for node2
            for (String word : pos) {
                if (node2.toLowerCase().contains(word.toLowerCase())) {
                    nodeList2.add(word);
                }
            }

            boolean edgeExistsInAllGraphs = true;
            for (Graph graph : graphs) {
                if (!graph.edgeExists(nodeList1, nodeList2)) {
                    edgeExistsInAllGraphs = false;
                    break;
                }
            }

            if (edgeExistsInAllGraphs) {
                Graph newGraph = new Graph();
                newGraph.addEdge(nodeList1, nodeList2);
                graphs.add(newGraph);
            } else {
                for (Graph graph : graphs) {
                    if (!graph.edgeExists(nodeList1, nodeList2)) {
                        graph.addEdge(nodeList1, nodeList2);
                        break;
                    }
                }
            }
        }

        // Printing details of each graph
        int[] posElementCounts = new int[graphs.size()];

        for (int i = 0; i < graphs.size(); i++) {
            Graph graph = graphs.get(i);
            System.out.println("Graph-" + (i + 1));
            graph.printGraph();
            System.out.println("Number of nodes: " + graph.numberOfNodes());
            System.out.println("Number of components: " + graph.numberOfComponents());
            System.out.println();

            // Count pos elements in the graph
            posElementCounts[i] = graph.countPosElements();

            // Print all ArrayList of nodes
            System.out.println("ArrayList of nodes in Graph-" + (i + 1) + ":");
            for (String key : graph.nodes.keySet()) {
                List<String> values = graph.nodes.get(key).values;
                System.out.println("Node [" + String.join(", ", values) + "]");
            }
            System.out.println();

            // Collect all pos elements present in the graph nodes
            Set<String> posElements = graph.collectPosElements();
            System.out.println("All pos elements in Graph-" + (i + 1) + ": " + posElements);
            System.out.println();
        }

        // Print array of pos element counts
        System.out.println("Number of pos elements in each graph:");
        for (int i = 0; i < posElementCounts.length; i++) {
            System.out.println("Graph-" + (i + 1) + ": " + posElementCounts[i]);
        }
        
        double sum=0;
        for(int i=0;i<graphs.size();i++){
            sum+= calculate_d(graphs.get(i),pos.length, posElementCounts[i]);
        }
        
        System.out.println("Metric sum is "+sum);
    }
}
