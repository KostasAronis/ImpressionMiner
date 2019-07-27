package dbconnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Search;
import models.TargetWebsite;
import models.Word;
import models.Work;

public class RepositoryFactory
{
     private static Map<Class, Class> repoRegistry = new HashMap<>();

     public static <T> IRepository<T> GetRepository(Class<T> classType) throws InstantiationException, IllegalAccessException
     {
          RegisterRepositories();
          if(repoRegistry.containsKey(classType))
          {
               var repositoryType = repoRegistry.get(classType);
               return (IRepository<T>) repositoryType.newInstance();
          }
          else
          {
               throw new UnsupportedOperationException("Class was not registered");
          }
     }

     private static void RegisterRepositories()
     {
          repoRegistry.put(Work.class, WorkRepository.class);
          repoRegistry.put(Word.class, WordRepository.class);
          repoRegistry.put(TargetWebsite.class, TargetWebsiteRepository.class);
          repoRegistry.put(Search.class, SearchRepository.class);
     }
}