uniform mat4 model;
uniform mat4 viewProjection;

attribute vec4 position;
attribute vec3 normal;
attribute vec2 coords;
attribute vec3 colors;

varying vec2 coord;
varying vec3 color;

void main() {
    gl_Position = viewProjection * model * position;
    coord = coords;
    color = colors;
}