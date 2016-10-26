
use scene::Scene;

pub struct MainMenu {
	test : u32,
}

impl MainMenu {
	pub fn new() -> MainMenu {
		MainMenu {
			test : 1234,
		}
	}
}

impl Scene for MainMenu {
	fn render(&self, deltaTime : f32) -> () {}
	fn update(&mut self, deltaTime : f32) -> () {}
}

