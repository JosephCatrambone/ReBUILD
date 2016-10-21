#[macro_use]
#[allow(dead_code)]
#[allow(unused_imports)]

extern crate rand;
extern crate glium;

mod geometry;

use std::sync::{Arc, Mutex};
use std::thread;
use std::time::{Duration, Instant};
use glium::{DisplayBuild, Surface};
use glium::index::PrimitiveType;
use glium::glutin;

use geometry::Vertex2f;

fn main() {
	const WINDOW_WIDTH: usize = 1280;
	const WINDOW_HEIGHT: usize = 1024;

	let mut display = glutin::WindowBuilder::new().build_glium().unwrap();
	//let mut timeAccumulator = Duration::new(0, 0);
	//let mut now = Instant::now();

	let frame_delay = Duration::from_millis(16u64);
	let vertex_shader_src = include_str!("vert_shader.cl");
	let fragment_shader_src = include_str!("frag_shader.cl");
	let program = glium::Program::from_source(&display, vertex_shader_src, fragment_shader_src, None).unwrap();

	'main: loop {
		// Start stopwatch.
		let draw_start = Instant::now();

		// Draw loop.
		let mut target = display.draw();
		target.clear_color(1.0, 0.0, 1.0, 1.0);
		target.finish().unwrap();

		// Event loop.
		for event in display.poll_events() {
			match event {
				glutin::Event::KeyboardInput(_, _, Some(glutin::VirtualKeyCode::Escape)) | glutin::Event::Closed => break 'main,
				_ => {},
			}
		}

		// Logic loop.

		// Delay.
		// Frame finish time.
		let delta_time = draw_start.elapsed();

		// Lock to 60fps.
		if delta_time < frame_delay {
			thread::sleep(frame_delay - delta_time);
		}
	}
}
