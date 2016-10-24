
pub trait Scene {
	fn render(screen : &Drawable, deltaTime : f32) -> ();
	fn update(deltaTime : f32) -> ();
}