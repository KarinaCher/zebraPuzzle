package zebrapuzzle.model;

import java.io.Serializable;
import static zebrapuzzle.Application.HOUSE_POSITION_PROP;
import zebrapuzzle.condition.Relevance;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class House implements Serializable
{
    private Map<String, Property> properties = new HashMap<>();
    
    public House(Map<String, Property> prop, String position)
    {
        for (String key : prop.keySet())
        {
            properties.put(key, new Property(key, prop.get(key).getValues()));
        }
        properties.get(HOUSE_POSITION_PROP).setValueRelevance(position, Relevance.YES);
    }

    public void approveProperty(String prop, String value)
    {
        properties.get(prop).setValueRelevance(value, Relevance.YES);
    }
    
    public void rejectProperty(String prop, String value)
    {
        properties.get(prop).setValueRelevance(value, Relevance.NO);
    }
    
    public String getPropertyValue(String propertyName)
    {
        return properties.get(propertyName).getValue();
    }
    
    public Set<String> getPropAssumptions(String propertyName)
    {
        return properties.get(propertyName).getAssumptions().get();
    }

}
