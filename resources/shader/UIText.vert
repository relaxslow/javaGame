#version 330
layout (location=1) in vec3 a_Position;
layout (location=1) in vec2 a_TexCoords;
//layout (location=2) in vec3 vertexNormal;

uniform mat4 u_MVPMatrix;


out vec2 v_TexCoords;

void main()
{
//     gl_Position = vec4(a_Position, 1.0);
    gl_Position = u_MVPMatrix * vec4(a_Position, 1.0);
    v_TexCoords = a_TexCoords;
}
