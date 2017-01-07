package me.blog.tastedroid.attendance.ranking;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Paging<T> {

    protected final List<T> list;
    protected final int num;
    protected final Class<T> clazz;

    public Paging(List<T> olist, Class<T> c, int perpage) {
        this.list = ImmutableList.copyOf(olist);
        this.num = perpage;
        this.clazz = c;
        init();
    }

    public List<T> getSource() {
        return list;
    }

    public int getNumbersPerPage() {
        return num;
    }

    protected void init() {
        arrays = new ArrayList<>();
        T[] array = (T[]) Array.newInstance(clazz, num);

        if (list.isEmpty()) {
            // have nothing to do.
        } else if (list.size() < num) {
            for (ListIterator<T> iter = list.listIterator(); iter.hasNext(); ) {
                int index = iter.nextIndex();
                array[index] = iter.next();
            }
            arrays.add(array);
        } else {
            int i = 0;
            for (T t : list) {
                if (num == i) {
                    i = 0;
                    arrays.add(array);
                    array = (T[]) Array.newInstance(clazz, num);
                }
                array[i++] = t;
            }
        }

        arrays = Collections.unmodifiableList(arrays);
    }

    protected List<T[]> arrays;

    public T[] getPage(int index) {
        return arrays.get(index);
    }

    public List<T[]> getArrays() {
        return arrays;
    }

    public int getPages() {
        return arrays.size();
    }
}
