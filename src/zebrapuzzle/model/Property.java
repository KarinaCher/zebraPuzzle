package zebrapuzzle.model;

import java.io.Serializable;
import zebrapuzzle.condition.Relevance;
import static zebrapuzzle.condition.Relevance.MAY_BE;
import static zebrapuzzle.condition.Relevance.NO;
import static zebrapuzzle.condition.Relevance.YES;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class Property implements Serializable
{
    private String name;
    private Map<String, Relevance> values = new HashMap<>();

    public Property(String name, Set<String> values)
    {
        this.name = name;
        values.forEach((v) -> this.values.put(v, Relevance.MAY_BE));
    }

    public String getName()
    {
        return name;
    }

    public Set<String> getValues()
    {
        return values.keySet();
    }
    
    public void setValueRelevance(String value, Relevance rel)
    {
        if (values.containsKey(value))
        {
            switch(rel)
            {
                case YES:
                    values.keySet().stream().forEach((key) ->
                    {
                        values.put(key, key.equals(value) ? YES : NO);
                    });
                    break;
                    
                case NO:
                    if (values.get(value) == MAY_BE || values.get(value) == NO)
                    {
                        values.put(value, NO);
                    }
                    else
                    {
                        throw new IllegalArgumentException("YES can't be set to NO");
                    }
                    break;
                    
                case MAY_BE:
                    throw new IllegalArgumentException("MAY_BE not set");
            }
        }
        else
        {
            throw new IllegalArgumentException("No such values for property " + name);
        }
    }
    
    public String getValue()
    {
        if (values.values().stream().filter((p) -> p == YES).count() == 1
                || values.values().stream().filter((p) -> p == NO).count() == values.size() - 1)
        {
            for (String s : values.keySet())
            {
                if (values.get(s) == YES || values.get(s) == MAY_BE)
                {
                    return s;
                }
            }
        }
        
        return null;
    }
    
    public Optional<Set<String>> getAssumptions()
    {
        Set<String> result = new HashSet();
        values.keySet().stream().filter((key) -> (values.get(key) == MAY_BE)).forEach((key) ->
        {
            result.add(key);
        });
        return Optional.of(result);
    }
}
