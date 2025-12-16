package model.prgState.out;
import java.util.List;

public interface IMyList<T> {
    void add(T value);
    List<T> getList();
}
