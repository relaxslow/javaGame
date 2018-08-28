#version 330

in vec2 v_TextureCoords;
out vec4 fragColor;

uniform sampler2D u_Texture1;
void main()
{
fragColor = texture(u_Texture1, v_TextureCoords);
}