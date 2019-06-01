package dbconnection;

import models.Search;

public class SearchRepository<Search> implements IRepositoryFactory<Search,Search>
{

    @Override
    public Search Get(Search arg) {
        return null;
    }

    @Override
    public Search Insert(Search arg) {
        return null;
    }

    @Override
    public boolean Update(Search arg) {
        return false;
    }

    @Override
    public boolean Delete(Search arg) {
        return false;
    }

}