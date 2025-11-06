use glam::I64Vec2;

#[derive(Clone, Debug, Default)]
pub struct Level {
	sectors: Vec<Sector>,
	walls: Vec<Wall>,
	corners: Vec<Corner>,
}

#[derive(Clone, Debug, Default)]
pub struct Corner {
	vertex: I64Vec2,
	walls: Vec<usize>, // Weak ref
}

#[derive(Clone, Debug, Default)]
pub struct Wall {
	start_vertex: usize,
	end_vertex: usize,
	pub material: Option<Material>,
	pub neighbor: Option<usize>,
}

#[derive(Clone, Debug, Default)]
pub struct Sector {
	corners: Vec<usize>,
	walls: Vec<usize>,
	floor_material: Option<Material>,
	ceiling_material: Option<Material>,
	ceiling_height: i64,
	floor_height: i64,
}

#[derive(Clone, Debug, Default)]
pub struct Material {
	pub index: usize,
	pub offset: I64Vec2,
	pub rotation: f32,
	pub zoom: I64Vec2,
}

impl Level {
	pub fn new() -> Self {
		let mut level = Self::default();

		// TODO: Some mods here.

		level
	}

	pub fn point_exists(&self, p: &I64Vec2) -> bool {
		for c in &self.corners {
			if &c.vertex == p {
				return true;
			}
		}
		false
	}

	pub fn corner_exists(&self, c: &Corner) -> bool {
		self.point_exists(&c.vertex)
	}

	pub fn add_sector(&mut self, sector: Sector) {}

	pub fn add_sector_partial(&mut self, sector: Sector) {}

	pub fn get_wall_point_pair_iterator(&self) -> LevelWallPointPairIterator<'_> {
		LevelWallPointPairIterator {
			level_ref: &self,
			wall_idx: 0,
		}
	}


}


pub struct LevelWallPointPairIterator<'a> {
	level_ref: &'a Level,
	wall_idx: usize,
}


impl<'a> Iterator for LevelWallPointPairIterator<'a> {
	type Item = (I64Vec2, I64Vec2);

	fn next(&mut self) -> Option<Self::Item> {
		if self.wall_idx >= self.level_ref.walls.len() { return None; }

		let pair = if let Some(current) = self.level_ref.walls.get(self.wall_idx) {
			let a = self.level_ref.corners[current.start_vertex].vertex;
			let b = self.level_ref.corners[current.end_vertex].vertex;
			Some((a, b))
		} else {
			None
		};

		if let Some(new_idx) = self.wall_idx.checked_add(1) {
			self.wall_idx = new_idx;
		} else {
			panic!("Levels with more than 2^64 walls are not supported.");
		}

		pair
	}
}

