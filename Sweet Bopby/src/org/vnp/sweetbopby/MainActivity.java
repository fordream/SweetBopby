package org.vnp.sweetbopby;

import game.base.BaseMGameActivty;
import game.base.BaseMSprise;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;

import android.os.Bundle;

public class MainActivity extends BaseMGameActivty {
	private BaseMSprise baseSprise = new BaseMSprise();

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		return false;
	}

	@Override
	public void onLoadComplete() {
		getmMainScene().attachChild(baseSprise.getSprCat());
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
