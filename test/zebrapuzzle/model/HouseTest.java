package zebrapuzzle.model;

import zebrapuzzle.model.Property;
import zebrapuzzle.model.House;
import static zebrapuzzle.Application.HOUSE_POSITION_PROP;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class HouseTest
{
    private House house;
    
    @Before
    public void setUp()
    {
        Property colorProp = new Property("color", new HashSet<>(Arrays.asList("red", "green", "blue")));
        Property positionProp = new Property(HOUSE_POSITION_PROP, new HashSet<>(Arrays.asList("1", "2", "3")));
        Map<String, Property> properties = new HashMap<>();
        properties.put(colorProp.getName(), colorProp);
        properties.put(positionProp.getName(), positionProp);
        
        house = new House(properties, "1");
    }
    
    @Test
    public void testAddProp()
    {
        house.approveProperty("color", "red");
        assertEquals("red", house.getPropertyValue("color"));
    }
    
}
