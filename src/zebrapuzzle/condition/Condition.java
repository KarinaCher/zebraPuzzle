package zebrapuzzle.condition;

/**
 *
 * @author karina.cherkashina@gmail.com
 */
public class Condition
{
    public Condition(HousePosition housePosition, String prop1, String value1, String prop2, String value2)
    {
        this.housePosition = housePosition;
        this.prop1 = prop1;
        this.value1 = value1;
        this.prop2 = prop2;
        this.value2 = value2;
    }
    
    private final HousePosition housePosition;
    private final String prop1;
    private final String value1;
    private final String prop2;
    private final String value2;
    
    public HousePosition getHousePosition()
    {
        return housePosition;
    }

    public String getProp1()
    {
        return prop1;
    }

    public String getValue1()
    {
        return value1;
    }

    public String getProp2()
    {
        return prop2;
    }

    public String getValue2()
    {
        return value2;
    }
}
