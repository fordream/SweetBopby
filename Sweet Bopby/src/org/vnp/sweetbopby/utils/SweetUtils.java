package org.vnp.sweetbopby.utils;

import game.base.BaseMSprise;
import game.base.ItemObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SweetUtils {

	public static final int ROWS = 9;
	public static final int COLUMNS = 9;
	public static final int MAXTYPEBALL = 8;

	public static class Way {
		public Way(Way parent, ItemObject it) {
			this.it = it;
			this.parent = parent;
		}

		public ItemObject getIt() {
			return it;
		}

		public Way getParent() {
			return parent;
		}

		private ItemObject it;
		private Way parent;
	}

	public static Way findway(ItemObject itemChecked, ItemObject itemSelected, ItemObject[][] boards) {
		List<Way> ques = new ArrayList<Way>();
		Map<ItemObject, String> danhdau = new HashMap<ItemObject, String>();

		ques.add(new Way(null, itemChecked));
		danhdau.put(itemChecked, "1");
		while (ques.size() > 0) {
			Way item = ques.get(ques.size() - 1);
			danhdau.put(item.getIt(), "1");
			ques.remove(item);

			int x = item.getIt().getPosition().x;
			int y = item.getIt().getPosition().y;

			// check 4 o
			// x, y +1
			if (y < COLUMNS - 1) {
				ItemObject it = boards[x][y + 1];
				Way way = new Way(item, it);

				if (it == itemSelected) {
					return way;
				}

				if (danhdau.get(it) == null && !it.isBig()) {
					ques.add(way);
				}
			}
			// x , y-1
			if (y > 0) {
				ItemObject it = boards[x][y - 1];
				Way way = new Way(item, it);

				if (it == itemSelected) {
					return way;
				}

				if (danhdau.get(it) == null && !it.isBig()) {
					ques.add(way);
				}
			}
			// x -1, y
			if (x > 0) {
				ItemObject it = boards[x - 1][y];
				Way way = new Way(item, it);

				if (it == itemSelected) {
					return way;
				}

				if (danhdau.get(it) == null && !it.isBig()) {
					ques.add(way);
				}
			}
			// x+ 1, y
			if (x < ROWS - 1) {
				ItemObject it = boards[x + 1][y];
				Way way = new Way(item, it);

				if (it == itemSelected) {
					return way;
				}

				if (danhdau.get(it) == null && !it.isBig()) {
					ques.add(way);
				}
			}
			// neu 1 trong 4 o la diem den --> ket thuc
		}

		return null;
	}

	public static boolean isClickBy(BaseMSprise newGame, int x, int y) {
		return newGame.getSprCat().getX() < x && x < newGame.getSprCat().getX() + newGame.getSprCat().getWidth() &&
				newGame.getSprCat().getY() < y && y< newGame.getSprCat().getY() + newGame.getSprCat().getHeight();
	}
}
