use notan::draw::Draw;
use notan::math::I64Vec2;
use crate::level::Level;
use crate::tools::Tool;

struct DrawTool {
	corners: Vec<I64Vec2>,
}

impl DrawTool {
	pub fn new() -> Self {
		Self { corners: vec![] }
	}
}

impl Tool for DrawTool {
	fn draw(&self, ctx: &mut Draw) {
		todo!()
	}

	fn start(&mut self, map: &Level) {
		todo!()
	}

	fn tick(&mut self, map: &Level) {
		todo!()
	}

	fn update(&mut self, map: &Level) {
		todo!()
	}

	fn finish(&mut self, map: &mut Level) {
		todo!()
	}
}