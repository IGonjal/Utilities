package org.igm.util;

import java.util.*;

/**
 * This class mantains an unsorted list that has insertion cost O(1 + O(log(m)) being m the number of occurrences of a
 * duplicated element when inserted, O(1) on delete, O(1) on access, does not mantain order and allows duplicated
 * elements. The counterpart of this list is consuming O(2n) space
 *
 * @param <T>
 */
public class OptimizedUnsortedList<T> implements Collection<T> {

    private ArrayList<T> list;
    private HashMap<T, PriorityQueue<Integer>> map;

    public OptimizedUnsortedList(){
        this.list = new ArrayList<>();
        this.map = new HashMap<>();
    }


    @Override
    public boolean add(T t) {
        if (map.containsKey(t)) {
            map.get(t).add(list.size());
        } else {
            PriorityQueue<Integer> set = new PriorityQueue<>(Collections.reverseOrder());
            set.add(list.size());
            map.put(t, set);
        }
        return list.add(t);
    }

    @Override
    public boolean remove(Object o) {
        if(!this.contains(o)) {
            return false;
        }

        PriorityQueue<Integer> charIndexes = map.get(o);

        T last = list.get(list.size() - 1); //get last index
        list.set(charIndexes.peek(), last); // replace the char to delete for the last one in the list

        PriorityQueue<Integer> swap = map.get(last); //get the last letter position
        swap.remove();

        if (swap != charIndexes) { //if the object to swap is the same than the removed
            swap.add(charIndexes.peek()); //set the new position (THIS IS THE HEAVIEST OPERATION O(log n)
            map.put(last, swap); // fix the object whe just modified position
            charIndexes.remove(); //remove the deleted object from the indexes
        }

        list.remove(list.size() - 1); //delete last object as it has been copied
        if (charIndexes.size() == 0) {
            map.remove(o); //deleted the object in the map
        }

        return true;
    }


    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty(){
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean success = true;
        for(T element: c){
            success &= this.add(element);
        }
        return success;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean success = true;
        for(Object o : c){
            success &= this.remove(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ArrayList<T> list = new ArrayList();
        for(Object o: c) {
            if(contains(o)){
                list.add((T) o);
            }
        }
        this.clear();
        return this.addAll(list);
    }
    @Override
    public void clear() {
        this.list = new ArrayList<>();
        this.map = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        // Creates a second list and deletes the elements one by one, when finished, if the elements
        // didn't match or if the list contains more than the original, they weren't equals. The
        // cost in the worst case scenario is O(2n)
        if(o instanceof OptimizedUnsortedList<?>) {
            OptimizedUnsortedList other =((OptimizedUnsortedList) o).clone();

            Iterator it = this.iterator();

            while(it.hasNext()) {
                Object next = it.next();
                if(other.contains(next)){
                    other.remove(next);
                } else {
                    return false;
                }
            }
            return other.isEmpty();
        }
        return false;
    }

    public OptimizedUnsortedList clone() {
        OptimizedUnsortedList<T> clone = new OptimizedUnsortedList<>();
        Iterator it = this.iterator();

        while (it.hasNext()){
            clone.add((T) it.next());
        }
        return clone;
    }


    @Override
    public int hashCode() {
        List<T> list = (List<T>) this.list.clone();
        Collections.sort(list, Collections.reverseOrder());
        return list.hashCode();
    }
}


