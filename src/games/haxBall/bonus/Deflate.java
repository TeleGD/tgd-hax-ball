package games.haxBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public class Deflate extends Bonus {
	private Ball ball;
	private Audio sound;
	private int timer;

	public Deflate(int posX, int posY, Field field) {
		super(posX, posY, new Color(255,128,0), field);

		ball = null;
		timer = 12*1000;
		this.sound = AppLoader.loadAudio("/sounds/haxBall/deflate.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(!deleted && activated) {
			timer -= delta;
		}

		if (timer <= 0) {
			ball.setRad(ball.getRad()*2);
			deleted = true;
		}
		super.update(container, game, delta);
	}

	public void activate(Player p, Ball b) {
		activated = true;
		sound.playAsSoundEffect(1, .4f, false);
		ball = b;
		ball.setRad(ball.getRad()/2);
	}

}
