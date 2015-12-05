package com.lando.systems.rings;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class RingsGame extends ApplicationAdapter implements GestureDetector.GestureListener {
	PolygonSpriteBatch   polygonSpriteBatch;
	Playfield            playfield;
	OrthographicCamera   camera;

	@Override
	public void create() {
		polygonSpriteBatch = new PolygonSpriteBatch();

		final int   numSegments        = 8;
		final int   sectorNumDivisions = 20;
		final float sectorAngleSize    = 90f;
		final float outerRadius        = 230f;
		playfield = new Playfield(numSegments, sectorNumDivisions, sectorAngleSize, outerRadius);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		camera.update();

		Gdx.input.setInputProcessor(new GestureDetector(this));
	}

	public void update(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			camera.position.set(0, 0, 0);
			camera.update();
		}

		playfield.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(176 / 255f, 224 / 255f, 230 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		polygonSpriteBatch.setProjectionMatrix(camera.combined);
		polygonSpriteBatch.begin();
		playfield.render(polygonSpriteBatch);
		polygonSpriteBatch.end();
	}

	@Override
	public void dispose() {
		playfield.dispose();
		polygonSpriteBatch.dispose();
	}

	// ------------------------------------------------------------------------
	// GestureListener Implementation
	// ------------------------------------------------------------------------

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	Vector3 touchCoords = new Vector3();

	@Override
	public boolean tap(float x, float y, int count, int button) {
		touchCoords.set(x, y, 0);
		camera.unproject(touchCoords);
		return playfield.handleTouch(touchCoords.x, touchCoords.y);
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.translate(-deltaX, deltaY);
		camera.update();
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
