#version 330

layout (location=0) in vec3 a_Position;
layout (location=1) in vec3 a_Color;

out vec3 v_Color;

uniform mat4 u_MVPMatrix;

//uniform mat4 u_ModelMatrix;

void main()
{
    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);
    v_Color = a_Color;
}
