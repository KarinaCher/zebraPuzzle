package zebrapuzzle.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import static zebrapuzzle.Application.HOUSE_POSITION_PROP;
import zebrapuzzle.condition.Condition;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class Street implements Serializable
{
    
    private LinkedList<House> houses = new LinkedList<>();
    private Map<String, Property> properties;
    
    public House getHouseByPosition(int index)
    {
        return houses.get(index-1);
    }
    
    public Street(int count, Set<Condition> conditions, String questionFile) throws NumberFormatException
    {
        properties = new HashMap<>();
        Map<String, Set<String>> initial = new HashMap<>();
        
        createPropertySet(conditions, initial, questionFile);
        initHousesProperties(initial, count);
        removeBoundaryConditions(conditions);
    }

    private void removeBoundaryConditions(Set<Condition> conditions)
    {
        for (Condition condition : conditions)
        {
            switch (condition.getHousePosition())
            {
                case NEXT:
                    houses.getFirst().rejectProperty(condition.getProp2(), condition.getValue2());
                    houses.getLast().rejectProperty(condition.getProp1(), condition.getValue1());
                    break;
            }
        }
    }

    private void initHousesProperties(Map<String, Set<String>> initial, int count)
    {
        for (String initKey : initial.keySet())
        {
            properties.put(initKey, new Property(initKey, initial.get(initKey)));
        }
        
        Set<String> position = new HashSet<>();
        for (int i = 1; i <= count; i++)
        {
            position.add(String.valueOf(i));
        }
        properties.put(HOUSE_POSITION_PROP, new Property(HOUSE_POSITION_PROP, position));
        
        for (int i = 1; i <= count; i++)
        {
            House house = new House(properties, String.valueOf(i));
            houses.add(house);
        }
    }

    private void createPropertySet(Set<Condition> conditions, Map<String, Set<String>> initial, String questionFile)
    {
        for (Condition condition : conditions)
        {
            if (!initial.containsKey(condition.getProp1()))
            {
                initial.put(condition.getProp1(), new HashSet<>());
            }
            initial.get(condition.getProp1()).add(condition.getValue1());
            
            if (!initial.containsKey(condition.getProp2()))
            {
                initial.put(condition.getProp2(), new HashSet<>());
            }
            initial.get(condition.getProp2()).add(condition.getValue2());
        }
        
        try (Stream<String> stream = Files.lines(Paths.get(questionFile)))
        {
            Iterator<String> iter = stream.iterator();
            while (iter.hasNext())
            {
                String[] cnd = iter.next().split(";");
                initial.get(cnd[0]).add(cnd[1]);
            }
            
        } catch (IOException e)
        {
        }
    }
    
    public House findHouseByProperty(String prop1, String value1)
    {
        Set<House> result = new HashSet();
        for (House house : houses)
        {
            if (house.getPropertyValue(prop1) != null && house.getPropertyValue(prop1).equals(value1))
            {
                result.add(house);
            }
        }
        
        return result.size() == 1 ? result.stream().findFirst().get() : null;
    }

    public boolean updateHouses(House house, String prop, String value)
    {
        if (house != null)
        {
            for (House h : houses)
            {
                if (h.getPropertyValue(HOUSE_POSITION_PROP).equals(house.getPropertyValue(HOUSE_POSITION_PROP)))
                {
                    h.approveProperty(prop, value);
                }
                else
                {
                    h.rejectProperty(prop, value);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param direction true = NEXT, false = PREV
     * @return 
     */
    public House findHouseByNeighbourProperty(boolean direction, String prop, String value)
    {
        Set<House> result = new HashSet();
        
        int start = direction ? 0 : 1;
        int end = direction ? houses.size() -1 : houses.size();
    
        for (int i = start; i < end; i++)
        {
            House house = houses.get(i);
            
            if (house.getPropertyValue(prop) != null && house.getPropertyValue(prop).equals(value))
            {
                if (direction)
                {
                    result.add(houses.get(i + 1));
                }
                else
                {
                    result.add(houses.get(i - 1));
                }
            }
        }
        
        return result.size() == 1 ? result.stream().findFirst().get() : null;
    }

    public void updateHouseNotFit(String prop1, String value1, String prop2, String value2)
    {
        for (House house : houses)
        {
            if (house.getPropertyValue(prop1) != null && !house.getPropertyValue(prop1).equals(value1))
            {
                house.rejectProperty(prop2, value2);
            }
            if (house.getPropertyValue(prop2) != null && !house.getPropertyValue(prop2).equals(value2))
            {
                house.rejectProperty(prop1, value1);
            }
        }
    }

    public void updateHouseNotFitNeighbourCondition(String prop1, String value1, String prop2, String value2)
    {

        for (int i = 1; i < houses.size() - 1; i++)
        {
            House house1 = houses.get(i);
            House house2 = houses.get(i + 1); 
            
            if (house1.getPropertyValue(prop1) != null && !house1.getPropertyValue(prop1).equals(value1))
            {
                house2.rejectProperty(prop2, value2);
            }
            if (house2.getPropertyValue(prop2) != null && !house2.getPropertyValue(prop2).equals(value2))
            {
                house1.rejectProperty(prop1, value1);
            }
        }
    }

    /**
     * When conditions are checked, refresh properties if is known.
     */
    public void refreshHousesProperties()
    {
        for (House house : houses)
        {
            for (String propertyName : properties.keySet())
            {
                if (house.getPropertyValue(propertyName) != null)
                {
                    updateHouses(house, propertyName, house.getPropertyValue(propertyName));
                }
            }
        }
        
    }

    public String writeHouseProp()
    {
        StringBuilder sb = new StringBuilder();
        
        for (House house : houses)
        {
            sb.append("<house ");
            properties.keySet().stream().forEach((propertyName) ->
            {
                sb.append(String.format("%s=\"%s\" ", propertyName, house.getPropertyValue(propertyName)));
            });
            sb.append("/>\n");
        }
        return sb.toString();
    }

    public boolean isResolved()
    {
        for (House house : houses)
        {
            for (String propertyName : properties.keySet())
            {
                if (house.getPropertyValue(propertyName) == null)
                {
                    return false;
                }
            }
        }
            
        return true;
    }

    public Map<String, Set<String>> returnAssumptions(int houseNum)
    {
        Map<String, Set<String>> result = new HashMap<>();
        House house = getHouseByPosition(houseNum);
        for (String propertyName : properties.keySet())
        {
            final Set<String> propAssumptions = house.getPropAssumptions(propertyName);
            if (!propAssumptions.isEmpty())
            {
                result.put(propertyName, propAssumptions);
            }
        }
        
        return result;
    }
    
    public Street copy()
    {
        Street obj = null;
        try
        {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos))
            {
                out.writeObject(this);
                out.flush();
            }

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = (Street) in.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
        }
        return obj;
    }
}
