#[macro_use]
#[allow(dead_code)]

extern crate rand;
extern crate glium;

mod geometry;

use std::thread;
use std::time::{Duration, Instant};
use glium::{DisplayBuild, Surface};
use glium::index::PrimitiveType;
use glium::glutin;

implement_vertex!(Vertex2f, position);

fn main() {
	const WINDOW_WIDTH: usize = 1280;
	const WINDOW_HEIGHT: usize = 1024;

	let mut display = glutin::WindowBuilder::new().build_glium().unwrap();
	let mut timeAccumulator = Duration::new(0, 0);
	let mut now = Instant::now();

	'main: loop {
		// Draw loop.
		let mut target = display.draw();
		target.clear_color(1.0, 0.0, 1.0, 1.0);
		target.finish.unwrap();

		// Event loop.
		for event in display.poll_events() {
			match event {
				glutin::Event::KeyboardInput(_, _, Some(glutin::VirtualKeyCode::Escape)) | glutin::Event::Closed => break 'main,
				_ => {},
			}
		}

		// Logic loop.
	}
}
