package org.vnp.sweetbopby;

import game.base.BaseMGameActivty;
import game.base.BaseMSprise;
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

public class MainActivity extends BaseMGameActivty {
	private BaseMSprise baseSprise = new BaseMSprise();
	private ItemObject[][] boards = new ItemObject[SweetUtils.ROWS][SweetUtils.COLUMNS];
	private BaseMSprise mBoard = new BaseMSprise();
	private BaseMSprise[] bigs = new BaseMSprise[8];
	private BaseMSprise[] smalls = new BaseMSprise[8];

	private BaseMSprise line = new BaseMSprise();

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j] = new ItemObject();
			}
		}

		for (int i = 0; i < 8; i++) {
			smalls[i] = new BaseMSprise();
			bigs[i] = new BaseMSprise();
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene mScene, TouchEvent arg1) {

		if (arg1.getAction() == TouchEvent.ACTION_DOWN) {
			int x = (int) arg1.getX();
			int y = (int) arg1.getY();
			// null or not null
			ItemObject selected = getItemSlected(x, y);
			// null or not null
			ItemObject itemChecked = getItemChecked();
			if (itemChecked == null) {
				if (selected != null) {
					selected.setChecked(true);
					selected.runAnimation(true);
				}
			} else if (selected != null) {
				if (selected == itemChecked) {
					selected.setChecked(false);
					selected.runAnimation(false);
				} else if (!selected.isBig()) {
					// FIXME find way
					Way way = SweetUtils.findway(itemChecked, selected, boards);
					if (way != null) {
						selected.randomType(getmMainScene(), itemChecked.getType(), bigs);
						itemChecked.clear(getmMainScene());

						Way item = way;

						// List<AnimatedSprite> list = new
						// ArrayList<AnimatedSprite>();
						// do {
						// AnimatedSprite bacground =
						// item.getIt().getBackgroud();
						// AnimatedSprite sprite = new
						// AnimatedSprite(bacground.getX(), bacground.getY(),
						// line.getRegCat().deepCopy());
						// getmMainScene().attachChild(sprite);
						// list.add(sprite);
						// item = item.getParent();
						// } while (item.getParent() != null);
						// remove(list);

						// FIXME
						// check an hay khong
						List<ItemObject> eat = checkEat(selected);
						if (eat.size() > 0) {
							for (ItemObject mitem : eat) {
								mitem.setType(-1);
								mitem.removeSprite(getmMainScene());
							}
							// clear ball ear
						} else {
							randomNext();
						}

						// neu khong an --> nho thanh to

						// add diem

						//
					}
				}
			}
		}

		return false;
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

	private void remove(List<AnimatedSprite> list) {
		Message message = new Message();
		message.obj = list;
		handler.sendMessageAtTime(message, 300);
	}

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
		TiledTextureRegion region = baseSprise.getRegCat();
		int with = region.getWidth() / 2;
		int height = region.getHeight();
		int left = ((int) getmCamera().getWidth() - with * SweetUtils.COLUMNS) / 2;
		int top = ((int) getmCamera().getHeight() - height * SweetUtils.ROWS) / 2;

		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j].create(getmMainScene(), left, top, i, j, region);
			}
		}

		createNewGame();
	}

	private void randomNext() {
		List<ItemObject> items = new ArrayList<ItemObject>();
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j].toBig(getmMainScene(), bigs);

				if (boards[i][j].getType() == -1) {
					items.add(boards[i][j]);
				}
			}
		}

		Random random = new Random();
		if (items.size() > 0) {
			int index = random.nextInt(items.size());
			ItemObject object = items.get(index);
			object.randomType(getmMainScene(), (random.nextInt(8) + 1) * 10, bigs);
			items.remove(object);
		}
		if (items.size() > 0) {
			int index = random.nextInt(items.size());
			ItemObject object = items.get(index);
			object.randomType(getmMainScene(), (random.nextInt(8) + 1) * 10, bigs);
			items.remove(object);
		}
		if (items.size() > 0) {
			int index = random.nextInt(items.size());
			ItemObject object = items.get(index);
			object.randomType(getmMainScene(), (random.nextInt(8) + 1) * 10, bigs);
			items.remove(object);
		}
	}

	public void createNewGame() {
		for (int i = 0; i < SweetUtils.ROWS; i++) {
			for (int j = 0; j < SweetUtils.COLUMNS; j++) {
				boards[i][j].clear(getmMainScene());
			}
		}

		Random random = new Random();

		for (int i = 0; i < 3; i++) {
			int px = random.nextInt(SweetUtils.COLUMNS);
			int py = random.nextInt(SweetUtils.ROWS);

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
			bigs[i].onCreateResources(mEngine, this, (i + 1) + "_x.png", 2, 1);
		}
		mBoard.onCreateResources(mEngine, this, "bongs.png", 3, 8);
		line.onCreateResources(mEngine, this, "line.png", 1, 1);
	}

	@Override
	public Scene onLoadScene() {
		Scene scene = super.onLoadScene();
		baseSprise.onCreateScene(getmMainScene());
		line.onCreateScene(getmMainScene());
		mBoard.onCreateScene(getmMainScene());
		return scene;
	}
}
