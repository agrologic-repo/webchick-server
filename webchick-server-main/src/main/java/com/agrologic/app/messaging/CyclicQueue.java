
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.messaging;



import java.util.LinkedList;

/**
 * Title: CyclicQueue <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.0 <br>
 */
public class CyclicQueue<RequestMessage> {
    private boolean                          cycleCompleted = false;
    private int                              head;
    private int                              next;
    private final LinkedList<RequestMessage> queue;
    private int                              tail;

    /**
     * Construct a CyclicQueue with
     * default list of requests.
     */
    public CyclicQueue() {
        this.head  = 0;
        this.next  = 0;
        this.tail  = 0;
        this.queue = new LinkedList<RequestMessage>();
    }

    /**
     * Inserts the specified <tt>Message</tt> element at the end of this list.
     * @param e the element to add
     * @return {@code true} (as specified by {@link LinkedList#add})
     */
    public boolean add(RequestMessage e) {
        if (queue.isEmpty()) {
            head = 0;
            next = 0;
            tail = 0;
        } else {
            tail++;
        }

        return queue.add(e);
    }

    /**
     * This method works as specified by {@link LinkedList#remove(Object e)}.
     * @param e element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean remove(RequestMessage e) {
        if (queue.isEmpty()) {
            return false;
        }

        tail--;

        return queue.remove(e);
    }

    /**
     * Returns the nextElem element in the list.
     * @return the nextElem element in the list.
     * @exception IllegalAccessException if the iteration has no nextElem element.
     */
    public RequestMessage nextElem() throws IllegalAccessException {
        if (queue.isEmpty()) {
            throw new IllegalAccessException("Illegal access. The list is empty.");
        }

        if (next > tail) {
            next = head;

//          cycleCompleted = false;
        }

        if (next == tail) {
            cycleCompleted = true;
        }

        return queue.get(next++);
    }

    /**
     * Return true when all elements was passed .
     * @return true if cycle was completed.
     */
    public boolean isCycleCompleted() {
        return cycleCompleted;
    }

    /**
     * Reset cycle complete flag.
     */
    public void resetCycleFlag() {
        cycleCompleted = false;
    }

    /**
     * Clear queue .
     */
    public void clear() {
        queue.clear();
    }
}



