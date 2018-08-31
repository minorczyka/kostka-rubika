precision mediump float;

varying vec2 coord;
varying vec3 color;

const float border = 0.05;

void main() {
    if (coord.x < border || coord.x > (1.0 - border) || coord.y < border || coord.y > (1.0 - border)) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    } else {
        gl_FragColor = vec4(color, 1.0);
    }
}