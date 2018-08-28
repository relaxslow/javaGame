#version 330
out vec4 fragColor;
uniform vec4 u_Color;
void main() {
    fragColor =u_Color;
//    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}