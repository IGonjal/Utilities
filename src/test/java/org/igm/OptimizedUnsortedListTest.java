package org.igm;

import org.igm.util.OptimizedUnsortedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OptimizedUnsortedListTest {

    OptimizedUnsortedList<Object> sut;

    @BeforeEach
    public void createList () {
        //given
        sut = new OptimizedUnsortedList<>();
    }

    @Test
    public void add() {
        //given
        String str = "whatever";
        Boolean bool = Boolean.TRUE;
        StringBuilder sb = new StringBuilder();

        //when
        sut.add(str);
        sut.add(bool);
        sut.add(sb);

        //then
        assertEquals(sut.size(), 3);
        assertTrue(sut.contains(str));
        assertTrue(sut.contains(bool));
        assertTrue(sut.contains(sb));
    }

    @Test
    public void removeOne() {
        //given
        String str = "whatever";
        Boolean bool = Boolean.TRUE;
        StringBuilder sb = new StringBuilder();
        sut.add(str);
        sut.add(bool);
        sut.add(sb);

        //when
        sut.remove(bool);

        //then
        assertEquals(sut.size(),2);
        assertFalse(sut.contains(bool));
        assertTrue(sut.contains(str));
        assertTrue(sut.contains(sb));

    }


    @Test
    public void addAll() {
        //Given
        String []aux = {"one", "two", "three"};
        List<String> addThis = Arrays.stream(aux).toList();

        //when
        sut.addAll(addThis);

        //then
        assertTrue(sut.containsAll(addThis));
        assertEquals(sut.size(),3);
        Iterator it = addThis.iterator();
        while(it.hasNext()){
            Object contained = it.next();
            assertTrue(sut.contains(contained));
        }
    }

    @Test
    public void clearTest() {
        //given
        String []aux = {"one", "two", "three"};
        List<String> addThis = Arrays.stream(aux).toList();
        sut.addAll(addThis);

        //when
        sut.removeAll(addThis);

        //then
        assertEquals(sut.size(),0);
    }

    @Test
    public void retainAllTest(){
        //given
        String []aux = {"one", "two", "three"};
        List<String> addThis = Arrays.stream(aux).toList();
        sut.addAll(addThis);
        String [] aux2 = {"one", "two"};

        //when
        sut.addAll(addThis);
        sut.retainAll(Arrays.stream(aux2).toList());

        //then
        assertEquals(sut.size(), 2);
    }

    @Test
    public void equalsTest() {
        //given
        String[] aux = {"one", "two", "three", "three"};
        String[] aux2 = {"three", "two", "one", "three"};
        String[] aux3 =  {"three", "two", "one"};
        OptimizedUnsortedList<Object> sut2 = new OptimizedUnsortedList<>();
        OptimizedUnsortedList<Object> sut3 = new OptimizedUnsortedList<>();
        StringBuilder stringBuilder = new StringBuilder();

        //when
        sut.addAll(Arrays.stream(aux).toList());
        sut2.addAll(Arrays.stream(aux2).toList());
        sut3.addAll(Arrays.stream(aux3).toList());

        //then
        assertTrue(sut.equals(sut2));
        assertFalse(sut.equals(stringBuilder));
        assertFalse(sut.equals(sut3));
    }

    @Test
    public void cloneTest() {
        //given
        String[] aux = {"one", "two", "three", "three"};
        sut.addAll(Arrays.stream(aux).toList());
        OptimizedUnsortedList<Object> other;

        //when
        other = sut.clone();

        //then
        assertTrue(other.equals(sut));
        assertFalse(sut == other);
    }

    @Test
    public void hashTest() {
        //given
        String[] aux = {"one", "two", "three", "three"};
        String[] aux2 = {"three", "two", "one", "three"};
        OptimizedUnsortedList optimizedUnsortedList = new OptimizedUnsortedList<>();

        //when
        optimizedUnsortedList.addAll(Arrays.stream(aux2).toList());
        sut.addAll(Arrays.stream(aux).toList());

        //then
        assertEquals(sut.hashCode(), optimizedUnsortedList.hashCode());
    }
}
