package pages;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppMenu;
import app.elements.MenuItem;

import haxBall.World;

public class Choice extends AppMenu {

	public Choice (int ID) {
		super (ID);
	}

	@Override
	public void init (GameContainer container, StateBasedGame game) {
		super.initSize (container, game, 600, 400);
		super.init (container, game);
		this.setTitle ("Choix");
		this.setSubtitle ("CN du 20 octobre 2018");
		this.setMenu (Arrays.asList (new MenuItem [] {
			new MenuItem ("HaxBall") {
				public void itemSelected () {
					((World) game.getState (3)).setState (0);
					game.enterState (3, new FadeOutTransition (), new FadeInTransition ());
				}
			},
			new MenuItem ("Retour") {
				public void itemSelected () {
					game.enterState (0, new FadeOutTransition (), new FadeInTransition ());
				}
			}
		}));
		this.setHint ("SELECT A GAME");
	}

}
