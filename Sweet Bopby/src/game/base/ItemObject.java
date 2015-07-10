package game.base;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Point;

public class ItemObject {
	private Point position = new Point();
	private AnimatedSprite backgroud;
	private AnimatedSprite sprite;
	// type = -1 0 1 2 3 4 5 6 7 8
	private int type = -1;

	public AnimatedSprite getBackgroud() {
		return backgroud;
	}

	public Point getPosition() {
		return position;
	}

	public int getType() {
		return type;
	}

	public void create(Scene scene, int left, int top, int positionx, int positionY, TiledTextureRegion region) {
		position.x = positionx;
		position.y = positionY;
		int with = region.getWidth() / 2;
		int height = region.getHeight();
		backgroud = new AnimatedSprite(left + positionx * with, top + positionY * height, region.deepCopy());
		scene.attachChild(backgroud);
	}

	public void clear(Scene scene) {
		type = -1;
		isChecked = false;
		// backgroud.animate(0);
		if (sprite != null) {
			scene.detachChild(sprite);
		}
		sprite = null;
		runAnimation(false);
	}

	public void randomType(Scene scene, int type, BaseMSprise[] bigs) {
		this.type = type;

		int index = type;
		if (index >= 10)
			index = index / 10;
		removeSprite(scene);
		sprite = new AnimatedSprite(backgroud.getX(), backgroud.getY(), bigs[index - 1].getRegCat().deepCopy());
		scene.attachChild(sprite);
		runAnimation(false);
	}

	public void removeSprite(Scene scene) {
		if (sprite != null) {
			scene.detachChild(sprite);
		}
	}

	public boolean isSelected(int x, int y) {
		return backgroud.getX() < x && x < (backgroud.getX() + backgroud.getWidth() / 2) && backgroud.getY() < y && y < (backgroud.getY() + backgroud.getHeight());
	}

	public void setChecked(boolean isChecked) {
		if (isBig()) {
			this.isChecked = isChecked;
		}
	}

	public void runAnimation(boolean isRun) {
		// FIXME
		// run animation selected
		if (isChecked()) {
			if (isBig()) {
				// backgroud.animate(100);
				if (sprite != null)
					sprite.animate(200);
			}
		} else {
			// backgroud.stopAnimation();
			// backgroud.setCurrentTileIndex(0);

			if (sprite != null) {
				sprite.stopAnimation();

				if (isBig()) {
					sprite.setCurrentTileIndex(0);
				} else {
					sprite.setCurrentTileIndex(1);
				}
			}
		}
	}

	private boolean isChecked = false;

	public boolean isChecked() {
		return isChecked;
	}

	public boolean isBig() {
		return type <= 8 && type >= 0;
	}

	public void setType(int type2) {
		type = type2;
	}

	public void toBig(Scene getmMainScene, BaseMSprise[] bigs) {
		if (getType() >= 10) {
			setType(getType() / 10);
		}

		runAnimation(false);
	}
}