#version 330
in vec2 v_TexCoords;
out vec4 fragColor;

uniform sampler2D u_Texture01;
uniform vec4 u_Color;
void main()
{
      fragColor = u_Color * texture(u_Texture01, v_TexCoords);
}