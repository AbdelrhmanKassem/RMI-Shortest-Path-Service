package Server;

public interface Graph {
    public void addEdge(Integer start, Integer end);
    public void removeEdge(Integer start, Integer end);
    public Integer shortestPathLength(Integer start, Integer end);
}

