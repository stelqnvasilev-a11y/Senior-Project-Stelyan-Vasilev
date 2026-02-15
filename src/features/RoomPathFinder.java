package features;
import java.util.*;
public class RoomPathFinder {
    // a public method that MainBuildingRoomHelp will call.
    public static List<String> findFastestRouteInMainBuilding(String roomCode) {
        BuildingGraph g = buildDemoMainBuilding();
        Node start = g.getNode("ENT");              //entrance
        Node goal = g.getRoomNode(roomCode);        //end (room)
        if (goal == null){
            return List.of("I dont have this room in the map yet: "+ roomCode);
        }
    List<Node> path = aStar(g, start, goal); //the A* path
    }
    List<String>steps = new ArrayList<>();
    steps.add ("Fastest route to Room " + roomCode + ":");
    for (int i = 0; i<path.size(); i++){
        steps.add((i + 1) + ". " + path.get(i).label);
    }
    return steps;
}
private static BuildingGraph buildDemoMainBuilding() {
    BuildingGraph g = new BuildingGraph();
    // nodes
    Node ent  = new Node("ENT", "Entrance (Main Building)", 1, 0, 0);
    Node h1   = new Node("H1",  "Hallway 1",               1, 5, 0);
    Node h2   = new Node("H2",  "Hallway 2",               1, 10, 0);
    Node r4b  = new Node("R4B", "Room 4B door",            1, 10, -3);
    Node r101 = new Node("R101","Room 101 door",           1, 10, 3);

    g.addNode(ent); g.addNode(h1); g.addNode(h2); g.addNode(r4b); g.addNode(r101);

    // edges (cost = “distance/time”)
    g.addEdge(ent, h1, 5);
    g.addEdge(h1, h2, 5);
    g.addEdge(h2, r4b, 3);
    g.addEdge(h2, r101, 3);

    // room mapping: how user input is linked towards node
    g.mapRoom("4B", r4b);
    g.mapRoom("101", r101);

    return g;
}
private static List<Node> aStar(BuildingGraph g, Node start, Node goal) {

    Map<Node, Node> cameFrom = new HashMap<>();
    Map<Node, Double> gScore = new HashMap<>();
    Map<Node, Double> fScore = new HashMap<>();

    Comparator<Node> byF = Comparator.comparingDouble(n -> fScore.getOrDefault(n, Double.POSITIVE_INFINITY));
    PriorityQueue<Node> open = new PriorityQueue<>(byF);
    Set<Node> closed = new HashSet<>();

    gScore.put(Start, 0.0);
    fScore.put(Start, heuristic(start,goal));
    open.add(start);

    while (!open.isEmpty()) {
        Node current = open.poll();
        if (current.equals(goal)){
            return recostruct(cameFrom, current);
        }
        if (closed.contains(current)) continue;
        closed.add(current);
        for(Edge e : g.neighbours(current)) {
            Node neighbor = e.to;
            if (closed.contains(neighbor)) continue;

            double tentativeG = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + e.cost;

            if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeG);
                fScore.put(neighbor, tentativeG + heuristic(neighbor, goal));
                open.add(neighbor);
            }
        }
    }
    return List.of();
}