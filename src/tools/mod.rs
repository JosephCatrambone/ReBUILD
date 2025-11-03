use crate::level::Level;

pub trait Tool {
	fn start(&mut self, map: &Level);
	fn tick(&mut self, map: &Level); // Called every frame.
	fn update(&mut self, map: &Level); // Called every action.
	fn finish(&mut self, map: &mut Level);
}