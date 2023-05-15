package Server;
import java.util.*;
public class BFSGraph implements Graph {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    public void addEdge(Integer start, Integer end){
        if(!graph.containsKey(start))
            graph.put(start, new LinkedList<Integer>());
        graph.get(start).add(end);
    }
    public void removeEdge(Integer start, Integer end){
        if(graph.containsKey(start))
            graph.get(start).remove(end);
    }
    public Integer shortestPathLength(Integer start, Integer end){
        if(start == end)
            return 0;
        int dist = 0;
        boolean found = false;
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(start);
        while(!queue.isEmpty()){
            int size = queue.size();
            while(size-->0){
                Integer curr = queue.remove();
                if(visited.contains(curr))
                    continue;
                visited.add(curr);
                if(curr.equals(end)){
                    found = true;
                    break;
                }
                if(graph.containsKey(curr)){
                    for(Integer val: graph.get(curr))
                        if(!visited.contains(val))
                            queue.add(val);
                }
            }
            if(found)
                break;
            dist++;
        }
        if(found)
            return dist;
        return -1;
    }
}

