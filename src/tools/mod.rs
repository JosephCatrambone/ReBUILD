mod draw;

use notan::draw::Draw;
use crate::level::Level;

pub trait Tool {
	fn draw(&self, ctx: &mut Draw);
	fn start(&mut self, map: &Level);
	fn tick(&mut self, map: &Level); // Called every frame.
	fn update(&mut self, map: &Level); // Called every action.
	fn finish(&mut self, map: &mut Level);
}