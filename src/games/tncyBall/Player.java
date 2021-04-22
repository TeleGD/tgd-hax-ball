package games.tncyBall;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Player {
	private float m_posX, m_posY, m_radius;
	private Color m_actualColor, m_defaultColor;
	private float m_speedX, m_speedY, m_speed;
	private boolean up, down, right, left, updown, rightLeft;
	private Field field;
	private Circle m_shape;
	private boolean shooting;
	private float spawnX, spawnY;
	private int team;
	private String name;
	private int rightButton, leftButton, upButton, downButton, shootButton;
	private Image club_logo;

	public Player(String name, Field field, int team) {

		this.field = field;
		this.name = name;
		this.shooting = false;
		this.team = team;

		m_radius = field.getHeight()/25;
		spawnY = field.getCenterY() - m_radius;
		m_speed = .3f;

		if(team == 0) {

			spawnX = field.getCenterX() - (field.getWidth()*0.35f) - m_radius*2;

			m_defaultColor = new Color(0, 0, 255);
			upButton = Input.KEY_Z;
			downButton = Input.KEY_S;
			leftButton = Input.KEY_Q;
			rightButton = Input.KEY_D;
			shootButton = Input.KEY_SPACE;

			this.club_logo = AppLoader.loadPicture("/images/icon.png");
		}
		else {
			spawnX = field.getCenterX() + (field.getWidth()*0.35f);
			m_defaultColor = new Color(255, 0, 0);

			upButton = Input.KEY_UP;
			downButton = Input.KEY_DOWN;
			leftButton = Input.KEY_LEFT;
			rightButton = Input.KEY_RIGHT;
			shootButton = Input.KEY_ENTER;

			this.club_logo = AppLoader.loadPicture("/images/tncyBall/tektn.png");
		}

		m_posX = spawnX;
		m_posY = spawnY;
		m_actualColor = m_defaultColor;
		m_shape = new Circle(m_posX+(m_radius/2), m_posY+(m_radius/2), m_radius/2);
	}

	//Pilliers //TODO: faire que les pilliers ne soient pas des joueurs lol
	public Player(int x, int y, Field field) {
		this.field = field;
		name = "";

		this.shooting = false;
		this.spawnX = x;
		this.spawnY = y;
		this.m_posX = spawnX;
		this.m_posY = spawnY;
		this.m_radius = field.getHeight()/18;
		this.m_defaultColor = new Color(70,70,70);
		this.m_actualColor = m_defaultColor;
		this.m_speed = 0;
		this.team = 2;
		this.m_shape = new Circle(m_posX+m_radius, m_posY+m_radius, m_radius);
	}

	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		//ombre
		context.setColor(new Color(0, 0, 0, 100));
		context.fillOval(m_posX+3,m_posY+2,m_radius*2,m_radius*2);

		context.setColor(m_actualColor);
		context.fillOval(m_posX, m_posY, m_radius * 2, m_radius * 2);
		if(team < 2) {
			context.setColor(new Color(64, 64, 64));
			context.fillOval(m_posX + 4, m_posY + 4, m_radius * 2 - 8, m_radius * 2 - 8);
			context.drawImage(club_logo, m_posX + 4, m_posY + 4, m_posX + m_radius * 2 - 4, m_posY + m_radius * 2 - 4, 0, 0, club_logo.getWidth(), club_logo.getHeight());
			context.setColor(Color.black);
		}
	}

	public void update (GameContainer container, StateBasedGame game, int delta) {
		move(delta);
		updateShape();
	}

	public boolean collision(Player enemy) {
		updateShape();
		return m_shape.intersects(enemy.getShape());
	}

	private void updateShape() {
		m_shape.setLocation(m_posX, m_posY);
		m_shape.setRadius(m_radius);
	}

	public void keyPressed(int key, char c) {
		if(key==shootButton) shooting = true;

		if (key==upButton) {
			up = true;
			updown=false;
		}
		if (key==downButton) {
			down=true;
			updown=true;
		}
		if (key==leftButton) {
			left=true;
			rightLeft=true;

		}
		if (key==rightButton) {
			right=true;
			rightLeft=false;
		}


	}

	public void keyReleased(int key, char c) {

		if(key==shootButton) shooting = false;

		if (key==upButton) {
			up = false;
		}
		if (key==downButton) {
			down=false;
		}
		if (key==leftButton) {
			left=false;
		}
		if (key==rightButton) {
			right=false;
		}




	}

	public void move(int dt) {
		m_speedX = 0;
		m_speedY = 0;

		if((up && !down) || (up && down && !updown)){
			m_speedY=-m_speed;
		}

		if((down && !up) || (up && down && updown)) {
			m_speedY=m_speed;
		}

		if((left && !right)|| (left && right && rightLeft)) {
			m_speedX = -m_speed;
		}

		if((!left && right)|| (left && right && !rightLeft)) {
			m_speedX = m_speed;
		}

		if (m_speedX!=0 && m_speedY!=0) {
			m_speedX/=Math.sqrt(2);
			m_speedY/=Math.sqrt(2);
		}

		m_posX += dt*m_speedX;
		m_posY += dt*m_speedY;

		for(Player p : field.getPlayers()) {
			if(!p.equals(this) && collision(p)) {
				double powX = (getCenterX() - p.getCenterX()) * (getCenterX() - p.getCenterX());
				double powY = (getCenterY() - p.getCenterY()) * (getCenterY() - p.getCenterY());
				double dist = Math.sqrt(powX + powY);
				if(dist < m_radius + p.m_radius) {
					if(p.team < 2) {
						p.m_posX += dt * m_speedX * 0.5;
						p.m_posY += dt * m_speedY * 0.5;
					}
					placeNextToPlayer(p);

				}
			}
		}

		if(m_posY <= field.getPosY()-m_radius*3) {
			m_posY = field.getPosY()-m_radius*3;
			m_speedY = 0;

		} else if((m_posY) > (field.getPosY() + field.getHeight()+m_radius*2)) {
			m_posY = field.getPosY() + field.getHeight()+m_radius*2;
			m_speedY = 0;
		}

		if (m_posX <= field.getPosX()-m_radius*3) {
			m_posX = field.getPosX()-m_radius*3;
			m_speedX = 0;

		} else if ((m_posX) > (field.getPosX() + field.getWidth()+m_radius*2)) {
			m_posX = field.getPosX() + field.getWidth()+m_radius*2;
			m_speedX = 0;
		}
	}

	private void placeNextToPlayer(Player p) {
		updateShape();
		double angle = Math.atan2(getCenterX() - p.getCenterX(), getCenterY() - p.getCenterY());
		m_posX = p.getCenterX() + (float)(Math.sin(angle) * (m_radius + p.m_radius)) - m_radius;
		m_posY = p.getCenterY() + (float)(Math.cos(angle) * (m_radius + p.m_radius)) - m_radius;
	}

	public boolean isShooting() {
		return shooting;
	}

	public void placer(int x, int y) {
		m_posX = x;
		m_posY = y;
		m_speedX = 0;
		m_speedY = 0;
	}

	public float getCenterX() {
		return m_posX + m_radius;
	}

	public float getCenterY() {
		return m_posY + m_radius;
	}

	public float getPosX() {
		return m_posX;
	}

	public float getPosY() {
		return m_posY;
	}

	public int getTeam() { return team; }

	public void resetPos() {
		m_posX = spawnX;
		m_posY = spawnY;
	}

	public float getSpeedX() {
		return m_speedX;
	}

	public float getSpeedY() {
		return m_speedY;
	}

	public void setSpeed(float speed) {
		m_speed = speed;
	}

	public float getRadius() {
		return m_radius;
	}

	public void setColor(Color color) {
		m_actualColor = color;
	}

	public void resetColor() {
		m_actualColor = m_defaultColor;
	}

	public Circle getShape() {
		return m_shape;
	}



//	public void setShape(Circle shape) {
//		m_shape = shape;
//	}

//	public void setEnemy(Player enemy) {
//		m_enemy = enemy;
//	}

	public float getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(float spawnX) {
		this.spawnX = spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(float spawnY) {
		this.spawnY = spawnY;
	}

}
