
pub mod Menu;

pub trait Scene {
	fn render(&self, deltaTime : f32) -> ();
	fn update(&mut self, deltaTime : f32) -> ();
}
