package com.qf.Algorithm;

public class ListCode  {
    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        node.next.next.next.next = new ListNode(5);
        removeNthFromEnd(node, 3);
        while (node!=null) {
            System.out.print(node.val + "\t");
            node = node.next;
        }
    }
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode first = new ListNode(0);
        first.next = head;
        ListNode node1 = first;
        ListNode node2 = first;
        int i = 0;
        while (i<n) {
            node1 = node1.next;
            i++;
        }
        while (node1.next!=null) {
            node1 = node1.next;
            node2 = node2.next;
        }
        node2.next = node2.next.next;
        return first.next;

    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }
}
