package games.haxBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public class Teleport extends Bonus {
	private Ball ball;
	private Audio sound;

	public Teleport(int posX, int posY, Field field, Ball ball) {
		super(posX, posY, new Color(0,255,255), field);

		this.ball = ball;
		this.sound = AppLoader.loadAudio("/sounds/haxBall/teleportation.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		super.update(container, game, delta);
	}

	public void activate(Player p, Ball b) {
		activated = true;

		int posX = (int)(Math.random()*field.getWidth()/6);
		int posY = (int)(Math.random()*field.getHeight()/2) + field.getPosY() + field.getHeight()/4;
		if(p.getTeam() == 1) {
			posX += field.getPosX() + field.getWidth()/6;

		} else {
			posX += field.getPosX() + field.getWidth()/6 + field.getWidth()/2;
		}

		ball.setPosX(posX);
		ball.setPosY(posY);

		deleted = true;
		sound.playAsSoundEffect(1, .4f, false);
	}

}
