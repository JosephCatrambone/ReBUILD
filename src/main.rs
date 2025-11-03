
mod tools;
mod level;
mod camera;

use glam::{Vec2, I64Vec2};
use notan::draw::*;
use notan::prelude::*;

#[derive(AppState)]
struct State {
	// 3D Rendering:
	clear_options: ClearOptions,
	vbo: Buffer,
	pipeline: Pipeline,

	// 2D Rendering:
	font: Font,

	// Internal Representation:
	prev_mouse: Vec2,
	render_3d: bool,
	tool: Option<Box<dyn tools::Tool>>,
	map: level::Level,
}

#[notan_main]
fn main() -> Result<(), String> {
	notan::init_with(setup)
		.add_config(DrawConfig)
		.update(update)
		.draw(draw)
		.build()
}

fn setup(gfx: &mut Graphics) -> State {
	let font = gfx
		.create_font(include_bytes!("../assets/BebasNeue-Regular.ttf"))
		.unwrap();

	let clear_options = ClearOptions::color(Color::new(0.1, 0.2, 0.3, 1.0));

	let vertex_info = VertexInfo::new()
		.attr(0, VertexFormat::Float32x2)
		.attr(1, VertexFormat::Float32x3);

	let pipeline = gfx
		.create_pipeline()
		.from(&VERT, &FRAG)
		.with_vertex_info(&vertex_info)
		.build()
		.unwrap();

	#[rustfmt::skip]
    let vertices = [
        0.5, 1.0,   1.0, 0.2, 0.3,
        0.0, 0.0,   0.1, 1.0, 0.3,
        1.0, 0.0,   0.1, 0.2, 1.0,
    ];

	let vbo = gfx
		.create_vertex_buffer()
		.with_info(&vertex_info)
		.with_data(&vertices)
		.build()
		.unwrap();


	State {
		clear_options: clear_options,
		vbo: vbo,
		pipeline: pipeline,
		font: font,
		render_3d: false,
		prev_mouse: Vec2::ZERO,
		tool: None,
		map: level::Level::new(),
	}
}

fn update(app: &mut App, state: &mut State) {
	// get mouse cursor position here
	let (x, y) = app.mouse.position();

	if app.mouse.was_pressed(MouseButton::Left) {
		//state.left.push((x, y));
	}

	if app.keyboard.was_released(KeyCode::R) {
		state.render_3d = !state.render_3d;
	}

	state.prev_mouse.x = x;
	state.prev_mouse.y = y;
}

fn draw(gfx: &mut Graphics, state: &mut State) {
	if state.render_3d {
		draw3d(gfx, state);
	} else {
		draw2d(gfx, state);
	}
}

fn draw2d(gfx: &mut Graphics, state: &mut State) {
	let mut draw = gfx.create_draw();
	draw.clear(Color::BLACK);

	// Draw cursor
	draw.circle(8.0)
		.position(state.prev_mouse.x, state.prev_mouse.y)
		.color(Color::ORANGE);

	// Draw left clicks
	//state.left.iter().for_each(|(x, y)| {
	//	draw.circle(4.0).position(*x, *y).color(Color::RED);
	//});

	// Draw Grid
	// Draw Level
	for sector in &state.map.sectors {

	}

	// Draw position
	let text = format!("x: {} - y: {}", state.prev_mouse.x, state.prev_mouse.y);
	draw.text(&state.font, &text)
		.position(10.0, 10.0)
		.size(16.0)
		.h_align_left()
		.v_align_middle();

	gfx.render(&draw);
}

fn draw3d(gfx: &mut Graphics, state: &mut State) {
	let mut renderer = gfx.create_renderer();

	renderer.begin(Some(state.clear_options));
	renderer.set_pipeline(&state.pipeline);
	renderer.bind_buffer(&state.vbo);
	renderer.draw(0, 3);
	renderer.end();

	gfx.render(&renderer);
}

//language=glsl
const VERT: ShaderSource = notan::vertex_shader! {
    r#"
    #version 450
    layout(location = 0) in vec3 a_pos;
    layout(location = 1) in vec3 a_color;

    layout(location = 0) out vec3 v_color;

    void main() {
        v_color = a_color;
        gl_Position = vec4(a_pos, 1.0);
    }
    "#
};

//language=glsl
const FRAG: ShaderSource = notan::fragment_shader! {
    r#"
    #version 450
    precision mediump float;

    layout(location = 0) in vec3 v_color;
    layout(location = 0) out vec4 color;

    void main() {
        color = vec4(v_color, 1.0);
    }
    "#
};
