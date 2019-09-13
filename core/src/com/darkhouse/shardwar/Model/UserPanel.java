package com.darkhouse.shardwar.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.User;

public class UserPanel extends Table {

    private static class ExpBar extends Stack{
        private ProgressBar bar;
        private Label text;

        public ExpBar() {
            float min = ShardWar.user.getRankedLevel() == 1 ? 0 : User.POINTS[ShardWar.user.getRankedLevel() - 2];
            float max = User.POINTS[ShardWar.user.getRankedLevel() - 1];
            bar = new ProgressBar(min, max, 1f,
                    false, ShardWar.main.getAssetLoader().getTimeBarStyle(25));
            bar.setValue(ShardWar.user.getRankedPoints());
            text = new Label(ShardWar.user.getRankedPoints() + "/" + (int)max, ShardWar.main.getAssetLoader().getSkin());

//            bar.setBounds(0, 0, getWidth(), getHeight());
//            text.setPosition(bar.getWidth()/2, 0);
//            text.setSize();
            addActor(bar);
            addActor(text);
            text.setAlignment(Align.center);
            pack();
        }
    }


    private User user;
    private Label name;
    private Image avatar;
    private Label rankedLevel;
    private ExpBar bar;

    public User getUser() {
        return user;
    }

    public UserPanel() {
        setBackground(ShardWar.main.getAssetLoader().getSkin().getDrawable("info-panel"));
    }
    public void init(User user, float avatarSize){
        this.user = user;
        name = new Label(user.getName(), ShardWar.main.getAssetLoader().getSkin());
        avatar = new Image(user.getAvatar());
        rankedLevel = new Label("Level " + user.getRankedLevel(), ShardWar.main.getAssetLoader().getSkin());
        bar = new ExpBar();
//        bar.setWidth(getWidth() - 4);
        add(avatar).size(avatarSize, avatarSize).row();
        add(name).row();
        add(rankedLevel).row();
        add(bar).left().fill();
        pack();

//        setPosition(0, Gdx.graphics.getHeight() - getHeight());
    }
    public void update(){
        rankedLevel.setText("Level " + user.getRankedLevel());
        bar = new ExpBar();
    }
}
