package zebrapuzzle.model;

import zebrapuzzle.model.House;
import zebrapuzzle.model.Street;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static zebrapuzzle.Application.HOUSE_POSITION_PROP;
import zebrapuzzle.condition.Condition;
import zebrapuzzle.condition.HousePosition;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class StreetTest
{
    private String fileName, questionFile;
    private String count;
    private Set<Condition> conditions;
    private Street street;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
        fileName = "inputFile.txt";
        questionFile = "question.txt";
        count = "5";
        conditions = new HashSet<>();
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
        
        street = new Street(Integer.parseInt(count), conditions, questionFile);
    }

    @Test
    public void testFindHouseByProperty()
    {
        street.getHouseByPosition(3).approveProperty("color", "Green");
        House house = street.findHouseByProperty("color", "Green");
        assertEquals("3", house.getPropertyValue(HOUSE_POSITION_PROP));
    }

    @Test
    public void testUpdateHouses()
    {
        House house = street.findHouseByProperty(HOUSE_POSITION_PROP, "2");
        street.updateHouses(house, "color", "Ivory");
        assertEquals("Ivory", street.getHouseByPosition(2).getPropertyValue("color"));
        assertEquals(null, street.getHouseByPosition(1).getPropertyValue("color"));
        assertEquals(null, street.getHouseByPosition(3).getPropertyValue("color"));
        assertEquals(null, street.getHouseByPosition(4).getPropertyValue("color"));
        assertEquals(null, street.getHouseByPosition(5).getPropertyValue("color"));
    }

    @Test
    public void testFindHouseByLeftNeighbourPropertyLeft()
    {
        House house = street.findHouseByNeighbourProperty(false, HOUSE_POSITION_PROP, "3");
        assertEquals("2", house.getPropertyValue(HOUSE_POSITION_PROP));
    }
    
    @Test
    public void testFindHouseByLeftNeighbourPropertyNext()
    {
        House house = street.findHouseByNeighbourProperty(true, HOUSE_POSITION_PROP, "3");
        assertEquals("4", house.getPropertyValue(HOUSE_POSITION_PROP));
    }

    @Test
    public void testFindHouseByNeighbourProperty()
    {
        street.getHouseByPosition(3).approveProperty("color", "Green");
        House house = street.findHouseByNeighbourProperty(true, "color", "Green");
        assertEquals("4", house.getPropertyValue(HOUSE_POSITION_PROP));
        
        street.getHouseByPosition(3).approveProperty("color", "Green");
        house = street.findHouseByNeighbourProperty(false, "color", "Green");
        assertEquals("2", house.getPropertyValue(HOUSE_POSITION_PROP));
    }

    @Test
    public void testUpdateHouseNotFit()
    {
        House house = street.getHouseByPosition(3);
        house.approveProperty("color", "Green");
        house.rejectProperty("pet", "Snails");
        house.rejectProperty("pet", "Horse");
        house.rejectProperty("pet", "Zebra");
        street.updateHouseNotFit("color", "Blue", "pet", "Dog");
        assertEquals("Fox", house.getPropertyValue("pet"));
        
        house = street.getHouseByPosition(4);
        house.approveProperty("color", "Ivory");
        house.rejectProperty("pet", "Snails");
        house.rejectProperty("pet", "Horse");
        house.rejectProperty("pet", "Zebra");
        street.updateHouseNotFit("pet", "Dog", "color", "Blue");
        assertEquals("Fox", house.getPropertyValue("pet"));
    }

    @Test
    public void testUpdateHouseNotFitNeighbourCondition()
    {
        House house = street.getHouseByPosition(3);
        house.rejectProperty("pet", "Snails");
        house.rejectProperty("pet", "Horse");
        house.rejectProperty("pet", "Zebra");
        House next = street.getHouseByPosition(4);
        next.approveProperty("color", "Green");
        street.updateHouseNotFitNeighbourCondition("pet", "Dog", "color", "Blue");
        assertEquals("Fox", house.getPropertyValue("pet"));
        
    }

    @Test
    public void testReturnAssumptions()
    {
        House house = street.getHouseByPosition(3);
        house.rejectProperty("color", "Ivory");
        house.rejectProperty("color", "Red");
        house.rejectProperty("color", "Green");
        
        Collection<String> expected = new HashSet<>();
        expected.add("Blue");
        expected.add("Yellow");
        assertEquals(expected, street.returnAssumptions(3).get("color"));
    }


    @Test
    public void testIsResolved()
    {
        House house1 = street.getHouseByPosition(1);
        house1.approveProperty("color", "Red");
        house1.approveProperty("nationality", "English");
        house1.approveProperty("drink", "Coffee");
        house1.approveProperty("pet", "Dog");
        house1.approveProperty("smoke", "Old gold");
        House house2 = street.getHouseByPosition(2);
        house2.approveProperty("color", "Green");
        house2.approveProperty("nationality", "Spaniard");
        house2.approveProperty("drink", "Tea");
        house2.approveProperty("pet", "Snails");
        house2.approveProperty("smoke", "Kools");
        House house3 = street.getHouseByPosition(3);
        house3.approveProperty("color", "Ivory");
        house3.approveProperty("nationality", "Ukrainian");
        house3.approveProperty("drink", "Milk");
        house3.approveProperty("pet", "Fox");
        house3.approveProperty("smoke", "Chesterfields");
        House house4 = street.getHouseByPosition(4);
        house4.approveProperty("color", "Yellow");
        house4.approveProperty("nationality", "Norwegian");
        house4.approveProperty("drink", "Orange juice");
        house4.approveProperty("pet", "Horse");
        house4.approveProperty("smoke", "Lucky strike");
        street.refreshHousesProperties();
        assertEquals(true, street.isResolved());
    }
    
}
