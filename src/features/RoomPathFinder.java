package features;

import java.util.*;

//this is the main method which builds the map, runs A* and converts the path into readable steps.
public class RoomPathFinder {

    // Finds the route from entrance to a room
    public static List<String> findFastestRouteInMainBuilding(String roomCode) {
        // Build demo map of the building
        BuildingGraph graph = buildDemoMainBuilding();
        // Define start and goal nodes
        Node start = graph.getNode("ENT"); //the entrance node
        Node goal = graph.getRoomNode(roomCode); //the target room node

        if (goal == null) {
            return List.of("I don't have this room in the map yet: " + roomCode); // in case the room does not exist in the map
        }

        return buildReadableRoute(graph, start, goal, "Fastest route to Room " + roomCode + ":");
    }

    // new method to find the route from one room to another room inside the building
    public static List<String> findRouteBetweenRooms(String startRoomCode, String endRoomCode) {
        BuildingGraph graph = buildDemoMainBuilding();

        Node start = graph.getRoomNode(startRoomCode);
        Node goal = graph.getRoomNode(endRoomCode);

        if (start == null) {
            return List.of("I don't have the starting room in the map yet: " + startRoomCode);
        }

        if (goal == null) {
            return List.of("I don't have the destination room in the map yet: " + endRoomCode);
        }

        return buildReadableRoute(graph, start, goal,
                "Fastest route from Room " + startRoomCode + " to Room " + endRoomCode + ":");
    }

    private static List<String> buildReadableRoute(BuildingGraph graph, Node start, Node goal, String title) {
        List<Node> path = aStar(graph, start, goal);

        if (path.isEmpty()) {
            return List.of("No path was found.");
        }

        List<String> steps = new ArrayList<>();
        steps.add(title);

        for (int i = 0; i < path.size(); i++) {
            steps.add((i + 1) + ". " + path.get(i).label);
        }

        return steps;
    }

    // Builds a demo map of the Main Building
    private static BuildingGraph buildDemoMainBuilding() {
        BuildingGraph graph = new BuildingGraph();

        //The ground floor
        Node ent = new Node("ENT", "Entrance (Main Building)", 0, 0, 0);
        Node gfPreHall = new Node("GF_PRE", "Area before Main Hall", 0, 3, 0);
        Node r2 = new Node("R2", "Room 2 door", 0, 5, -2);

        Node gfHall = new Node("GF_HALL", "Main Hall", 0, 7, 0);
        Node gfStairsCentral = new Node("GF_SC", "Central Stairs (Floor 0)", 0, 10, 0);

        Node gfLeft = new Node("GF_LEFT", "Left Side of Main Hall", 0, 10, -3);
        Node r4a = new Node("R4A", "Room 4A door", 0, 12, -3);
        Node r4b = new Node("R4B", "Room 4B door", 0, 14, -3);

        Node gfRight = new Node("GF_RIGHT", "Right Side of Main Hall", 0, 10, 3);
        Node r9 = new Node("R9", "Room 9 door", 0, 12, 3);

        //The first Floor
        Node f1StairsCentral = new Node("F1_SC", "Central Stairs (Floor 1)", 1, 10, 0);
        Node f1Hall = new Node("F1_HALL", "Main Hall (Floor 1)", 1, 10, 2);

        Node f1Left = new Node("F1_LEFT", "Left Hallway", 1, 6, 2);
        Node r110 = new Node("R110", "Room 110 door", 1, 4, 3);
        Node r111 = new Node("R111", "Room 111 door", 1, 4, 5);
        Node r112 = new Node("R112", "Room 112 door", 1, 4, 7);

        Node f1Right = new Node("F1_RIGHT", "Right Hallway", 1, 14, 2);
        Node r101b = new Node("R101B", "Room 101B door", 1, 14, 1);
        Node r101 = new Node("R101", "Room 101 door", 1, 14, -1);
        Node r116 = new Node("R116", "Room 116 Computer Lab door", 1, 16, 4);

        // Nodes for the ground floor
        graph.addNode(ent);
        graph.addNode(gfPreHall);
        graph.addNode(r2);
        graph.addNode(gfHall);
        graph.addNode(gfStairsCentral);
        graph.addNode(gfLeft);
        graph.addNode(r4a);
        graph.addNode(r4b);
        graph.addNode(gfRight);
        graph.addNode(r9);

        // Nodes for the first floor
        graph.addNode(f1StairsCentral);
        graph.addNode(f1Hall);
        graph.addNode(f1Left);
        graph.addNode(r110);
        graph.addNode(r111);
        graph.addNode(r112);
        graph.addNode(f1Right);
        graph.addNode(r101b);
        graph.addNode(r101);
        graph.addNode(r116);

        //Ground floor connections
        graph.addEdge(ent, gfPreHall, 3);
        graph.addEdge(gfPreHall, r2, 2);
        graph.addEdge(gfPreHall, gfHall, 4);
        graph.addEdge(gfHall, gfStairsCentral, 3);
        graph.addEdge(gfHall, gfLeft, 2);
        graph.addEdge(gfHall, gfRight, 2);
        graph.addEdge(gfLeft, r4a, 2);
        graph.addEdge(gfLeft, r4b, 3);
        graph.addEdge(gfRight, r9, 2);
        //Stairs between 0 and 1 floor
        graph.addEdge(gfStairsCentral, f1StairsCentral, 6);

        //First floor connections
        graph.addEdge(f1StairsCentral, f1Hall, 2);
        graph.addEdge(f1Hall, f1Left, 3);
        graph.addEdge(f1Hall, f1Right, 3);
        graph.addEdge(f1Left, r110, 2);
        graph.addEdge(f1Left, r111, 3);
        graph.addEdge(f1Left, r112, 4);
        graph.addEdge(f1Right, r101b, 2);
        graph.addEdge(f1Right, r101, 3);
        graph.addEdge(f1Right, r116, 4);

        // Ground floor rooms
        graph.mapRoom("2", r2);
        graph.mapRoom("4A", r4a);
        graph.mapRoom("4B", r4b);
        graph.mapRoom("9", r9);

        // First floor rooms
        graph.mapRoom("101", r101);
        graph.mapRoom("101B", r101b);
        graph.mapRoom("110", r110);
        graph.mapRoom("111", r111);
        graph.mapRoom("112", r112);
        graph.mapRoom("116", r116);

        return graph;
    }

    // A* algorithm
    private static List<Node> aStar(BuildingGraph graph, Node start, Node goal) {
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();

        PriorityQueue<Node> open = new PriorityQueue<>(
                Comparator.comparingDouble(n -> fScore.getOrDefault(n, Double.POSITIVE_INFINITY))
        );

        Set<Node> closed = new HashSet<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        open.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.equals(goal)) {
                return reconstruct(cameFrom, current);
            }

            if (closed.contains(current)) {
                continue;
            }
            closed.add(current);

            for (Edge edge : graph.neighbors(current)) {
                Node neighbor = edge.to;

                if (closed.contains(neighbor)) {
                    continue;
                }

                double tentativeG =
                        gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + edge.cost;

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);

                    double estimatedTotal = tentativeG + heuristic(neighbor, goal);
                    fScore.put(neighbor, estimatedTotal);

                    open.add(neighbor);
                }
            }
        }

        return List.of();
    }

    // Reconstructs final path
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

    // Heuristic function
    private static double heuristic(Node a, Node b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;

        double distance2D = Math.sqrt(dx * dx + dy * dy);

        double minPerFloor = 10.0;
        double floorPenalty = Math.abs(a.floor - b.floor) * minPerFloor;

        return distance2D + floorPenalty;
    }

    // Graph structure
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
            if (code == null) {
                return null;
            }
            return roomMap.get(code.toUpperCase());
        }
    }

    private static class Node {
        final String id;
        final String label;
        final int floor;
        final double x;
        final double y;

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