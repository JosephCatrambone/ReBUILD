use macroquad::prelude::*;

pub struct Map {
	sectors: Vec<Sector>,
	walls: Vec<Wall>,
}

pub struct Sector {
	start_wall: usize,
	num_walls: usize,
	ceiling_height: i64,
	floor_height: i64,
}

pub struct Wall {
	start_point: I64Vec2,
	next_sector: Option<usize>,
	material: Material, 
}

pub struct Material {
	index: usize,
	offset: I64Vec2,
	rotation: f32,
	zoom: Vec2,
}

impl Map {

}

impl Sector {

}

impl Wall {
	fn draw(camera_offset: I64Vec2, camera_zoom: f32) {
		draw_line(40.0, 40.0, 100.0, 200.0, 15.0, BLUE);
		draw_rectangle(screen_width() / 2.0 - 60.0, 100.0, 120.0, 60.0, GREEN);
	}
}