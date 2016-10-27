//#[allow(dead_code)]
//#[allow(unused_imports)]

// For linear algebra submodule.
// Not yet supported.
//#![feature(collections)]
//#![feature(collections_range)]
//extern crate collections;

extern crate rand;
#[macro_use]
extern crate glium;
#[macro_use]
extern crate rayon;

mod mesh;
mod linearalgebra;
mod scene;
mod computegraph;

use std::env;
use std::sync::{Arc, Mutex};
use std::thread;
use std::time::{Duration, Instant};

use glium::{DisplayBuild, Surface};
use glium::index::PrimitiveType;
use glium::glutin;

use mesh::*;
use linearalgebra::*;
use scene::*;

// Maybe we should call this library ReBar because it's a foundation for so many of my apps.
fn main() {
	const WINDOW_WIDTH: usize = 1280;
	const WINDOW_HEIGHT: usize = 1024;

	let args: Vec<String> = env::args().map(|arg| arg.to_owned()).collect();

	let mut delta_time : f32 = 0f32;

	let mut display = glutin::WindowBuilder::new().build_glium().unwrap();
	let mut mesh_renderer = MeshRenderer::new(&display); // Holds mesh data and vertex buffers.  Pass reference to display for building buffers.
	let mut scene_stack : Vec<Box<Scene>> = Vec::new(); // TODO: Get this working.

	//let mut timeAccumulator = Duration::new(0, 0);
	//let mut now = Instant::now();

	let frame_delay : f32 = 1.0f32/60.0f32; //Duration::from_millis(16u64);
	let vertex_shader_src = include_str!("vert_shader.cl");
	let fragment_shader_src = include_str!("frag_shader.cl");
	let program = glium::Program::from_source(&display, vertex_shader_src, fragment_shader_src, None).unwrap();

	scene_stack.push(Box::new(Menu::MainMenu::new()));

	'main: loop {
		// Start stopwatch.
		let draw_start = Instant::now();

		// Draw loop.
		let mut target = display.draw();
		target.clear_color(1.0, 0.0, 1.0, 1.0);

		// Update UI.
		let window = display.get_window().unwrap();
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
