package dbconnection;

public interface IRepositoryFactory<T,U>
{
     T Get(U arg);
     T Insert(U arg);
     boolean Update(U arg);
     boolean Delete(U arg);
}