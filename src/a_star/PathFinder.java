package a_star;

import java.util.ArrayList;

/**
 * A-Star
 * Created by Connor Crawford on 12/9/15.
 */
public class PathFinder {

    private class VertexHeap {
        Vertex[] heap;
        private int lastIndex = 0;

        VertexHeap(int size) {
            heap = new Vertex[size];
        }

        public boolean isEmpty() {
            return heap[0] == null;
        }

        public boolean contains(Vertex vertex) {
            for (Vertex v : heap) {
                if (v == vertex) return true;
            }
            return false;
        }

        private int getParentIndex(int childIndex) {
            return (childIndex - 1) / 2;
        }

        public void insert(Vertex v) {
            if (lastIndex < heap.length) {
                heap[lastIndex] = v;
                int currentIndex = lastIndex, parentIndex = getParentIndex(currentIndex);
                lastIndex++;
                if (currentIndex != 0) {
                    // Sift up
                    while (heap[currentIndex].h_distance < heap[parentIndex].h_distance) {
                        Vertex temp = heap[parentIndex];
                        heap[parentIndex] = heap[currentIndex];
                        heap[currentIndex] = temp;
                        currentIndex = parentIndex;
                        parentIndex = getParentIndex(currentIndex);
                    }
                }
            } else
                System.out.println("Error: attempting to insert more elements than available. Please delete an element and try again.");
        }

        public void deleteMin() {
            if (isEmpty()) {
                System.out.println("Error: attempting to delete from an empty heap.");
                return;
            }
            try {
                heap[0] = heap[--lastIndex];
                if (lastIndex != 0) {
                    int parentIndex = 0, currentIndex, leftCIndex, rightCIndex;
                    // Sift down
                    do {
                        // Calculate the indices of the parent's children
                        leftCIndex = parentIndex * 2 + 1;
                        rightCIndex = parentIndex * 2 + 2;

                        // Figure out which whether to compare the left or right child or if complete
                        if (rightCIndex >= lastIndex) {
                            if (leftCIndex >= lastIndex)
                                return; // No more children, can return
                            currentIndex = leftCIndex; // No right child, left child only option
                        } else {
                            if (heap[leftCIndex].h_distance <= heap[rightCIndex].h_distance)
                                currentIndex = leftCIndex;
                            else
                                currentIndex = rightCIndex;
                        }
                        if (heap[parentIndex].h_distance > heap[currentIndex].h_distance) {
                            // Swap
                            Vertex temp = heap[currentIndex];
                            heap[currentIndex] = heap[parentIndex];
                            heap[parentIndex] = temp;
                            parentIndex = currentIndex;
                        } else
                            return;
                    } while (rightCIndex < lastIndex && leftCIndex < lastIndex);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: No path exists between selected vertices.");
                System.exit(-1);
            }
        }

        public Vertex popMin() {
            Vertex v = heap[0];
            deleteMin();
            return v;
        }

    }

    public Vertex[] findPath(Grid grid, Vertex start, Vertex end) {
        int dim = grid.getDimension();
        VertexHeap open = new VertexHeap(dim*dim);
        Vertex[][] cameFrom = new Vertex[dim][dim];
        Vertex current;
        open.insert(start);
        start.g_distance = 0;
        start.h_distance = calculateCost(start.getX(), start.getY(), end.getX(), end.getY());



        while (!open.isEmpty()) {
            current = open.popMin();
            current.isClosed = true;

            if (current == end) // Path has been found
                return getPath(cameFrom, start, end);

            for (Vertex neighbor : current.edges) {
                if (neighbor == null || neighbor.isClosed)
                    continue; // vertex has already been evaluated

                double dist = current.g_distance + calculateCost(current.getX(), current.getY(),
                        neighbor.getX(), neighbor.getY());
                if (!open.contains(neighbor))
                    open.insert(neighbor);

                if (dist >= neighbor.g_distance)
                    continue; // not a better path

                neighbor.g_distance = dist;
                neighbor.h_distance = dist + calculateCost(neighbor.getX(), neighbor.getY(), end.getX(), end.getY());
                cameFrom[neighbor.getRow()][neighbor.getCol()] = current;
            }
        }
        return null;
    }


    private Vertex[] getPath(Vertex[][] paths, Vertex start, Vertex end) {
        ArrayList<Vertex> totalPath = new ArrayList<>(1);
        Object[] out;
        Vertex current = end;
        while (current != start) {
            totalPath.add(current);
            current = paths[current.getRow()][current.getCol()];
        }
        totalPath.add(start);
        out = totalPath.toArray();
        return convertPath(out);
    }

    private Vertex[] convertPath(Object[] path) {
        try {
            int i = 0;
            Vertex[] p = new Vertex[path.length];
            for (Object o : path) {
                p[i] = (Vertex)o;
                i++;
            }
            return p;
        } catch (ClassCastException e) {
            return null;
        }
    }

    private double calculateCost(int w_x, int w_y, int v_x, int v_y) {
        double a = Math.sqrt(Math.pow(w_x-v_x, 2) + Math.pow(w_y-v_y, 2));
        return a;
    }

}
