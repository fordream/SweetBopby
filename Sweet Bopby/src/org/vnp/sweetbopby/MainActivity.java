package org.vnp.sweetbopby;

import game.base.BaseMGameActivty;
import game.base.BaseMSprise;
import game.base.ItemObject;

import java.util.Random;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.os.Bundle;

public class MainActivity extends BaseMGameActivty {
	private BaseMSprise baseSprise = new BaseMSprise();
	private ItemObject[][] boards = new ItemObject[9][9];

	private BaseMSprise[] bigs = new BaseMSprise[8];
	private BaseMSprise[] smalls = new BaseMSprise[8];

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boards[i][j] = new ItemObject();
			}
		}

		for (int i = 0; i < 8; i++) {
			smalls[i] = new BaseMSprise();
			bigs[i] = new BaseMSprise();
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {

		if (arg1.getAction() == TouchEvent.ACTION_DOWN) {
			int x = (int) arg1.getX();
			int y = (int) arg1.getY();
			// null or not null
			ItemObject itemSelected = getItemSlected(x, y);
			// null or not null
			ItemObject itemChecked = getItemSlected();

			if (itemChecked == null) {
				if (itemSelected != null) {
					itemSelected.setChecked(true);
					itemSelected.runAnimation(true);
				}
			} else if (itemSelected != null) {
				if (itemSelected == itemChecked) {
					itemSelected.setChecked(false);
					itemSelected.runAnimation(false);
				} else if (!itemSelected.isBig()) {
					// FIXME find way
				}
			}
		}

		return false;
	}

	private ItemObject getItemSlected() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (boards[i][j].isChecked()) {
					return boards[i][j];
				}
			}
		}
		return null;
	}

	private ItemObject getItemSlected(int x, int y) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (boards[i][j].isSelected(x, y)) {
					return boards[i][j];
				}
			}
		}
		return null;
	}

	@Override
	public void onLoadComplete() {
		TiledTextureRegion region = baseSprise.getRegCat();
		int with = region.getWidth() / 2;
		int height = region.getHeight();
		int left = ((int) getmCamera().getWidth() - with * 9) / 2;
		int top = ((int) getmCamera().getHeight() - height * 9) / 2;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boards[i][j].create(getmMainScene(), left, top, i, j, region);
			}
		}

		createNewGame();
	}

	public void createNewGame() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boards[i][j].clear(getmMainScene());
			}
		}

		Random random = new Random();

		for (int i = 0; i < 3; i++) {
			int px = random.nextInt(9);
			int py = random.nextInt(9);

			if (boards[px][py].getType() != -1) {
				i--;
			} else {
				boards[px][py].randomType(getmMainScene(), random.nextInt(8) + 1, bigs);
			}
		}
	}

	@Override
	public void onLoadResources() {
		super.onLoadResources();
		baseSprise.onCreateResources(mEngine, this, "mbg.png", 2, 1);
		for (int i = 0; i < 8; i++) {
			smalls[i].onCreateResources(mEngine, this, (i + 1) + "_small.png", 1, 1);
			bigs[i].onCreateResources(mEngine, this, (i + 1) + ".png", 1, 1);
		}
	}

	@Override
	public Scene onLoadScene() {
		baseSprise.onCreateScene(getmMainScene());
		return super.onLoadScene();
	}
}
