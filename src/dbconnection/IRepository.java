package dbconnection;

import java.util.List;

public interface IRepository<T>
{
    List<T> GetAll();
    T GetById(Integer id);
    T Insert(T arg);
    boolean Update(T arg);
    boolean Delete(T arg);
}