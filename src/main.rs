#[allow(dead_code)]
#[allow(unused_imports)]

extern crate rand;
#[macro_use]
extern crate glium;
#[macro_use]
extern crate imgui;

mod mesh;
mod linearalgebra;

use std::sync::{Arc, Mutex};
use std::thread;
use std::time::{Duration, Instant};
use glium::{DisplayBuild, Surface};
use glium::index::PrimitiveType;
use glium::glutin;
use imgui::{ImGui, Ui, ImGuiKey};
use imgui::glium_renderer::Renderer;

use mesh::*;
use linearalgebra::*;

fn main() {
	const WINDOW_WIDTH: usize = 1280;
	const WINDOW_HEIGHT: usize = 1024;

	let mut delta_time : f32 = 0f32;

	let mut display = glutin::WindowBuilder::new().build_glium().unwrap();
	let mut imgui = ImGui::init();
	let ui_renderer = Renderer::init(&mut imgui, &display).unwrap();
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
		ui_renderer.render(&mut target, imgui.frame(window.get_inner_size_points().unwrap(), window.get_inner_size_pixels().unwrap(), delta_time/1.0e6f32));

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
		delta_time = draw_start.elapsed().as_secs() as f32;

		// Lock to 60fps.
		if delta_time < frame_delay {
			thread::sleep(Duration::from_millis(((frame_delay as f32 - delta_time as f32) as u64 * 1000u64)));
		}
	}
}
