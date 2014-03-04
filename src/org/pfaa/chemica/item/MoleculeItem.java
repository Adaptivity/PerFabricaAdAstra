package org.pfaa.chemica.item;

import org.pfaa.chemica.model.Compound.Compounds;

public class MoleculeItem extends IndustrialMaterialItem<Compounds> {
	public MoleculeItem(int id) {
		super(id, Compounds.class);
	}
}
