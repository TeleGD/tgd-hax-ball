package games.tncyBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.tncyBall.Ball;
import games.tncyBall.Field;
import games.tncyBall.Player;

public class Inflate extends Bonus {
	private Ball ball;
	private int timer;
	private Audio sound;
	private int sizeFactor = 4;

	public Inflate(int posX, int posY, Field field, Ball ball) {
		super(posX, posY, new Color(255,0,255), field);

		this.ball = ball;
		timer = 12*1000;

		this.sound = AppLoader.loadAudio("/sounds/tncyBall/inflate.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(!deleted && activated) {
			timer -= delta;
		}

		if (timer <= 0) {
			ball.setRadius(ball.getRadius()/sizeFactor);
			deleted = true;
		}
		super.update(container, game, delta);
	}

	public void activate(Player p, Ball b) {
		activated = true;
		sound.playAsSoundEffect(1, .4f, false);

		ball.setRadius(ball.getRadius()*sizeFactor);
	}
}
