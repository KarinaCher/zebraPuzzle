package zebrapuzzle.model;

import zebrapuzzle.model.Property;
import zebrapuzzle.condition.Relevance;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class PropertyTest
{
    Property prop;
    
    @Before
    public void setUp() throws Exception
    {
        prop = new Property("position" , new HashSet<>(Arrays.asList("1", "2", "3")) );
    }

    @Test
    public void testGetValue()
    {
        Assert.assertEquals(null, prop.getValue());
    }

    @Test
    public void testSetValueRelevanceYes()
    {
        prop.setValueRelevance("2", Relevance.YES);
        Assert.assertEquals("2", prop.getValue());
    }
    
    @Test
    public void testSetValueRelevanceNo()
    {
        prop.setValueRelevance("1", Relevance.NO);
        prop.setValueRelevance("2", Relevance.NO);
        Assert.assertEquals("3", prop.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetValueRelevanceExc()
    {
        prop.setValueRelevance("aa", Relevance.NO);
    }
    
}
