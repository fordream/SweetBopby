package org.vnp.sweetbopby;

import game.base.BaseMGameActivty;
import game.base.BaseMSprise;
import game.base.FontObject;
import game.base.ItemObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.vnp.sweetbopby.utils.SweetUtils;
import org.vnp.sweetbopby.utils.SweetUtils.Way;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.acv.cheerz.db.DataStore;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.LogUtils;

public class MainActivity extends BaseMGameActivty {
	private BaseMSprise bg = new BaseMSprise();
	private BaseMSprise baseSprise = new BaseMSprise();
	private BaseMSprise menuSprise = new BaseMSprise();
	private BaseMSprise newGame = new BaseMSprise();
	private ItemObject[][] boards = new ItemObject[SweetUtils.ROWS][SweetUtils.COLUMNS];
	private BaseMSprise mBoard = new BaseMSprise();
	private BaseMSprise line = new BaseMSprise();
	private BaseMSprise gameover = new BaseMSprise();
	private FontObject scoreText = new FontObject();
	private FontObject scoreNumber = new FontObject();
	private FontObject highText = new FontObject();
	private FontObject highNumber = new FontObject();
	private FontObject nextText = new FontObject();

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		DataStore.getInstance().init(this);
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j] = new ItemObject();
			}
		}
	}

	private int score = 0;

	@Override
	public boolean onSceneTouchEvent(Scene mScene, TouchEvent arg1) {
		if (arg1.getAction() == TouchEvent.ACTION_DOWN) {

			int x = (int) arg1.getX();
			int y = (int) arg1.getY();

			// null or not null
			ItemObject selected = getItemSlected(x, y);
			// null or not null
			ItemObject itemChecked = getItemChecked();
			if (selected == null) {

				if (SweetUtils.isClickBy(newGame, x, y)) {
					createNewGame();
				}
				return true;
			}

			if (gameover.getSprCat().isVisible()) {
				return true;
			}
			if (itemChecked == null) {
				if (selected != null) {
					selected.setChecked(true);
					selected.update(getmMainScene());
				}
			} else if (selected != null) {
				if (selected == itemChecked) {
					selected.setChecked(false);
					selected.update(getmMainScene());
				} else if (!selected.isBig()) {
					// FIXME find way
					Way way = SweetUtils.findway(itemChecked, selected, boards);
					if (way != null) {
						selected.randomType(getmMainScene(), itemChecked.getType(), mBoard);
						itemChecked.clear(getmMainScene());

						Way item = way;

						// List<AnimatedSprite> list = new
						// ArrayList<AnimatedSprite>();
						do {
							item.getIt().way(true);
							item = item.getParent();
						} while (item.getParent() != null);

						// FIXME
						// check an hay khong
						List<ItemObject> eat = checkEat(selected);
						if (eat.size() == 0) {
							phontTo();
							// check eat when phong to
							for (int i = 0; i < SweetUtils.ROWS; i++) {
								for (int j = 0; j < SweetUtils.COLUMNS; j++) {
									if (boards[i][j].isBig()) {
										List<ItemObject> eatTemp = checkEat(boards[i][j]);
										if (eatTemp.size() > 0) {
											for (ItemObject itemx : eatTemp) {
												if (!eat.contains(itemx)) {
													eat.add(itemx);
												}
											}
										}
									}
								}
							}
						}

						if (eat.size() > 0) {
							score = score + eat.size() * 2;
							updateScore();

							for (ItemObject mitem : eat) {
								mitem.setType(-1);
								mitem.update(getmMainScene());
							}
							// clear ball ear
						} else {
							// check an when phon to
							randomNext();
						}

						// neu khong an --> nho thanh to
						// add diem

						//
					}
				}
			}
		}

		return true;
	}

	private List<ItemObject> checkEat(ItemObject selected) {
		List<ItemObject> list = new ArrayList<ItemObject>();
		list.addAll(checkEat(selected, CHECKEAT.NGANG));
		list.addAll(checkEat(selected, CHECKEAT.DOC));
		list.addAll(checkEat(selected, CHECKEAT.TREO00));
		list.addAll(checkEat(selected, CHECKEAT.TREO0MAX));

		return list;
	}

	private List<ItemObject> checkEat(ItemObject selected, CHECKEAT typeCheck) {
		int x = selected.getPosition().x;
		int y = selected.getPosition().y;
		int type = selected.getType();

		List<ItemObject> list = new ArrayList<ItemObject>();

		if (typeCheck == CHECKEAT.NGANG) {
			ItemObject item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().x - 1 >= 0) {
						item = boards[item.getPosition().x - 1][y];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}

			item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().x + 1 < SweetUtils.ROWS) {
						item = boards[item.getPosition().x + 1][y];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}
		} else if (typeCheck == CHECKEAT.DOC) {
			ItemObject item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().y - 1 >= 0) {
						item = boards[x][item.getPosition().y - 1];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}

			item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().y + 1 < SweetUtils.ROWS) {
						item = boards[x][item.getPosition().y + 1];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}
		} else if (typeCheck == CHECKEAT.TREO00) {
			ItemObject item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().y - 1 >= 0 && item.getPosition().x - 1 >= 0) {
						item = boards[item.getPosition().x - 1][item.getPosition().y - 1];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}

			item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().y + 1 < SweetUtils.ROWS && item.getPosition().x + 1 < SweetUtils.ROWS) {
						item = boards[item.getPosition().x + 1][item.getPosition().y + 1];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}
		} else if (typeCheck == CHECKEAT.TREO0MAX) {
			ItemObject item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().y + 1 < SweetUtils.ROWS && item.getPosition().x - 1 >= 0) {
						item = boards[item.getPosition().x - 1][item.getPosition().y + 1];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}

			item = selected;
			while (item != null) {
				if (item.getType() == type) {
					if (!list.contains(item)) {
						list.add(item);
					}

					if (item.getPosition().x + 1 < SweetUtils.ROWS && item.getPosition().y - 1 >= 0) {
						item = boards[item.getPosition().x + 1][item.getPosition().y - 1];
					} else {
						item = null;
					}
				} else {
					break;
				}
			}
		}

		if (list.size() < 5) {
			list.clear();
		}
		return list;
	}

	private enum CHECKEAT {
		NGANG, DOC, TREO00, TREO0MAX
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(final Message msg) {
			super.dispatchMessage(msg);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					List<AnimatedSprite> list = (List<AnimatedSprite>) msg.obj;
					for (AnimatedSprite sprite : list) {
						getmMainScene().detachChild(sprite);
					}
				}
			});
		}
	};

	private ItemObject getItemChecked() {
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				if (boards[i][j].isChecked()) {
					return boards[i][j];
				}
			}
		}
		return null;
	}

	private ItemObject getItemSlected(int x, int y) {
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				if (boards[i][j].isSelected(x, y)) {
					return boards[i][j];
				}
			}
		}
		return null;
	}

	@Override
	public void onLoadComplete() {
		AnimatedSprite newGame = this.newGame.getSprCat();
		getmMainScene().attachChild(bg.getSprCat());
		TiledTextureRegion region = baseSprise.getRegCat();
		int with = region.getWidth() / 2;
		int height = region.getHeight();
		int top = ((int) getmCamera().getHeight() - height * SweetUtils.ROWS) / 2;

		int left = ((int) getmCamera().getWidth() - with * SweetUtils.COLUMNS - (int) newGame.getWidth() / 2 - 15);
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j].create(getmMainScene(), left, top, i, j, region);
				boards[i][j].randomType(getmMainScene(), -1, mBoard);
			}
		}

		getmMainScene().attachChild(newGame);
		newGame.animate(200);
		newGame.setPosition(left / 2 - newGame.getWidth() / 2, top + 50);

		int mLeft = (int) (left / 2 - newGame.getWidth() / 2);
		nextText.setText("Next");
		scoreText.setText("Score");
		scoreNumber.setText("0");
		highText.setText("High Score");
		highNumber.setText("0");

		scoreText.setPosition(mLeft, top + 100);
		scoreNumber.setPosition(mLeft + 5, top + 150);

		AnimatedSprite menu1 = new AnimatedSprite(mLeft, top + 145, menuSprise.getRegCat().deepCopy());
		getmMainScene().attachChild(menu1);

		// nextText.setPosition(mLeft, top + 200);

		highText.setPosition(mLeft, top + 450);
		highNumber.setPosition(mLeft + 5, top + 500);
		AnimatedSprite menu2 = new AnimatedSprite(mLeft, top + 495, menuSprise.getRegCat().deepCopy());
		getmMainScene().attachChild(menu2);

		// nextText.attachChild(getmMainScene());
		scoreText.attachChild(getmMainScene());
		scoreNumber.attachChild(getmMainScene());
		highText.attachChild(getmMainScene());
		highNumber.attachChild(getmMainScene());
		highNumber.setText(getMaxScore() + "");

		gameover.getSprCat().setPosition((getmCamera().getWidth() - gameover.getSprCat().getWidth()) / 2, (getmCamera().getHeight() - gameover.getSprCat().getHeight()) / 2);
		getmMainScene().attachChild(gameover.getSprCat(), 100);

		createNewGame();
	}

	private int getMaxScore() {
		return DataStore.getInstance().get("highscore", 0);
	}

	public void setMaxScore(int hightScore) {
		if (hightScore > getMaxScore()) {
			DataStore.getInstance().save("highscore", hightScore);
		}
	}

	private void phontTo() {
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j].toBig(getmMainScene(), mBoard);
			}
		}

	}

	private void randomNext() {
		// Phong to
		List<ItemObject> items = new ArrayList<ItemObject>();

		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {

				if (boards[i][j].getType() == -1) {
					items.add(boards[i][j]);
				}
			}
		}

		if (items.size() <= 1) {
			// CommonAndroid.showDialog(this, "end game", null);
			setMaxScore(score);
			updateMaxScore();
			gameover.getSprCat().setVisible(true);
			// FIXME
			return;
		}

		Random random = new Random();
		if (items.size() > 0) {
			int index = random.nextInt(items.size());
			ItemObject object = items.get(index);
			object.randomType(getmMainScene(), (random.nextInt(SweetUtils.MAXTYPEBALL) + 1) * 10, mBoard);
			items.remove(object);
		}
		if (items.size() > 0) {
			int index = random.nextInt(items.size());
			ItemObject object = items.get(index);
			object.randomType(getmMainScene(), (random.nextInt(SweetUtils.MAXTYPEBALL) + 1) * 10, mBoard);
			items.remove(object);
		}
		if (items.size() > 0) {
			int index = random.nextInt(items.size());
			ItemObject object = items.get(index);
			object.randomType(getmMainScene(), (random.nextInt(SweetUtils.MAXTYPEBALL) + 1) * 10, mBoard);
			items.remove(object);
		}
	}

	private void updateMaxScore() {
		highNumber.setText(getMaxScore() + "");
	}

	public void createNewGame() {
		gameover.getSprCat().setVisible(false);
		score = 0;
		List<ItemObject> list = new ArrayList<ItemObject>();
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j].clear(getmMainScene());
				list.add(boards[i][j]);
			}
		}

		Random random = new Random();

		for (int i = 0; i < 3; i++) {
			int index = random.nextInt(list.size());
			ItemObject object = list.get(index);
			list.remove(index);
			object.randomType(getmMainScene(), random.nextInt(SweetUtils.MAXTYPEBALL) + 1, mBoard);
		}
		for (int i = 0; i < 3; i++) {
			int index = random.nextInt(list.size());
			ItemObject object = list.get(index);
			list.remove(index);
			object.randomType(getmMainScene(), 10 * (random.nextInt(SweetUtils.MAXTYPEBALL) + 1), mBoard);
		}
		updateScore();
	}

	private void updateScore() {
		scoreNumber.setText(score + "");
	}

	@Override
	public void onLoadResources() {
		super.onLoadResources();
		baseSprise.onCreateResources(mEngine, this, "mbg.png", 2, 1);
		mBoard.onCreateResources(mEngine, this, "bong1s1.png", 3, 8);
		line.onCreateResources(mEngine, this, "line.png", 1, 1);
		newGame.onCreateResources(getEngine(), this, "new_game.png", 2, 1);
		bg.onCreateResources(getEngine(), this, new Random().nextInt(100) < 50 ? "bg1.png" : "bg2.png", 1, 1);

		nextText.onLoadResources(getEngine());
		scoreText.onLoadResources(getEngine());
		scoreNumber.onLoadResources(getEngine());
		highText.onLoadResources(getEngine());
		highNumber.onLoadResources(getEngine());
		gameover.onCreateResources(getEngine(), this, "gameover.png", 1, 1);
		menuSprise.onCreateResources(getEngine(), this, "menu_score.png", 1, 1);
	}

	@Override
	public Scene onLoadScene() {
		Scene scene = super.onLoadScene();
		baseSprise.onCreateScene(getmMainScene());
		line.onCreateScene(getmMainScene());
		mBoard.onCreateScene(getmMainScene());
		newGame.onCreateScene(getmMainScene());
		bg.onCreateScene(getmMainScene());
		nextText.onLoadScene(getmCamera());
		scoreText.onLoadScene(getmCamera());
		scoreNumber.onLoadScene(getmCamera());
		highText.onLoadScene(getmCamera());
		highNumber.onLoadScene(getmCamera());
		menuSprise.onloadSucess(getmMainScene());
		gameover.onCreateScene(getmMainScene());
		return scene;
	}
}
