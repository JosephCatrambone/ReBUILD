
mod tools;
mod level;
mod camera;

use glam::{Vec2, I64Vec2};
use notan::draw::*;
use notan::prelude::*;
use crate::camera::Camera2D;

const NEIGHBORLESS_COLOR: Color = Color::WHITE;
const NEIGHBORED_COLOR: Color = Color::PINK;
const GRID_COLOR: Color = Color::GRAY;

#[derive(AppState)]
struct State {
	// 3D Rendering:
	clear_options: ClearOptions,
	vbo: Buffer,
	pipeline: Pipeline,

	// 2D Rendering:
	font: Font,
	camera2d: Camera2D,

	// Internal Representation:
	prev_mouse: Vec2,
	mouse_press_started: Option<Vec2>,
	render_3d: bool,
	tool: Option<Box<dyn tools::Tool>>,
	map: level::Level,
}

#[notan_main]
fn main() -> Result<(), String> {
	notan::init_with(setup)
		.add_config(DrawConfig)
		.event(event)
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
		.attr(0, VertexFormat::Float32x3)
		.attr(1, VertexFormat::Float32x3);

	let pipeline = gfx
		.create_pipeline()
		.from(&VERT, &FRAG)
		.with_vertex_info(&vertex_info)
		.build()
		.unwrap();

	#[rustfmt::skip]
    let vertices = [
        0.5, 1.0, 0.0,   1.0, 0.2, 0.3,
        0.0, 0.0, 0.0,   0.1, 1.0, 0.3,
        1.0, 0.0, 0.0,   0.1, 0.2, 1.0,
    ];

	let vbo = gfx
		.create_vertex_buffer()
		.with_info(&vertex_info)
		.with_data(&vertices)
		.build()
		.unwrap();

	let mut camera = Camera2D::new(gfx.size().0 as f32, gfx.size().1 as f32);
	camera.snap_level = 16;

	State {
		clear_options: clear_options,
		vbo: vbo,
		pipeline: pipeline,

		font: font,
		camera2d: camera, // We're not using draw.transform().push(container.matrix()) here.

		render_3d: false,
		prev_mouse: Vec2::ZERO,
		mouse_press_started: None,
		tool: None,
		map: level::Level::new(),
	}
}

fn event(state: &mut State, event: Event) {
	match event {
		Event::ReceivedCharacter(c) if c != '\u{7f}' => {
			//state.msg.push(c);
		},
		Event::MouseMove { .. } => {
		},
		Event::MouseDown { button, .. } => {
		},
		Event::MouseUp { button, .. } => {
		},
		Event::MouseEnter { .. } => {
		},
		Event::MouseLeft { .. } => {
		},
		Event::MouseWheel { .. } => {
		},
		_ => {}
	}
}

fn update(app: &mut App, state: &mut State) {
	if state.render_3d {

	} else {
		update2d(app, state);
	}
}

fn update2d(app: &mut App, state: &mut State) {
	// Mouse Inputs
	let (mouse_x, mouse_y) = app.mouse.position();
	let (mouse_dx, mouse_dy) = app.mouse.motion_delta;

	if app.mouse.was_pressed(MouseButton::Left) {
		//state.left.push((x, y));
	}
	if app.mouse.middle_is_down() { // was_pressed(MouseButton::Middle)? is_down(Middle)?
		state.camera2d.move_camera(mouse_dx as f32, mouse_dy as f32);
	}

	if app.mouse.is_scrolling() {
		let delta_x = app.mouse.wheel_delta.x;
		let delta_y = app.mouse.wheel_delta.y;
		/*
		if delta_y > 0f32 {
			state.camera2d.scale *= 1.25;
		} else {
			state.camera2d.scale *= 0.75;
		}
		state.camera2d.scale = state.camera2d.scale.max(1.0 / 2048f32);
		*/

		//state.x = (state.x + delta_x).max(0.0).min(800.0);
		//state.y = (state.y + delta_y).max(0.0).min(600.0);
	}

	// Keyboard Inputs:
	if app.keyboard.was_released(KeyCode::R) {
		state.render_3d = !state.render_3d;
	}
	if app.keyboard.was_released(KeyCode::Key0) {
		state.camera2d.reset();
	}

	// Closing and cleanup:
	state.prev_mouse.x = mouse_x;
	state.prev_mouse.y = mouse_y;
}

fn update3d(app: &mut App, state: &mut State) {
	if app.keyboard.was_released(KeyCode::R) {
		state.render_3d = !state.render_3d;
	}
}

fn draw(gfx: &mut Graphics, state: &mut State) {
	if state.render_3d {
		draw3d(gfx, state);
	} else {
		draw2d(gfx, state);
	}
}

fn draw2d(gfx: &mut Graphics, state: &mut State) {
	let mut ctx = gfx.create_draw();
	ctx.clear(Color::BLACK);

	// Draw an invisible rect so we can get the position in local space.
	{
		//let mut rect = ctx.rect((0.0, 0.0), (gfx.size().0 as f32, gfx.size().1 as f32));
		//let local = rect.screen_to_local_position(state.mouse.x, app.mouse.y);
	}

	// Draw the grid.
	/*
	let (screen_width, screen_height) = gfx.size();
	for y in (0i64..screen_height as i64).step_by(state.camera2d.snap_level as usize) {
		let dy = (state.camera2d.center.y as i64) % state.camera2d.snap_level as i64;
		ctx.line((0f32, (y + dy) as f32), (screen_width as f32, (y + dy) as f32)).color(GRID_COLOR);
	}
	*/

	// Draw cursor
	ctx.circle(8.0)
		.position(state.prev_mouse.x, state.prev_mouse.y)
		.color(Color::ORANGE);

	// Draw left clicks
	//state.left.iter().for_each(|(x, y)| {
	//	draw.circle(4.0).position(*x, *y).color(Color::RED);
	//});

	// Draw Grid
	// Draw Level
	for (start, end) in state.map.get_wall_point_pair_iterator() {
		let a = state.camera2d.world_to_screen(start);
		let b = state.camera2d.world_to_screen(end);
		ctx.line(a, b).color(NEIGHBORLESS_COLOR);
	}

	// Draw position
	let text = format!("x: {} - y: {}", state.prev_mouse.x, state.prev_mouse.y);
	ctx.text(&state.font, &text)
		.position(10.0, 10.0)
		.size(16.0)
		.h_align_left()
		.v_align_middle();

	gfx.render(&ctx);
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

/*
fn draw(gfx: &mut Graphics, state: &mut State) {
    // update the data that will be sent to the gpu
    update_bytes(state);

    // Update the texture with the new data
    gfx.update_texture(&mut state.texture)
        .with_data(&state.bytes)
        .update()
        .unwrap();

    // Draw the texture using the draw 2d API for convenience
    let mut draw = gfx.create_draw();
    draw.clear(Color::BLACK);
    draw.image(&state.texture);
    gfx.render(&draw);
}

fn update_bytes(state: &mut State) {
    for _ in 0..100 {
        let index = state.count * 4;
        state.bytes[index..index + 4].copy_from_slice(&state.color.rgba_u8());
        state.count += 9;

        let len = state.bytes.len() / 4;
        if state.count >= len {
            state.count -= len;
            state.step = (state.step + 1) % COLORS.len();
            state.color = COLORS[state.step];
        }
    }
}
 */