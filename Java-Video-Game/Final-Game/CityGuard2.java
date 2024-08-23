import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * CityGuard2 is the player-character that fights the Werewolf by throwing knives at 
 * it in stage II ( Courtyard() ).
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 * 
 * Source for CityGuard's sprite & animations:
 * https://opengameart.org/content/lpc-medieval-fantasy-character-sprites
 */
public class CityGuard2 extends Actor
{
    /* FIELD(S) */
    private int cycleCount;
    private int throwCooldown = 0;
    private int spriteFrameCounter = 60;
    private int knivesThrown = 0;
    private int timeRemaining = 0;
    
    /* CONSTRUCTOR(S) */
    
    /* METHOD(S) */
    /**
     * Act - do whatever the CityGuard wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        checkKeyPress();
        throwKnife();
        getKnivesThrown();
        throwCooldown--;
        spriteFrameCounter--;
    } // end method act
    
    /**
     * This method is used to assign keyboard inputs by the user to certain
     * actions of CityGuard such as walking and throwing knives.
     */
    private void checkKeyPress()
    {
        if ( Greenfoot.isKeyDown("w") && getY() >= 33 && spriteFrameCounter <= 60 )
        {
            setLocation(getX(), getY()-2);
            setImage("cityguard_back1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX(), getY()+2);
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_back2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_back3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end outer if
        if ( Greenfoot.isKeyDown("s") && getY() <= 429 )
        {
            setLocation(getX(), getY()+2);
            setImage("cityguard_front1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX(), getY()-2);
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_front2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_front3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("a") && getX() >= 40 )
        {
            setLocation(getX()-2, getY());
            setImage("cityguard_left1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX()+2, getY());
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_left2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_left3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("d") && getX() <= 600 )
        {
            setLocation(getX()+2, getY());
            setImage("cityguard_right1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX()-2, getY());
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_right2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_right3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end if
    } // end method checkKeyPress
    
    /**
     * This method allows the player to throw knives up, down, left, and right
     * by using the arrow keys.
     * 
     * Source for "throw.wav":
     * https://www.youtube.com/watch?v=jhfbDIRYVro
     */
    private void throwKnife()
    {
        if ( Greenfoot.isKeyDown("up") && throwCooldown <= 0)
        {
            getWorld().addObject(new ThrowableSilverKnife(), getX(), getY()+10 );
            throwCooldown = 75;
            knivesThrown++;
            if ( throwCooldown == 75 )
            {
                Greenfoot.playSound("throw.wav");
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("down") && throwCooldown <= 0 )
        {
            getWorld().addObject(new ThrowableSilverKnife(), getX(), getY()+10 );
            throwCooldown = 75;
            knivesThrown++;
            if ( throwCooldown == 75 )
            {
                Greenfoot.playSound("throw.wav");
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("left") && throwCooldown <= 0 )
        {
            getWorld().addObject(new ThrowableSilverKnife(), getX(), getY()+10 );
            throwCooldown = 75;
            knivesThrown++;
            if ( throwCooldown == 75 )
            {
                Greenfoot.playSound("throw.wav");
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("right") && throwCooldown <= 0 )
        {
            getWorld().addObject(new ThrowableSilverKnife(), getX(), getY()+10 );
            throwCooldown = 75;
            knivesThrown++;
            if ( throwCooldown == 75 )
            {
                Greenfoot.playSound("throw.wav");
            } // end inner if
        } // end if
    } // end method throwKnife
    
    /**
     * This method checks to see if the player has thrown all 10 knives before
     * defeating the Werewolf. If the Werewolf hasn't been defeated, a lose
     * screen will appear and prompt the user to try again or quit.
     */
        private void getKnivesThrown()
    {
        if ( knivesThrown == 20 )
        {
            timeRemaining++;
            if ( timeRemaining == 60 )
            {
                Greenfoot.setWorld(new CourtyardLoseScreen1());
            } // end inner if
        } // end outer if
    } // end method checkKnivesThrown
} // end class CityGuard2
