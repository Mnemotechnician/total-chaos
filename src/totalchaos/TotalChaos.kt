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
			//regions
			Time.run(60f) {
				val copy = Seq<TextureRegion>(500);
				Core.atlas.regions.each {
					copy.add(TextureRegion(it));
				};
				
				Core.atlas.regions.each {
					val index = Mathf.random(copy.size - 1);
					val newRegion = copy.remove(index);
					
					if (newRegion is TextureAtlas.AtlasRegion) {
						it.set(newRegion);
					}
				};
			}
			
			//bundles
			val map = Core.bundle.properties;
			val mapCopy = map.copy();
			val keys = Seq<String>(1000);
			map.each { k, v -> keys.add(k) };
			
			map.each { k, v ->
				val index = Mathf.random(keys.size - 1);
				val newKey = keys.remove(index);
				
				map.put(k, mapCopy[newKey]);
			};
			
			mapCopy.clear();
			
			//updates
			Updater.checkUpdates(this);
		}
	}
}
