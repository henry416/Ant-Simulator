package com.henryuts.antsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.Iterator;

public class AntSim extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;

	private OrthographicCamera camera;
    private Viewport viewport;
	private ShapeRenderer shapeRenderer;

	// dimensions of simulation
	private static final int simWidth = 720;
	private static final int simHeight = 480;
	private static final int cellSize = 6;

	// Board object
	private Board board = new Board(simWidth, simHeight, cellSize);

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		camera = new OrthographicCamera();
		viewport = new FitViewport(simWidth, simHeight, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        shapeRenderer = new ShapeRenderer();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
        camera.update();

        // fit surface
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//batch.draw(img, 0, 0);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // draw background surface
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);

        // draw hive rectangle
			//shapeRenderer.setColor(Color.BLUE);
			//shapeRenderer.rect(camera.viewportWidth / 2 - 3, camera.viewportHeight / 2 - 3, 6, 6);
			//shapeRenderer.end();
		shapeRenderer.setColor(Color.BLUE);
		Point hivePos = board.coordTrans(board.hivePos);
		shapeRenderer.rect(hivePos.x, hivePos.y, cellSize, cellSize);

		// draw ALL the obstacles
		shapeRenderer.setColor(Color.BLACK);
		Iterator it = board.obstVec.iterator();
		while (it.hasNext()) {
			Point obstPos = board.coordTrans((Point) it.next());
			shapeRenderer.rect(obstPos.x, obstPos.y, cellSize, cellSize);
		}
		shapeRenderer.end();

        batch.end();
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
