import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This world contains CityGuard2(), Werewolf(), and four Pillar() objects.
 * SilverKnife objects can appear when the user presses any of the arrow keys.
 * 
 * @author croweld@email.uscb.edu
 * @version Dec 11, 2022
 */
public class Courtyard extends World
{
    /* FIELD(S) */
    
    /* CONSTRUCTOR(S) */
    /**
     * This constructor sets the world size and calls the prepare() method.
     */
    public Courtyard()
    {    
        // Create a new Courtyardww with 640x480 cells with a cell size of 1x1 pixels.
        super(640, 480, 1); 
        prepare();
    } // end Courtyard constructor
    
    /* METHOD(S) */
    /**
     * Adds all necessary objects to this world.
     */
    public void prepare()
    {
        addObject(new CityGuard2(), 320, 34);
        addObject(new Pillar(), 200, 150);
        addObject(new Pillar(), 200, 330);
        addObject(new Pillar(), 440, 150);
        addObject(new Pillar(), 440, 330);
        addObject(new Werewolf(), 320, 400);
    } // end method prepare
} // end class Courtyard
