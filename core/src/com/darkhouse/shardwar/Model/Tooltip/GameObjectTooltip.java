package com.darkhouse.shardwar.Model.Tooltip;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.ShardWar;

public class GameObjectTooltip extends AbstractTooltip {

    private Slot slot;
    private Label l;

    public GameObjectTooltip(Slot s) {
        super("", ShardWar.main.getAssetLoader().getSkin());
//        getTitleTable().padBottom(20f);
//        getTitleTable().pack();
        this.slot = s;

        l = new Label("", ShardWar.main.getAssetLoader().getSkin(), "spell-tooltip");
        l.getStyle().font.getData().markupEnabled = true;
        add(l);
        hasChanged();
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible) {
            if (l.getText().length == 0) super.setVisible(false);
            else super.setVisible(true);
        } else super.setVisible(false);
    }

    @Override
    public void hasChanged() {
        getTitleLabel().setText(slot.getSomeObject().getName());
        l.setText(slot.getSomeObject().getTooltip());
//        if (l.getText().length == 0) super.setVisible(false);
        pack();
    }
}
