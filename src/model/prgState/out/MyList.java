package model.prgState.out;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IMyList<T> {
    private List<T> list;
    public MyList() {
        list = new ArrayList<T>();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public void add(T v){
        list.add(v);
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
