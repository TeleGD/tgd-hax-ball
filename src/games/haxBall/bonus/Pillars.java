package games.haxBall.bonus;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.haxBall.Ball;
import games.haxBall.Field;
import games.haxBall.Player;

public class Pillars extends Bonus {

	private int timer;
	private List<Player> pillars;
	private Audio sound;

	public Pillars(int posX, int posY, Field field) {
		super(posX, posY, new Color(70,70,70), field);

		pillars = new ArrayList<Player>();
		this.timer = 20*1000;
		this.sound = AppLoader.loadAudio("/sounds/haxBall/pillar.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(!deleted && activated) {
			timer -= delta;
		}

		if (timer <= 0) {
			for(Player p : pillars) {
				field.removePlayer(p);
			}
			deleted = true;
		}

		super.update(container, game, delta);
	}

	public void activate(Player p, Ball b) {
		activated = true;
		sound.playAsSoundEffect(1, .4f, false);

		int w = field.getWidth();
		int h = field.getHeight();
		int x = field.getPosX();
		int y = field.getPosY();

		pillars.add(new Player((int)(Math.random()*w/6+x),(int)(Math.random()*h/2+y),field));
		pillars.add(new Player((int)(Math.random()*w/6+x+w/6),(int)(Math.random()*h/2+y+h/2),field));
		pillars.add(new Player((int)(Math.random()*w/6+x+w*2/6),(int)(Math.random()*h/2+y),field));
		pillars.add(new Player((int)(Math.random()*w/6+x+w*3/6),(int)(Math.random()*h/2+y+h/2),field));
		pillars.add(new Player((int)(Math.random()*w/6+x+w*4/6),(int)(Math.random()*h/2+y),field));
		pillars.add(new Player((int)(Math.random()*w/6+x+w*5/6),(int)(Math.random()*h/2+y+h/2),field));

		for(Player q : pillars) {
			field.addPlayer(q);
		}
	}
}
