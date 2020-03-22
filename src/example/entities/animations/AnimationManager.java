package example.entities.animations;

import engine.display.Renderer;
import engine.graphics.ImageLoader;
import engine.utils.Constants;
import engine.utils.Utils;
import example.entities.Pokemon;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.TreeMap;

public class AnimationManager {

    private Pokemon pokemon;
    private TreeMap<String, PokemonAnimation> animations;
    private PokemonAnimation selectedAnimation;

    public AnimationManager(Pokemon pokemon) {
        this.pokemon = pokemon;
        animations = new TreeMap<>();

        String json = Constants.IMAGES_PATH + pokemon.getName() + "/" + pokemon.getName() + ".json";

        JSONObject config = new JSONObject(Utils.loadJSON(json));
        JSONArray anims = config.getJSONArray("animations");

        for(int i = 0; i < anims.length(); i++) {
            JSONObject anim = anims.getJSONObject(i);
            String name = anim.getString("name");
            int spriteSize = anim.getInt("sprite_size");
            JSONArray durations = anim.getJSONArray("frames");
            animations.put(name, new PokemonAnimation(pokemon, ImageLoader.loadImage(pokemon.getName() + "/" + name + ".png"), spriteSize, durations, -1));
        }

        select("idle");
    }

    public void render(Renderer renderer) {
        Point.Double p = pokemon.isoCoordinates();
        renderer.drawImage(selectedAnimation.getCurrentFrame(), pokemon.getSize(), p.x, p.y - pokemon.getSize()/4, true, true);
    }

    public void update() {
        selectedAnimation.update();
    }

    public void select(String name) {
        if(animations.containsKey(name))
            selectedAnimation = animations.get(name);
    }

}
