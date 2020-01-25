package games.haxBall;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppLoader;

public class World extends BasicGameState {

	private int ID;
	private int state;

	private Field field;
	private int width;
	private int height;
	private ScoreInterface interfaceScore;

	private Audio soundMusicBackground;
	private float soundMusicBackgroundPos;

	public World(int ID) {
		this.ID = ID;
		this.state = 0;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au chargement du programme */
		soundMusicBackground = AppLoader.loadAudio("/musics/haxBall/crowd.ogg");
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à l'apparition de la page */
		if (this.state == 0) {
			this.play(container, game);
		} else if (this.state == 2) {
			this.resume(container, game);
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à la disparition de la page */
		if (this.state == 1) {
			this.pause(container, game);
		} else if (this.state == 3) {
			this.stop(container, game);
			this.state = 0; // TODO: remove
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			this.setState(1);
			game.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
		field.update(container, game, delta);
		if (interfaceScore.isWin()) {
			this.setState(3);
			game.enterState(3, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		field.keyPressed(key,c);
	}

	@Override
	public void keyReleased(int key, char c) {
		field.keyReleased(key,c);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		field.render(container, game, context);
		interfaceScore.render(container, game, context);
	}

	public void play (GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au début du jeu */
		soundMusicBackground.playAsMusic(1, 2f, true);
		this.width = container.getWidth ();
		this.height = container.getHeight ();
		field = new Field(this.height , this.width);
		interfaceScore = new ScoreInterface(field.getBall());
	}

	public void pause(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la mise en pause du jeu */
		soundMusicBackgroundPos = soundMusicBackground.getPosition();
		soundMusicBackground.stop();
	}

	public void resume(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la reprise du jeu */
		soundMusicBackground.playAsMusic(1, 2f, true);
		soundMusicBackground.setPosition(soundMusicBackgroundPos);
	}

	public void stop(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois à la fin du jeu */
		soundMusicBackground.stop();
	}

	public void setState (int state) {
		this.state = state;
	}

	public int getState () {
		return this.state;
	}

}
