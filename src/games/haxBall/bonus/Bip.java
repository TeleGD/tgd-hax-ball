package games.haxBall.bonus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public class Bip extends Bonus {

	private int timer;
	private Audio sound;
	private Player p;

	public Bip(int posX, int posY,  Field field) {
		super(posX, posY, new Color(254,222,1), field);
		this.timer = 12*1000;

		this.sound = AppLoader.loadAudio("/sounds/haxBall/bip.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(!activated) {

		} else if(!deleted) {
			timer -= delta;

			if (timer%500<=16 && timer!=0 ){
				p.resetColor();
				sound.playAsSoundEffect(1, .4f, false);

			} else if (timer>0){
				p.setColor(new Color(0,0,0,0));
			}
		}
		if (timer <= 0) {
			p.resetColor();
			deleted = true;
		}
		super.update(container, game, delta);

	}


	public void activate(Player p, Ball b) {
		activated = true;
		this.p=p;
		sound.playAsSoundEffect(1, .4f, false);
	}
}
