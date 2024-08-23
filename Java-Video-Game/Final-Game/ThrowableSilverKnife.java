import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This knife moves in any direction until it collides with an object.
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 10, 2022
 * 
 * I used this video as a refresher before coding this section. The code I wrote
 * here is mostly trial and error.
 * https://www.youtube.com/watch?v=hRT-vi53cls&t=133s
 */
public class ThrowableSilverKnife extends Actor
{
    /* FIELD(S) */
    private int throwCooldown = 0;
    
    /* CONSTRUCTOR(S) */
    
    /* METHOD(S) */
    /**
     * Act - do whatever the ThrowableSilverKnife wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * 
     * Source for "knifeimpact.wav":
     * https://www.youtube.com/watch?v=8xgsADj_h80
     */
    public void act()
    {
        checkKeyPress();
        move(12);
        throwCooldown--;
        if (getX() == 0 || getX() == 639 || getY() == 0 || getY() == 479 || isTouching(Pillar.class))
        {
            Greenfoot.playSound("knifeimpact.wav");
            Courtyard courtyard = (Courtyard)getWorld();
            getWorld().removeObject(this);
        } // end if
    } // end method act
    
    private void checkKeyPress()
    {
        if ( Greenfoot.isKeyDown("up") && throwCooldown <= 0)
        {
            setRotation(270);
            throwCooldown = 75;
        } // end if
        if ( Greenfoot.isKeyDown("down") && throwCooldown <= 0 )
        {
            setRotation(90);
            throwCooldown = 75;
        } // end if
        if ( Greenfoot.isKeyDown("left") && throwCooldown <= 0 )
        {
            setRotation(180);
            throwCooldown = 75;
        } // end if
        if ( Greenfoot.isKeyDown("right") && throwCooldown <= 0 )
        {
            setRotation(0);
            throwCooldown = 75;
        } // end if
    } // end method checkKeyPress
} // end class SilverKnifeDown
