
use glium::Frame;

pub mod Menu;

pub trait Scene {
	fn render(&self, surface: &mut Frame, deltaTime : f32) -> ();
	fn update(&mut self, deltaTime : f32) -> ();
}
