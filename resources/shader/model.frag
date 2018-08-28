#version 330
in vec2 v_TexCoords;
in vec3 v_Color;
out vec4 fragColor;
uniform sampler2D u_Texture01;

uniform int u_UseTexture;
void main()
{
    if ( u_UseTexture == 1 ){
        fragColor = texture(u_Texture01, v_TexCoords);
    }else{
        fragColor = vec4(v_Color,1.0f);
    }

}