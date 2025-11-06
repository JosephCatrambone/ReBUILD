
use glam::{Vec2, I64Vec2};

#[derive(Clone, Debug, Default)]
pub struct Camera2D {
	center: Vec2, // What is in the center of this view?
	viewport_width: f32, // What part of the world do I see in this rect?
	screen_width: f32,
	aspect_ratio: f32, // w/h
	pub snap_level: u32,
}

impl Camera2D {
	pub fn new(screen_width: f32, screen_height: f32) -> Self {
		let aspect_ratio = screen_width / screen_height;
		Camera2D {
			center: Vec2::ZERO,
			viewport_width: screen_width,
			screen_width: screen_width,
			aspect_ratio: aspect_ratio,
			snap_level: 1,
		}
	}

	pub fn screen_resized(&mut self, screen_width: f32, screen_height: f32) {
		self.aspect_ratio = screen_width / screen_height;
		self.screen_width = screen_width;
	}

	pub fn screen_to_world(&self, x: f32, y: f32, snap: bool) -> I64Vec2 {
		let x_norm = x / self.screen_width;
		let y_norm = x / (self.screen_width / self.aspect_ratio);
		// x_norm,y_norm in (0, 1).  Map to -0.5, 0.5, then to the size of the rect before offsetting.
		let x_world = ((x_norm - 0.5) * self.viewport_width) - self.center.x;
		let y_world = ((y_norm - 0.5) * (self.viewport_width / self.aspect_ratio)) - self.center.y;

		let mut x_snapped: i64 = x_world as i64;
		let mut y_snapped: i64 = y_world as i64;

		if snap && self.snap_level > 1 {
			x_snapped = (x_snapped / self.snap_level as i64) * self.snap_level as i64;
			y_snapped = (y_snapped / self.snap_level as i64) * self.snap_level as i64;
		}
		I64Vec2::new(x_snapped, y_snapped)
	}

	pub fn world_to_screen(&self, pt: I64Vec2) -> (f32, f32) {
		let x_world = pt.x as f32;
		let y_world = pt.y as f32;
		let viewport_height = self.viewport_width / self.aspect_ratio;
		let screen_height = self.screen_width / self.aspect_ratio;

		let x_local = ((x_world + self.center.x) / self.viewport_width) + 0.5f32;
		let y_local = ((y_world + self.center.y) / viewport_height) + 0.5f32;

		(x_local * self.screen_width, y_local * screen_height)
	}

	pub fn snap_float(&self, value: f32) -> i64 {
		(value / self.snap_level as f32).round() as i64 * self.snap_level as i64
	}

	pub fn move_camera(&mut self, dx: f32, dy: f32) {
		self.center.x += dx;
		self.center.y += dy;
	}

	pub fn reset(&mut self) {
		self.center = Vec2::ZERO;
		self.viewport_width = self.screen_width;
	}
}