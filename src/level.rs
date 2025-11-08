use glam::I64Vec2;

#[derive(Clone, Debug, Default)]
pub struct Level {
	sectors: Vec<Sector>,
	walls: Vec<Wall>,
	corners: Vec<I64Vec2>,
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

	pub fn get_corner_idx(&self, p: &I64Vec2) -> Option<usize> {
		for (idx, c) in self.corners.iter().enumerate() {
			if c == p {
				return Some(idx);
			}
		}
		None
	}

	pub fn corner_exists(&self, c: &I64Vec2) -> bool {
		self.get_corner_idx(c).is_some()
	}

	pub fn add_corner(&mut self, c: I64Vec2) -> usize {
		// Adds a new corner or, if it exists already, returns an index for it.
		if let Some(existing_idx) = self.get_corner_idx(&c) {
			existing_idx
		} else {
			let idx = self.corners.len();
			self.corners.push(c);
			idx
		}
	}

	pub fn add_wall(&mut self, a: I64Vec2, b: I64Vec2) -> usize {
		let start = self.add_corner(a);
		let end = self.add_corner(b);
		// TODO: Walls are unordered, right?  AB == BA
		for (wall_idx, w) in self.walls.iter().enumerate() {
			if (w.start_vertex == start && w.end_vertex == end) || (w.start_vertex == end && w.end_vertex == start) {
				return wall_idx;
			}
		}
		// The wall doesn't exist.
		let new_wall_idx = self.walls.len();
		self.walls.push(Wall {
			start_vertex: start,
			end_vertex: end,
			material: None,
			neighbor: None, // TODO: Real neighbor.
		});
		new_wall_idx
	}

	pub fn add_sector(&mut self, sector: Sector) {

	}

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
			let a = self.level_ref.corners[current.start_vertex];
			let b = self.level_ref.corners[current.end_vertex];
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

