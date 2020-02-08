package games.haxBall;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Ball {
	private float posx;
	private float posy;
	private float vitx;
	private float vity;
	private int radius;
	private Color color;
	private boolean colliding;
	private int r_origx;
	private int r_origy;
	private int r_larg;
	private int r_haut;
	private Circle hitbox;
	private Field field;
	private World world;
	private Player player;
	private float  speed;
	private Audio goalsound;

	public Ball(Field field, World world){

		r_origx = field.getPosX();
		r_origy = field.getPosY();
		r_larg = field.getWidth();
		r_haut = field.getHeight();
		radius = field.getHeight()/60;
		this.field = field;
		this.world = world;
		this.player = null;
		this.colliding = false;
		this.speed = 1.0f;
		resetPos();
		vitx=0;
		vity=0;
		color=Color.white;

		goalsound = AppLoader.loadAudio("/sounds/haxBall/Goal_Sound.ogg");

		hitbox=new Circle(posx+ radius, posy+ radius, radius);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		//on update la position
		float oldPosX = posx;
		float oldPosY = posy;

		if(!colliding) {
			//c'est pas beau mais si on fait pas ça les arrondis sont diff en négatif et en positif du coup si la balle a une vitesse negative elle met plus longtemps à s'arrêter sur cette composante...
			int newVitX = (int)(vitx*delta);
			if(vitx<0) {
				newVitX = -(int)(-vitx*delta);
			}
			int newVitY = (int)(vity*delta);
			if(vity<0) {
				newVitY = -(int)(-vity*delta);
			}

			posx+=newVitX;
			posy+=newVitY;

			vitx=vitx*0.97f;
			vity=vity*0.97f;
			//on regarde si la balle sort du terrain
			bordersCollision(oldPosX, oldPosY);

		}

		//si elle est en collision avec un joueur elle le suit
		if(colliding) {
			player.setSpeed(.3f);
			vitx = player.getSpeedX()*0.97f;
			vity = player.getSpeedY()*0.97f;

			posx+=(int)(vitx*delta);
			posy+=(int)(vity*delta);

			bordersCollision(oldPosX, oldPosY);

			//on regarde si on est toujours en collision avec le joueur
			updateShape();
			colliding = !(Math.sqrt(Math.pow(hitbox.getCenterX() - player.getShape().getCenterX(),2) + Math.pow(hitbox.getCenterY() - player.getShape().getCenterY(),2) ) > (hitbox.getRadius() + player.getShape().getRadius()+1) );

		}

		for (Player p : field.getPlayers()) {
			updateShape();
			if(hitbox.intersects(p.getShape())) { //si on a une collision avec un nouveau joueur
				if(p.getTeam()>1) {
					collideWithPlayer(p);
					shoot(p);
					bordersCollision(oldPosX, oldPosY);

				} else {
					if(!(colliding && p.equals(player))) {
						colliding = true;
						player = p;
						player.setSpeed(.3f);
						vitx = player.getSpeedX();
						vity = player.getSpeedY();

						posx+=vitx*delta;
						posy+=vity*delta;
					}

					collideWithPlayer(player);
					bordersCollision(oldPosX, oldPosY);
				}
			}

			if(p.isShooting() && colliding && player.equals(p)) {
				updateShape();
				shoot(player);
			}
		}

		updateShape();
	}

	private void updateShape() {
		hitbox.setLocation(posx,posy);
		hitbox.setRadius(radius);
	}

	private void collideWithPlayer(Player p) {
		updateShape();
		double angle = Math.atan2(hitbox.getCenterX() - p.getCenterX(), hitbox.getCenterY() - p.getCenterY());
		posx = p.getCenterX() + (float)(Math.sin(angle) * (radius + p.getRadius())) - radius;
		posy = p.getCenterY() + (float)(Math.cos(angle) * (radius + p.getRadius())) - radius;
	}

	private void shoot(Player p) {
		double tmpSpeed = speed;
		if(p.getTeam()>1) tmpSpeed = Math.sqrt(Math.pow(vitx, 2)+Math.pow(vity, 2));

		double angle = 0;
		double hyp = Math.sqrt(2)*tmpSpeed;

		int signeX = 1;
		if(hitbox.getCenterX()-p.getShape().getCenterX()<0) signeX = -1;

		//si on est en +-pi/2
		if(hitbox.getCenterX() - p.getShape().getCenterX() == 0) {
			int signe = 1;
			if(hitbox.getCenterY()-p.getShape().getCenterY()>0) signe = -1;
			angle = signe*Math.PI/2;

		} else {

			angle = (-signeX)*Math.atan((hitbox.getCenterY() - p.getShape().getCenterY())/(hitbox.getCenterX() - p.getShape().getCenterX())); //angle en radians
		}

		vitx = (float)(Math.cos(angle)*hyp*signeX);
		vity = (float)(-hyp*Math.sin(angle));

		if(p.getTeam()<=1) colliding = false;
	}

	private void bordersCollision(float oldX, float oldY) {
		//s'il y a collision sur le bord de droite
		if (posx+ radius > r_origx+r_larg){
			//on regarde s'il y a un but
			if ((posy>r_origy+r_haut*1/3)&&(posy<r_origy+2*r_haut/3)) {
				colliding = false;
				world.addScore(0);
				goalsound.playAsSoundEffect(1, 15f, false);
				resetPos();
				vitx=0;
				vity=0;
//				System.out.println(pointsJ1);
//				System.out.println(pointsJ2);

				for(Player p : field.getPlayers()) {
					p.resetPos();
				}

			} else {
				if(!colliding) vitx=-vitx;
				else vitx=0;

				posx = oldX;
			}

		} else if (posx<r_origx) { //s'il y a collision sur le bord de gauche
			//s'il y a un but
			if ((posy>r_origy+r_haut*1/3)&&(posy<r_origy+2*r_haut/3)) {
				colliding = false;
				world.addScore(1);
				goalsound.playAsSoundEffect(1, 15f, false);
				resetPos();
				vitx=0;
				vity=0;
//				System.out.println(pointsJ1);
//				System.out.println(pointsJ2);

				for(Player p : field.getPlayers()) {
					p.resetPos();
				}

			} else {
				if(!colliding) vitx=-vitx;
				else vitx=0;

				posx=oldX;
			}
		}

		//s'il y a collision avec le bord du bas ou celui du haut
		if (posy+ radius >r_origy+r_haut || posy<r_origy){
			if(!colliding) vity=-vity;
			else vity=0;

			posy=oldY;
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context){
		//ombre
		context.setColor(new Color(0, 0, 0, 100));
		context.fillOval(posx+3,posy+2,radius*2,radius*2);

		//remplissage
		context.setColor(Color.white);
		context.fillOval(posx,posy,radius*2,radius*2);

		//countour
		context.setColor(Color.black);
		context.drawOval(posx,posy,radius*2,radius*2);

	}

	public void setPosX(int posx) {
		this.posx = posx;
	}

	public void setPosY(int posy) {
		this.posy = posy;
	}

	public void setRadius(int r) {
		radius = r;
		posx = (int)hitbox.getCenterX() - radius;
		posy = (int)hitbox.getCenterY() - radius;
		updateShape();
	}

	public int getRadius() {
		return radius;
	}

	public void resetPos() {
		posx= field.getCenterX() - radius;
		posy= field.getCenterY() - radius;
	}
}
