
use glam::I64Vec2;

#[derive(Default)]
pub struct Level {
	pub sectors: Vec<Sector>,
	pub vertices: Vec<I64Vec2>,
}

#[derive(Clone, Debug, Default)]
pub struct Sector {
	pub walls: Vec<Wall>,
	pub floor_material: Option<Material>,
	pub ceiling_material: Option<Material>,
	pub ceiling_height: i64,
	pub floor_height: i64,
}

#[derive(Clone, Debug, Default)]
pub struct Wall {
	pub vertex: usize,
	pub material: Option<Material>,
	pub neighbor: Option<usize>,
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

		// 123
		// 054

		level.vertices.extend([
			I64Vec2 { x: 0, y: 0 },  // 0
			I64Vec2 { x: 0, y: 64 },  // 1
			I64Vec2 { x: 64, y: 64 },  // 2
			I64Vec2 { x: 128, y: 64 },  // 3
			I64Vec2 { x: 128, y: 0 },  // 4
			I64Vec2 { x: 64, y: 0 },  // 5
		]);

		let mut sector_a = Sector::default();
		sector_a.walls.extend([
			Wall { vertex: 0, neighbor: None, material: None, },
			Wall { vertex: 1, neighbor: None, material: None, },
			Wall { vertex: 2, neighbor: None, material: None, },
			Wall { vertex: 5, neighbor: Some(1), material: None, },
		]);

		let mut sector_b = Sector::default();
		sector_b.walls.extend([
			Wall { vertex: 2, neighbor: None, material: None, },
			Wall { vertex: 3, neighbor: None, material: None, },
			Wall { vertex: 4, neighbor: None, material: None, },
			Wall { vertex: 5, neighbor: Some(0), material: None, },
		]);

		level.sectors.push(sector_a);
		level.sectors.push(sector_b);

		level
	}
}
