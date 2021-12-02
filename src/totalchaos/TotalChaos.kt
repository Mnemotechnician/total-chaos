package totalchaos;

import io.mnemotechnician.autoupdater.*;
import arc.*;
import arc.math.*;
import arc.util.*;
import arc.struct.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;

import mindustry.*;
import mindustry.mod.*;
import mindustry.game.EventType.*;

class TotalChaos : Mod() {

	lateinit var buffer: FrameBuffer;
	lateinit var shader: Shader;
	
	init {
		Events.on(ClientLoadEvent::class.java) {
			//regions
			Time.run(60f) {
				val copy = Seq<TextureAtlas.AtlasRegion>(500);
				Core.atlas.regions.each {
					copy.add(TextureAtlas.AtlasRegion(it));
				};
				
				Core.atlas.regions.each {
					val index = Mathf.random(copy.size - 1);
					val newRegion = copy.remove(index);
					
					it.set(newRegion);
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
			
			//mogus
			setupShader();
		}
	}
	
	open fun setupShader() {
		buffer = FrameBuffer(Core.graphics.width, Core.graphics.height);
		shader = NauseaShader(Vars.tree.get("shaders/screenspace.vert").readString(), Vars.tree.get("shaders/nausea.frag").readString());
		
		fun beginDraw() {
			buffer.resize(Core.graphics.width, Core.graphics.height);
			buffer.begin(Color.clear);
		}
		
		Events.run(Trigger.preDraw::class.java) {
			beginDraw();
		}

		/*Events.run(Trigger.uiDrawBegin::class.java) {
			if (Vars.state.isMenu()) {
				beginDraw();
			};
		};*/
		
		Events.run(Trigger.drawOver::class.java) {
			buffer.end();
			
			Draw.blend(Blending.additive);
			Draw.blit(buffer, shader);
			Draw.blend();
		};
	};
	
	open class NauseaShader(vert: String, frag: String) : Shader(vert, frag) {
	
		lateinit var noise: Texture;
		
		init {
			noise = Texture(Vars.tree.get("sprites/noise.png"));
			noise.setFilter(Texture.TextureFilter.linear);
        		noise.setWrap(Texture.TextureWrap.repeat);
		}
		
		override open fun apply() {
			setUniformf("u_time", Time.globalTime / Mathf.PI);
			
			noise.bind(1);
			setUniformi("u_noise", 1);
		}
		
	}
	
}
