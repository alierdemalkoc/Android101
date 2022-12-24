package com.alierdemalkoc.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bacground;
	Texture bird;
	Texture enemy1;
	Texture enemy2;
	Texture enemy3;
	Circle birdCircle;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	BitmapFont font2;

	float birdx =0;
	float birdy = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.6f;
	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];
	float [] enemyOffset = new float[numberOfEnemies];
	float [] enemyOffset2 = new float[numberOfEnemies];
	float [] enemyOffset3 = new float[numberOfEnemies];
	Circle[] enemyCircle;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;
	int score = 0;
	int scoredEnemy = 0;

	float distance = 0;
	float enemyVelocity = 2;
	Random random;
	
	@Override
	public void create () {
	batch = new SpriteBatch();
	bacground = new Texture("background.png");
	bird = new Texture("bird.png");
	enemy1 = new Texture("enemy.png");
	enemy2 = new Texture("enemy.png");
	enemy3 = new Texture("enemy.png");

	distance = Gdx.graphics.getWidth()/2;
	random = new Random();

	birdx = Gdx.graphics.getWidth()/3;
	birdy = Gdx.graphics.getHeight()/2;

	birdCircle = new Circle();
	enemyCircle = new Circle[numberOfEnemies];
	enemyCircle2 = new Circle[numberOfEnemies];
	enemyCircle3 = new Circle[numberOfEnemies];
	font = new BitmapFont();
	font.setColor(Color.BLUE);
	font.getData().setScale(4);
	font2 = new BitmapFont();
	font2.setColor(Color.RED);
	font2.getData().setScale(10);



	shapeRenderer = new ShapeRenderer();



		for (int i = 0; i < numberOfEnemies;i++){
		enemyOffset[i] = (random.nextFloat()) * (Gdx.graphics.getHeight());
		enemyOffset2[i] = (random.nextFloat()) * (Gdx.graphics.getHeight());
		enemyOffset3[i] = (random.nextFloat()) * (Gdx.graphics.getHeight());

		enemyX[i] = Gdx.graphics.getWidth()/2-enemy1.getWidth()/2 + i*distance;

		enemyCircle[i] = new Circle();
		enemyCircle2[i] = new Circle();
		enemyCircle3[i] = new Circle();


	}
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(bacground,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if (gameState == 1){
			if (enemyX[scoredEnemy] < birdx){
				score++;
				if (scoredEnemy<3){
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}



			if (Gdx.input.justTouched()){
				velocity = - Gdx.graphics.getHeight()/65;
			}
			for (int i = 0; i<numberOfEnemies;i++){
				if (enemyX[i] < 0) {
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-200);

				} else {
					enemyX[i] = enemyX[i] - enemyVelocity;
				}

				batch.draw(enemy1,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset[i],Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/8);
				batch.draw(enemy2,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset2[i],Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/8);
				batch.draw(enemy3,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset3[i],Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/8);

				enemyCircle[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/20, enemyOffset[i] + Gdx.graphics.getHeight()/16 + Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/20);
				enemyCircle2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/20, enemyOffset2[i] + Gdx.graphics.getHeight()/16 + Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/20);
				enemyCircle3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/20, enemyOffset3[i] + Gdx.graphics.getHeight()/16 + Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/20);

			}



			if (birdy >0 ) {
			velocity = velocity + gravity;
			birdy = birdy - velocity;}
			else{
				gameState = 2;
			}
			if (Gdx.graphics.getWidth() + Gdx.graphics.getHeight()/16 > birdy){
				velocity = velocity + gravity;
				birdy = birdy - velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0){if (Gdx.input.justTouched()){
			gameState = 1;
		}

		} else if (gameState == 2) {
			font2.draw(batch,"GAME OVER!",Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()*2/3);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				birdy = Gdx.graphics.getHeight() / 2;

				for (int i = 0; i < numberOfEnemies; i++) {
					enemyOffset[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffset3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

					enemyX[i] = Gdx.graphics.getWidth() / 2 - enemy1.getWidth() / 2 + i * distance;

					enemyCircle[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();


				}
				velocity = 0;
				scoredEnemy = 0;
				score = 0;
			}
		}

	batch.draw(bird,birdx,birdy,Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/8);
	font.draw(batch,String.valueOf(score),100,200);
	batch.end();

	birdCircle.set(birdx,birdy,Gdx.graphics.getWidth()/20);

	//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	//shapeRenderer.setColor(Color.BLACK);
	//shapeRenderer.circle(birdCircle.x + Gdx.graphics.getWidth()/20,birdCircle.y + Gdx.graphics.getHeight()/16,birdCircle.radius);

	for (int i = 0;i<numberOfEnemies; i++){
	//	shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/20, enemyOffset[i] + Gdx.graphics.getHeight()/16 + Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/20);
		//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/20, enemyOffset2[i] + Gdx.graphics.getHeight()/16 + Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/20);
	//	shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/20, enemyOffset3[i] + Gdx.graphics.getHeight()/16 + Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/20);

		if (Intersector.overlaps(birdCircle,enemyCircle[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i])) {
			gameState = 2;
		}

	}
	//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
