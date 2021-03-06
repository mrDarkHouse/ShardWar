/* Copyright (c) 2014 PixelScientists
 * 
 * The MIT License (MIT)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.darkhouse.shardwar.Model.Tooltip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TooltipListener extends ClickListener {

	private boolean inside;

	private AbstractTooltip tooltip;

	public AbstractTooltip getTooltip() {
		return tooltip;
	}

	private boolean followCursor;

	private Vector2 position = new Vector2();

//    public Vector2 getPosition() {
//        return position;
//    }

    private Vector2 tmp = new Vector2();
	private Vector2 offset = new Vector2(10, 10);

	public TooltipListener(AbstractTooltip tooltip, boolean followCursor) {
		this.tooltip = tooltip;
		this.followCursor = followCursor;
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
		if (inside && followCursor) {
			event.getListenerActor().localToStageCoordinates(tmp.set(x, y));
            tooltip.setPosition(tmp.x + position.x + offset.x, tmp.y + position.y + offset.y);
		}
		return false;
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		super.enter(event, x, y, pointer, fromActor);
        inside = true;
		tooltip.show();
		tmp.set(x, y);
		event.getListenerActor().localToStageCoordinates(tmp);
		tooltip.setPosition(tmp.x + position.x + offset.x, tmp.y + position.y + offset.y);
		tooltip.toFront();
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		super.exit(event, x, y, pointer, toActor);
        if(/*toActor == null*/pointer == -1) {
			inside = false;
			tooltip.hide();
			//System.out.println(toActor);
		}

	}


	/**
	 * The offset of the tooltip from the touch position. It should not be
	 * positive as the tooltip will flicker otherwise.
	 */
	public void setOffset(float offsetX, float offsetY) {
		offset.set(offsetX, offsetY);
	}

}
