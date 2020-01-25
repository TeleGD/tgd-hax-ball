package games.haxBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public class Inflate extends Bonus {
	private Ball ball;
	private int timer;
	private Audio sound;

	public Inflate(int posX, int posY, Field field, Ball ball) {
		super(posX, posY, new Color(255,0,255), field);

		this.ball = ball;
		timer = 12*1000;

		this.sound = AppLoader.loadAudio("/sounds/haxBall/inflate.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(!deleted && activated) {
			timer -= delta;
		}

		if (timer <= 0) {
			ball.setRad(ball.getRad()/2);
			deleted = true;
		}
		super.update(container, game, delta);
	}

	public void activate(Player p, Ball b) {
		activated = true;
		sound.playAsSoundEffect(1, .4f, false);

		ball.setRad(ball.getRad()*2);
	}
}
