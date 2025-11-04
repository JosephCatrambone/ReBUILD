
use glam::{Vec2, I64Vec2};

#[derive(Clone, Debug, Default)]
pub struct Camera2D {
	pub offset: Vec2,
	pub scale: f32,
	pub snap_level: u64,
}

impl Camera2D {
	pub fn screen_to_local(&self, x: f32, y: f32, snap: bool) -> I64Vec2 {
		let unsnapped = I64Vec2::new(
			((x + self.offset.x) * self.scale) as i64,
			((y + self.offset.y) * self.scale) as i64,
		);

		let snapped = if self.snap_level > 1 && snap {
			I64Vec2::new(
				// TODO: This probably doesn't work for negatives.
				unsnapped.x - (unsnapped.x % (self.snap_level as i64)),
				unsnapped.y - (unsnapped.y % (self.snap_level as i64)),
			)
		} else {
			unsnapped
		};

		snapped
	}

	pub fn local_to_screen(&self, pt: I64Vec2) -> (f32, f32) {
		let x = (pt.x as f32 / self.scale) - self.offset.x;
		let y = (pt.y as f32 / self.scale) - self.offset.y;
		(x, y)
	}

	pub fn snap_float(&self, value: f32) -> i64 {
		(value / self.snap_level as f32).round() as i64 * self.snap_level as i64
	}
}