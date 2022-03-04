package com.mrbysco.cursedloot.util.info;

import net.minecraft.resources.ResourceLocation;

public class CurseLocation {
	private final ResourceLocation resource;
	private final CursePos position;

	public CurseLocation(ResourceLocation resource, CursePos pos) {
		this.resource = resource;
		this.position = pos;
	}

	public CurseLocation(ResourceLocation resource) {
		this.resource = resource;
		this.position = new CursePos(0, 0);
	}

	public ResourceLocation getResource() {
		return resource;
	}

	public CursePos getPosition() {
		return position;
	}
}
