import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This container has nothing in it and it changes images when the player touches it.
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 */
public class EmptyContainer extends Actor
{
    /* FIELD(S) */
    
    /* CONSTRUCTOR(S) */
    
    /* METHOD(S) */
    /**
     * Act - do whatever the Desk wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (isTouching(CityGuard1.class))
        {
            setImage("container2.png");
        } // end if
    } // end method act
} // end class Container
