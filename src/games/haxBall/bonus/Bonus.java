package games.haxBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.state.StateBasedGame;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public abstract class Bonus {
	private int posX, posY;
	private Color color;
	private int diam;
	protected boolean activated, deleted;
	private int timer;
	private Ellipse shape;
	protected Field field;

	public Bonus(int posX, int posY, Color color, Field field) {
		this.posX = posX;
		this.posY = posY;
		this.color = color;
		this.diam = (int) (0.02*field.getWidth());
		this.field = field;

		this.shape = new Ellipse(posX+diam/2, posY+diam/2, diam/2, diam/2);

		this.timer = 7*1000;

		this.activated = false;
		this.deleted = false;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void update (GameContainer container, StateBasedGame game, int delta) {
		if(!activated)
			timer -= delta;

		if (timer <= 0) {
			deleted = true;
		}
	}

	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		//ombre
		context.setColor(new Color(0, 0, 0, 100));
		context.fillOval(posX+3,posY+2,diam,diam);

		float sineIntensity = 0.1f;
		float colorOffset = ((sineIntensity/2) + (float)Math.sin(timer / 100)*sineIntensity);
		context.setColor(new Color(color.r - colorOffset, color.g- colorOffset, color.b - colorOffset, 255));
		context.fillOval(posX, posY, diam, diam);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isActivated() {
		return activated;
	}

	public Ellipse getShape() {
		return shape;
	}

	public abstract void activate(Player p, Ball b);
}
