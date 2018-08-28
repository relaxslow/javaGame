#version 330

layout (location=0) in vec3 a_Position;
layout (location=1) in vec3 a_Color;
layout (location=2) in vec2 a_TexCoords;

uniform mat4 u_MVPMatrix;

//uniform mat4 u_ModelMatrix;
out vec2 v_TexCoords;
out vec3 v_Color;
void main()
{
    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);
    v_TexCoords=a_TexCoords;
    v_Color=a_Color;
}