package zebrapuzzle;

import zebrapuzzle.condition.Condition;
import zebrapuzzle.condition.HousePosition;
import zebrapuzzle.model.House;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import zebrapuzzle.model.Street;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class Application
{
    public static final String HOUSE_POSITION_PROP = "position";
    
    public static void main(String[] args) throws IOException
    {
        int housesCount = Integer.parseInt(args[0]);
        final String question = args[2];
        
        Set<Condition> conditions = readIn(args[1]);
        Street street = new Street(housesCount, conditions, question);
        
        // 1st step
        checkConditions(conditions, street);
        
        if (street.isResolved())
        {
            dataOut(street);
        }
        
        // 2nd step
        for (int i = 1; i <= housesCount; i++)
        {
            Map<String, Set<String>> assumptions = street.returnAssumptions(i);
            if (assumptions.isEmpty())
                continue;

            for (String assumpKey : assumptions.keySet())
            {
                for (String assumpValue : assumptions.get(assumpKey))
                {
                    Street newStreet = street.copy();
                    // create assumption
                    newStreet.getHouseByPosition(i).approveProperty(assumpKey, assumpValue);
                    
                    try 
                    {
                        checkConditions(conditions, newStreet);
                    } 
                    catch (IllegalArgumentException ex)
                    {
                        // So, this situation is impossible, continue with another assumption
                        continue;
                    }
                    if (newStreet.isResolved())
                    {
                        dataOut(newStreet);
                    }
                }
            }
        }
    }

    private static void checkConditions(Set<Condition> conditions, Street street) throws IOException
    {
        int i = 0;

        while (i < conditions.size())
        {
            for (Condition condition : conditions)
            {
                House house;
                switch (condition.getHousePosition())
                {
                    case SAME:
                        house = street.findHouseByProperty(condition.getProp1(), condition.getValue1());
                        street.updateHouses(house, condition.getProp2(), condition.getValue2());
                        
                        house = street.findHouseByProperty(condition.getProp2(), condition.getValue2());
                        street.updateHouses(house, condition.getProp1(), condition.getValue1());
                        
                        street.updateHouseNotFit(condition.getProp1(), condition.getValue1(), condition.getProp2(), condition.getValue2());
                        break;
                        
                    case NEXT:
                        house = street.findHouseByNeighbourProperty(true, condition.getProp1(), condition.getValue1());
                        street.updateHouses(house, condition.getProp2(), condition.getValue2());

                        house = street.findHouseByNeighbourProperty(false, condition.getProp2(), condition.getValue2());
                        street.updateHouses(house, condition.getProp1(), condition.getValue1());
                        
                        street.updateHouseNotFitNeighbourCondition(
                                condition.getProp1(), condition.getValue1(), condition.getProp2(), condition.getValue2());
                        break;
                }
                street.refreshHousesProperties();
            }
            
            i++;
        }
    }

    private static void dataOut(Street street) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<result>\n");
        sb.append(street.writeHouseProp());
        sb.append("</result>");
        
        Files.write(Paths.get("output.xml"), sb.toString().getBytes(), StandardOpenOption.CREATE);
    }

    static Set<Condition> readIn(String fileName)
    {
        Set<Condition> conditions = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            Iterator<String> iter = stream.iterator();
            while (iter.hasNext())
            {
                String[] cnd = iter.next().split(";");
                conditions.add(new Condition(HousePosition.valueOf(cnd[0]), cnd[1], cnd[2], cnd[3], cnd[4]));
            }

        } catch (IOException e)
        {
        }
        
        return conditions;
    }
}
