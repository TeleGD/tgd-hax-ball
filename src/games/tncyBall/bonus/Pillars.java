package games.tncyBall.bonus;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.tncyBall.Ball;
import games.tncyBall.Field;
import games.tncyBall.Player;

public class Pillars extends Bonus {

	private int timer;
	private List<Player> pillars;
	private Audio sound;
	private int pillarCount;

	public Pillars(int posX, int posY, Field field) {
		super(posX, posY, new Color(70,70,70), field);

		pillars = new ArrayList<Player>();
		this.timer = 20*1000;
		this.pillarCount = 12;
		this.sound = AppLoader.loadAudio("/sounds/tncyBall/pillar.ogg");
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

		for(int i = 0; i < pillarCount; i++)
			pillars.add(new Player((int)(Math.random()*w/pillarCount+x+w*i/pillarCount),(int)(Math.random()*h+y),field));

		for(Player q : pillars) {
			field.addPlayer(q);
		}
	}
}
