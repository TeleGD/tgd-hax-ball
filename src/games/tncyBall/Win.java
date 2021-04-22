package games.tncyBall;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppMenu;
import app.elements.MenuItem;

public class Win extends AppMenu {

	public Win(int ID) {
		super(ID);
	}

	public void init (GameContainer container, StateBasedGame game) {
		super.initSize(container, game, 600, 400);
		super.init(container, game);
		this.setTitle("Victoire !");
		this.setSubtitle("Sans sous-titre");
		this.setMenu(Arrays.asList(new MenuItem[] {
			new MenuItem("Rejouer") {
				public void itemSelected() {
					game.enterState(4, new FadeOutTransition(), new FadeInTransition());
				}
			},
			new MenuItem("Quitter") {
				public void itemSelected() {
					game.enterState(1, new FadeOutTransition(), new FadeInTransition());
				}
			}
		}));
	}

}
