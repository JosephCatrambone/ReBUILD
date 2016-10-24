#[allow(dead_code)]
#[allow(unused_imports)]

extern crate rand;
#[macro_use]
extern crate glium;
#[macro_use]
extern crate imgui;

mod mesh;
mod linearalgebra;
mod scene;

use std::sync::{Arc, Mutex};
use std::thread;
use std::time::{Duration, Instant};
use glium::{DisplayBuild, Surface};
use glium::index::PrimitiveType;
use glium::glutin;
use imgui::*;
use imgui::glium_renderer::Renderer;

use mesh::*;
use linearalgebra::*;
use scene::*;

fn main() {
	const WINDOW_WIDTH: usize = 1280;
	const WINDOW_HEIGHT: usize = 1024;

	let mut delta_time : f32 = 0f32;

	let mut display = glutin::WindowBuilder::new().build_glium().unwrap();
	let mut mesh_renderer = MeshRenderer::new(); // Also holds mesh data.
	let mut scene_stack : Vec<Box<Scene>> = vec![]; // TODO: Get this working.

	let mut imgui = ImGui::init();
	let mut ui_renderer = Renderer::init(&mut imgui, &display).unwrap();
	
	//let mut timeAccumulator = Duration::new(0, 0);
	//let mut now = Instant::now();

	let frame_delay : f32 = 1.0f32/60.0f32; //Duration::from_millis(16u64);
	let vertex_shader_src = include_str!("vert_shader.cl");
	let fragment_shader_src = include_str!("frag_shader.cl");
	let program = glium::Program::from_source(&display, vertex_shader_src, fragment_shader_src, None).unwrap();

	'main: loop {
		// Start stopwatch.
		let draw_start = Instant::now();

		// Draw loop.
		let mut target = display.draw();
		target.clear_color(1.0, 0.0, 1.0, 1.0);

		// Update UI.
		let window = display.get_window().unwrap();
		let ui_frame = imgui.frame(window.get_inner_size_points().unwrap(), window.get_inner_size_pixels().unwrap(), delta_time/1.0e6f32);
		ui_frame.window(im_str!("TITLE!"))
			.size((300.0, 100.0), ImGuiSetCond_FirstUseEver)
			.build(|| {
				ui_frame.text(im_str!("Hello world!"));
				ui_frame.separator();
				ui_frame.text(im_str!(" ~~~~ "));
			});
		ui_renderer.render(&mut target, ui_frame);

		target.finish().unwrap();

		// Event loop.
		for event in display.poll_events() {
			match event {
				glutin::Event::KeyboardInput(_, _, Some(glutin::VirtualKeyCode::Escape)) | glutin::Event::Closed => break 'main,
				glutin::Event::KeyboardInput(_, _, Some(x)) => {},
				glutin::Event::MouseMoved(x, y) => { 
					//let scale = imgui_ref.display_framebuffer_scale();
					//imgui_ref.set_mouse_pos(x as f32 / scale.0 as f32, y as f32 / scale.1 as f32);
				},
				_ => {},
			}
		}

//self.imgui.set_mouse_down(&[self.mouse_pressed.0, self.mouse_pressed.1, self.mouse_pressed.2, false, false]);
//self.imgui.set_mouse_wheel(self.mouse_wheel / scale.1);
//self.mouse_wheel = 0.0;

		// Logic loop.

		// Delay.
		// Frame finish time.
		delta_time = draw_start.elapsed().as_secs() as f32;

		// Lock to 60fps.
		if delta_time < frame_delay {
			thread::sleep(Duration::from_millis(((frame_delay as f32 - delta_time as f32) as u64 * 1000u64)));
		}
	}
}
