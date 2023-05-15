package Server;
import java.util.*;
public class BiBFSGraph implements Graph {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    Map<Integer, List<Integer>> reverseGraph = new HashMap<>();

    public void addEdge(Integer start, Integer end){
        if(!graph.containsKey(start))
            graph.put(start, new LinkedList<Integer>());
        if(!reverseGraph.containsKey(end))
            reverseGraph.put(end, new LinkedList<Integer>());
        graph.get(start).add(end);
        reverseGraph.get(end).add(start);
    }

    public void removeEdge(Integer start, Integer end){
        if(graph.containsKey(start))
            graph.get(start).remove(end);
        if(reverseGraph.containsKey(end))
            reverseGraph.get(end).remove(start);
    }

    public Integer shortestPathLength(Integer start, Integer end) {
        if(start == end)
            return 0;
        int dist = 0;
        Queue<Integer> queue = new LinkedList<>();
        Queue<Integer> revQueue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> revVisited = new HashSet<>();
        Set<Integer> allVisited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        revQueue.add(end);
        revVisited.add(end);
        while(!queue.isEmpty() && !revQueue.isEmpty()){
            int size = queue.size();
            while(size-->0){
                Integer curr = queue.remove();
                if(allVisited.contains(curr))
                    return dist + dist - 1;
                allVisited.add(curr);
                if(graph.containsKey(curr)){
                    for(Integer val: graph.get(curr))
                        if(!visited.contains(val)){
                            queue.add(val);
                            visited.add(val);
                        }
                }
            }
            size = revQueue.size();
            while(size-->0){
                Integer curr = revQueue.remove();
                if(allVisited.contains(curr))
                    return dist * 2;
                allVisited.add(curr);
                if(reverseGraph.containsKey(curr)){
                    for(Integer val: reverseGraph.get(curr))
                        if(!revVisited.contains(val)){
                            revQueue.add(val);
                            revVisited.add(val);
                        }
                }
            }
            dist++;
        }
        return -1;
    }
}
