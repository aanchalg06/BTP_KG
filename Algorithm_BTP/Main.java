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




import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        return term1 + term2 + term3;
    }

    public static void main(String[] args) {

        String[] oieTools= new String[4];
        String[] oieOutput=new String[4];
        oieOutput[0]="clausie_output.txt";
        oieOutput[1]="stanford_4.5.3_openie_output.txt";
        oieOutput[2]="stanford_4.5.6_openie_output.txt";
        oieOutput[3]="minie_output.txt";
        oieTools[0]="triple_clauseie.json";
        oieTools[1]="stanford_4.5.3_openie.json";
        oieTools[2]="stanford_4.5.6_openie.json";
        oieTools[3]="triple_minie.json";
        JSONParser parser = new JSONParser();
        for(int tool = 0;tool<4;tool++)
        {
            double finalsum=0;
                try {
            Object obj = parser.parse(new FileReader("Json/"+oieTools[tool]));
            JSONObject triples = (JSONObject) obj;
            try(BufferedWriter writer = new BufferedWriter(new FileWriter("output/"+oieOutput[tool]))){

                for(int targetSentenceNumber =1;targetSentenceNumber<=2140;targetSentenceNumber++)
                {
                    if(triples.containsKey(String.valueOf(targetSentenceNumber))){
                        // List of created graphs
                    List<Graph> graphs = new ArrayList<>();

                    // Read lines from output_pos.txt and store them in a list
                    List<String> pos = new ArrayList<>();
                    BufferedReader br = new BufferedReader(new FileReader("output_pos.txt"));
                    String line;
                    int lineNumber = 0;
                    while ((line = br.readLine()) != null) {

                        if (lineNumber == targetSentenceNumber - 1) {
                            // Split the line into individual elements
                            String[] elements = line.replaceAll("\\[|\\]", "").split(",\\s*");
                            
                            // Add each element to the list
                            for (String element : elements) {
                                pos.add(element.trim());
                            }
                            
                            break; // Stop reading after finding the target line
                        }

                        lineNumber++;
                    }



                    // Retrieve the values corresponding to targetSentenceNumber
                    List<List<String>> newinputStrings = new ArrayList<>();
                    if (triples != null && triples.containsKey(String.valueOf(targetSentenceNumber))) {
                        newinputStrings = (List<List<String>>) triples.get(String.valueOf(targetSentenceNumber));
                    }

                    List<List<String>> inputStrings = new ArrayList<>();

                    for (List<String> innerList : newinputStrings) {
                        List<String> newList = new ArrayList<>(innerList);
                        inputStrings.add(newList);
                    }
                    // System.out.println(pos.toString());
                    // System.out.println(inputStrings.toString());

                    // Processing input strings
                    for (List<String> tuple : inputStrings) {
                        
                        String node1 = tuple.get(0);
                        String relation = tuple.get(1);
                        String node2 = tuple.get(2);

                        List<String> nodeList1 = new ArrayList<>();
                        List<String> nodeList2 = new ArrayList<>();

                        // Find matching elements in pos list for node1
                        for (String word : pos) {
                            if (node1.toLowerCase().contains(word.toLowerCase())) {
                                nodeList1.add(word);
                            }
                        }

                        // Find matching elements in pos list for node2
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
                        // System.out.println("Graph-" + (i + 1));
                        // // graph.printGraph();
                        // System.out.println("Number of nodes: " + graph.numberOfNodes());
                        // System.out.println("Number of components: " + graph.numberOfComponents());
                        // System.out.println();

                        // Count pos elements in the graph
                        posElementCounts[i] = graph.countPosElements();

                        // Print all ArrayList of nodes
                        // System.out.println("ArrayList of nodes in Graph-" + (i + 1) + ":");
                        // for (String key : graph.nodes.keySet()) {
                        //     List<String> values = graph.nodes.get(key).values;
                        //     System.out.println("Node [" + String.join(", ", values) + "]");
                        // }
                        // System.out.println();

                        // Collect all pos elements present in the graph nodes
                        // Set<String> posElements = graph.collectPosElements();
                        // System.out.println("All pos elements in Graph-" + (i + 1) + ": " + posElements);
                        // System.out.println();
                    }

                    // Print array of pos element counts
                    // System.out.println("Number of pos elements in each graph:");
                    // for (int i = 0; i < posElementCounts.length; i++) {
                    //     System.out.println("Graph-" + (i + 1) + ": " + posElementCounts[i]);
                    // }

                    // Calculate and print the metric sum
                    // double finalsum=0;
                    double sum = 0;
                    for (int i = 0; i < graphs.size(); i++) {
                        sum += calculate_d(graphs.get(i), pos.size(), posElementCounts[i]);
                    }
                    System.out.println("sum- :"+ sum);

                    if(String.valueOf(sum).equals("Infinity")){
                        sum=0.0;
                    }
                    
                    writer.write(targetSentenceNumber +" "+ sum + "\n");

                    finalsum+=sum;

                    }
                }
                writer.write("\n"+"Avg. value is: " + finalsum/2140.0+ "\n");

            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        }
    }
}