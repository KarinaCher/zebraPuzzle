package zebrapuzzle;

import java.io.IOException;
import zebrapuzzle.condition.Condition;
import static zebrapuzzle.Application.HOUSE_POSITION_PROP;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import zebrapuzzle.model.Street;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class ApplicationTest
{
    private String fileName, questionFile;
    private String count;
    private final String[] args = new String[]{"5", "inputFile.txt", "question.txt"};
    private Set<Condition> conditions;
    private Street street;
    @Before
    public void setUp()
    {
        fileName = args[1];
        questionFile = args[2];
        count = args[0];
    }

    @Test
    public void testReadIn() throws IOException
    {
        conditions = Application.readIn(fileName);
        Assert.assertEquals(14, conditions.size());
    }

    @Test
    public void testInitHouses()
    {
        conditions = Application.readIn(fileName);
        street = new Street(Integer.parseInt(count), conditions, questionFile);
        Assert.assertEquals("1", street.getHouseByPosition(1).getPropertyValue(HOUSE_POSITION_PROP));
        Assert.assertEquals("2", street.getHouseByPosition(2).getPropertyValue(HOUSE_POSITION_PROP));
        Assert.assertEquals("3", street.getHouseByPosition(3).getPropertyValue(HOUSE_POSITION_PROP));
        Assert.assertEquals("4", street.getHouseByPosition(4).getPropertyValue(HOUSE_POSITION_PROP));
        Assert.assertEquals("5", street.getHouseByPosition(5).getPropertyValue(HOUSE_POSITION_PROP));
    }
    
}
