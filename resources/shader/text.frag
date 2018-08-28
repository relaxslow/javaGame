#version 330
out vec4 fragColor;
uniform vec4 u_Color;
uniform sampler2D u_Texture1;

in vec2 v_TextureCoords;
void main() {
//fragColor =  texture(u_Texture1, v_TextureCoords);
	fragColor = u_Color * texture(u_Texture1, v_TextureCoords);
//	 fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}