package manager;

import model.Save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyMap = new HashMap<>();

    private Node first;
    private Node last;

    private static class Node {
        Save task;
        Node prev;
        Node next;

        Node(Node prev, Save task, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public void add(Save task) {
        if (task == null) {
            return;
        }
        remove(task.getId());

        Node newNode = new Node(last, task, null);
        if (last != null) {
            last.next = newNode;
        } else {
            first = newNode;
        }
        last = newNode;
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.remove(id);
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
    }

    @Override
    public List<Save> getHistory() {
        List<Save> result = new ArrayList<>();
        Node current = first;
        while (current != null) {
            result.add(current.task);
            current = current.next;
        }
        return result;
    }
}