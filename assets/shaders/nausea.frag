#define HIGHP;

uniform sampler2D u_texture;
uniform sampler2D u_noise;

uniform float u_time;

void main() {
	vec seed = gl_fragCoord + u_time;
	vec2 noise = vec2(
		texture2D(u_noise, seed * vec2(-0.43, 0.39)),
		texture2D(u_noise, seed * vec2(0.61, -0.76))
	);
	
	vec3 color = texture2D(u_texture, gl_FragCoord.xy + noise);
	
	gl_FragColor = color;
}