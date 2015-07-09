package org.vnp.sweetbopby;

import game.base.BaseMGameActivty;
import game.base.BaseMSprise;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;

import com.vnp.core.common.CommonAndroid;

import android.os.Bundle;
import android.view.Display;

public class MainActivity extends BaseMGameActivty {
	private BaseMSprise baseSprise = new BaseMSprise();

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		return false;
	}

	@Override
	public void onLoadComplete() {
		int with = baseSprise.getRegCat().getWidth();
		int height = baseSprise.getRegCat().getHeight();
		int left = ((int)getmCamera().getWidth()- with * 9) / 2;
		int top = ((int)getmCamera().getHeight()- height * 9) / 2;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				AnimatedSprite sprite = new AnimatedSprite(left + i * with, top + j * height, baseSprise.getRegCat().deepCopy());
				getmMainScene().attachChild(sprite);
			}
		}
	}

	@Override
	public void onLoadResources() {
		super.onLoadResources();
		baseSprise.onCreateResources(mEngine, this, "mbg.png", 1, 1);
	}

	@Override
	public Scene onLoadScene() {
		baseSprise.onCreateScene(getmMainScene());
		return super.onLoadScene();
	}

}
