package games.haxBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public class Flash extends Bonus {

	private int timer;
	private Audio sound;

	public Flash(int posX, int posY, Field field) {
		super(posX, posY, new Color(255,255,255), field);
		this.timer = 7*1000;

		this.sound = AppLoader.loadAudio("/sounds/haxBall/flash.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(!activated) {
			super.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));

		} else if(!deleted) {
			timer -= delta;
			field.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
		}

		if (timer <= 0) {
			field.resetColor();
			deleted = true;
		}

		super.update(container, game, delta);
	}

	public void activate(Player p, Ball b) {
		activated = true;
		sound.playAsSoundEffect(1, .4f, false);
	}

}
