package totalchaos;

import io.mnemotechnician.autoupdater.*;
import arc.*;
import arc.math.*;
import arc.util.*;
import arc.struct.*;
import arc.graphics.g2d.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;

class TotalChaos : Mod() {

	init {
		Events.on(ClientLoadEvent::class.java) {
			val copy = Seq<TextureRegion>(500);
			Core.atlas.regions.each {
			    copy.add(TextureRegion(it));
			};
			
			Core.atlas.regions.each {
			    val index = Math.floor(Mathf.random(copy.size)).toInt();
			    it.set(copy[index] as TextureAtlas.AtlasRegion);
			
			    copy.remove(index);
			};
			
			
			val map = Core.bundle.properties;
			val mapCopy = map.copy();
			val keysTemp = Seq<String>();
			map.each { k, v -> keys.add(k) };
			
			map.each { k, v ->
			    val index = Math.floor(Mathf.random(keys.size));
			    val newKey = keys[index];
			    keys.remove(index);
			
			    map.put(k, mapCopy[newKey]);
			};
			
			mapCopy.clear();
			
			Updater.checkUpdates();
		}
	}
}
