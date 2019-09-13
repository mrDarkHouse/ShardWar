package com.darkhouse.shardwar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkhouse.shardwar.Model.UserPanel;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.User;

public class MainMenu extends AbstractScreen{

    public abstract static class RegisterDialog extends Dialog {

        private class ImageChooser extends Table{
            private int index;
            private Image[] images;
            private Texture[] textures;
            private ImageButton prev;
            private ImageButton next;
            private Stack stack;

            public ImageChooser(Texture... textures) {
                this.images = new Image[textures.length];
                this.textures = textures;
                stack = new Stack();
                defaults().space(3f);
                for (int i = 0; i < textures.length; i++) {
                    images[i] = new Image(textures[i]);
                    images[i].setSize(100, 100);
                    stack.add(images[i]);
                    images[i].setVisible(false);
                }
                AssetLoader l = ShardWar.main.getAssetLoader();
                prev = new ImageButton(l.generateImageButtonSkin
                        (l.get("less.png", Texture.class)));
                next = new ImageButton(l.generateImageButtonSkin
                        (l.get("more.png", Texture.class)));
                prev.addListener(new ClickListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(index > 0) {
                            index--;
                            update();
                        }
                        return true;
                    }
                });
                next.addListener(new ClickListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(index < images.length - 1){
                            index++;
                        }else index = 0;
                        update();
                        return true;
                    }
                });
                add(prev);
                add(stack).size(100, 100);
                add(next);
                update();
                pack();
            }
            private void update(){
                for (int i = 0; i < images.length; i++) {
                    if(index == i) images[i].setVisible(true);
                    else images[i].setVisible(false);
                }
            }
            public Texture getImage(){
                return textures[index];
            }


        }

        private TextField name;
        private ImageChooser avatar;

        public RegisterDialog() {
            super("Register", ShardWar.main.getAssetLoader().getSkin());
            init();
//            text("Name:");

        }
        private void init(){
            AssetLoader l = ShardWar.main.getAssetLoader();
            name = new TextField("User", l.getSkin());
            name.setMaxLength(10);
            avatar = new ImageChooser(l.get("player1Logo.png", Texture.class),
                    l.get("player2Logo.png", Texture.class),
                    l.get("player3Logo.png", Texture.class));
//            avatar = new TextField("1", ShardWar.main.getAssetLoader().getSkin());
//            avatar.setMaxLength(1);
            defaults().center();
            row();
            add(name).row();
            add(avatar).row();
//            button("Ok");
            TextButton b = new TextButton("Ok", l.getSkin());
            b.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setUser();
                    hide();
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
            add(b);
        }

        protected abstract void setUser();

        protected User getUser(){
            return User.registerNewUser(
                    /*avatar.getImage(),*/
                    avatar.index,
                    name.getText()
            );
        }
    }

    private RegisterDialog dialog;
    private UserPanel userPanel;

    private void userPanelReposition(){
        userPanel.setPosition(0, Gdx.graphics.getHeight() - userPanel.getHeight());
    }

    public MainMenu() {
        super(ShardWar.main.getAssetLoader().getMainMenuBg());
        dialog = new RegisterDialog(){
            @Override
            protected void setUser() {
                ShardWar.user = getUser();
//                    ShardWar.user.pointsChange(850);
                userPanel.init(ShardWar.user, 150);
                userPanelReposition();
//                    System.out.println(ShardWar.user);
            }
        };
        userPanel = new UserPanel();
        stage.addActor(userPanel);

        TextButton start = new TextButton("Start", ShardWar.main.getAssetLoader().getSkin());
        TextButton options = new TextButton("Options", ShardWar.main.getAssetLoader().getSkin());
        TextButton wiki = new TextButton("Wiki", ShardWar.main.getAssetLoader().getSkin());
        TextButton credits = new TextButton("Credits", ShardWar.main.getAssetLoader().getSkin());
        TextButton exit = new TextButton("Exit", ShardWar.main.getAssetLoader().getSkin());
        start.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(2);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        options.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(1);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        wiki.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ShardWar.main.switchScreen(3);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.exit(0);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Table t = new Table();

        t.defaults().space(30).padRight(40).width(200).height(70);
        t.add(start).row();
        t.add(options).row();
        t.add(wiki).row();
        t.add(credits).row();
        t.add(exit);
        t.right();
        t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(t);

        /*Preferences pref = Gdx.app.getPreferences("usercache");
        if(pref.contains("username")){
            int avatarID = pref.getInteger("userpic");
            String name = pref.getString("username");
            int rankedPoints = pref.getInteger("userpoints");
            ShardWar.user = User.loadUserFromCache(name, avatarID, rankedPoints);
            userPanel.init();
        }else */dialog.show(getStage());
    }

    @Override
    public void show() {
        super.show();
        if(ShardWar.user != null) userPanel.update();
    }

    @Override
    public void hide() {
        super.hide();
//        ShardWar.user.saveCache();
    }
}
