#define HIGHP;

uniform sampler2D u_texture;
uniform sampler2D u_noise;

uniform float u_time;

void main() {
	float seed = gl_FragCoord.x + gl_FragCoord.y + u_time;
	vec2 noise = vec2(
		texture2D(u_noise, seed * vec2(-0.43, 0.39)).r,
		texture2D(u_noise, seed * vec2(0.61, -0.76)).r
	);
	
	gl_FragColor = texture2D(u_texture, gl_FragCoord.xy + noise);
}