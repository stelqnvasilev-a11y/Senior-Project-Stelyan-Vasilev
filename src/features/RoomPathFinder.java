package features;

import java.util.*;

public class RoomPathFinder {


     //This is the main method called by MainBuildingRoomHelp.
     //It builds the map, runs A* and converts the path into readable steps.

    public static List<String> findFastestRouteInMainBuilding(String roomCode) {

        // Build demo map of the building
        BuildingGraph graph = buildDemoMainBuilding();

        // Define start and goal nodes
        Node start = graph.getNode("ENT");        // Entrance node
        Node goal  = graph.getRoomNode(roomCode); // Target room node

        //If the room does not exist in the map
        if (goal == null) {
            return List.of("I don't have this room in the map yet: " + roomCode);
        }

        //Run A* algorithm
        List<Node> path = aStar(graph, start, goal);

        //If no path was found
        if (path.isEmpty()) {
            return List.of("I couldn't find a path to this room: " + roomCode);
        }

        //Converts path into readable text steps
        List<String> steps = new ArrayList<>();
        steps.add("Fastest route to Room " + roomCode + ":");

        for (int i = 0; i < path.size(); i++) {
            steps.add((i + 1) + ". " + path.get(i).label);
        }

        return steps;
    }


     //Builds a small demo map of the Main Building.
     //Later this can be expanded with more rooms and floors.

    private static BuildingGraph buildDemoMainBuilding() {

        BuildingGraph graph = new BuildingGraph();

        // Create nodes (id, label, floor, x, y)
        Node ent  = new Node("ENT",  "Entrance (Main Building)", 1, 0, 0);
        Node h1   = new Node("H1",   "Hallway 1",               1, 5, 0);
        Node h2   = new Node("H2",   "Hallway 2",               1, 10, 0);
        Node r4b  = new Node("R4B",  "Room 4B door",            1, 10, -3);
        Node r101 = new Node("R101", "Room 101 door",           1, 10, 3);
        Node Red_Room = new Node("RedRoom","RedRoom door", 4, 3, -10);

        //Add nodes to graph
        graph.addNode(ent);
        graph.addNode(h1);
        graph.addNode(h2);
        graph.addNode(r4b);
        graph.addNode(r101);
        graph.addNode(Red_Room);

        //Connect nodes (bidirectional edges)
        graph.addEdge(ent, h1, 5);
        graph.addEdge(h1, h2, 5);
        graph.addEdge(h2, r4b, 3);
        graph.addEdge(h2, r101, 3);

        // Map room codes to room nodes
        graph.mapRoom("4B", r4b);
        graph.mapRoom("101", r101);

        return graph;
    }


     // A* algorithm implementation.
     // Finds the lowest-cost path between start and goal.

    private static List<Node> aStar(BuildingGraph graph, Node start, Node goal) {

        //Stores the best previous node for each node
        Map<Node, Node> cameFrom = new HashMap<>();

        //Real cost from start to each node
        Map<Node, Double> gScore = new HashMap<>();

        //Estimated total cost (gScore + heuristic)
        Map<Node, Double> fScore = new HashMap<>();

        //Priority queue ordered by lowest fScore
        PriorityQueue<Node> open = new PriorityQueue<>(
                Comparator.comparingDouble(n -> fScore.getOrDefault(n, Double.POSITIVE_INFINITY))
        );

        //Set of processed nodes
        Set<Node> closed = new HashSet<>();

        // Initialize start node
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        open.add(start);

        // Main A* loop
        while (!open.isEmpty()) {

            Node current = open.poll();

            // If goal is reached, reconstruct path
            if (current.equals(goal)) {
                return reconstruct(cameFrom, current);
            }

            if (closed.contains(current)) continue;
            closed.add(current);

            // Explore neighbors
            for (Edge edge : graph.neighbors(current)) {

                Node neighbor = edge.to;

                if (closed.contains(neighbor)) continue;

                // Calculate new possible cost
                double tentativeG =
                        gScore.getOrDefault(current, Double.POSITIVE_INFINITY)
                                + edge.cost;

                // If this path is better than previous one
                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {

                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);

                    double estimatedTotal =
                            tentativeG + heuristic(neighbor, goal);

                    fScore.put(neighbor, estimatedTotal);

                    open.add(neighbor);
                }
            }
        }

        // No path found
        return List.of();
    }

    /*
     * Reconstructs final path by walking backwards from goal.
     */
    private static List<Node> reconstruct(Map<Node, Node> cameFrom, Node goal) {

        LinkedList<Node> path = new LinkedList<>();
        Node current = goal;

        path.addFirst(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.addFirst(current);
        }

        return path;
    }


      //Heuristic function:
     //Uses Euclidean distance + minimal floor change penalty.

    private static double heuristic(Node a, Node b) {

        double dx = a.x - b.x;
        double dy = a.y - b.y;

        double distance2D = Math.sqrt(dx * dx + dy * dy);

        double minPerFloor = 10.0;
        double floorPenalty =
                Math.abs(a.floor - b.floor) * minPerFloor;

        return distance2D + floorPenalty;
    }

    //the GRAPH STRUCTURE

    private static class BuildingGraph {

        private final Map<Node, List<Edge>> adjacency = new HashMap<>();
        private final Map<String, Node> roomMap = new HashMap<>();
        private final Map<String, Node> idMap = new HashMap<>();

        void addNode(Node node) {
            adjacency.putIfAbsent(node, new ArrayList<>());
            idMap.put(node.id, node);
        }

        void addEdge(Node a, Node b, double cost) {
            adjacency.get(a).add(new Edge(b, cost));
            adjacency.get(b).add(new Edge(a, cost));
        }

        List<Edge> neighbors(Node node) {
            return adjacency.getOrDefault(node, List.of());
        }

        Node getNode(String id) {
            return idMap.get(id);
        }

        void mapRoom(String code, Node node) {
            roomMap.put(code.toUpperCase(), node);
        }

        Node getRoomNode(String code) {
            if (code == null) return null;
            return roomMap.get(code.toUpperCase());
        }
    }

    private static class Node {

        final String id;
        final String label;
        final int floor;
        final double x, y;

        Node(String id, String label, int floor, double x, double y) {
            this.id = id;
            this.label = label;
            this.floor = floor;
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private static class Edge {

        final Node to;
        final double cost;

        Edge(Node to, double cost) {
            this.to = to;
            this.cost = cost;
        }
    }
}