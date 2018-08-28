#version 330
layout (location=0) in vec3 a_Position;
layout (location=1) in vec2 a_TextureCoords;
out vec2 v_TextureCoords;

uniform mat4 u_MVPMatrix;
void main()
{
    gl_Position = u_MVPMatrix  * vec4(a_Position, 1.0);
    v_TextureCoords = a_TextureCoords;
}
