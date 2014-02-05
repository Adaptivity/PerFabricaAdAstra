package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Formula {
	public final List<Part> parts;
	
	public Formula(List<? extends PartFactory> parts) {
		List<Part> reifiedParts = new ArrayList();
		for (PartFactory part : parts) {
			reifiedParts.add(part.getPart());
		}
		this.parts = Collections.unmodifiableList(reifiedParts);
	}
	public Formula(PartFactory... parts) {
		this(Arrays.asList(parts));
	}
	
	/* First/last seems a pragmatic way of interpreting simple formulae,
	 * i.e., those representing simple salts and coordination compounds.
	 */
	
	public Part getFirstPart() {
		return parts.get(0);
	}
	
	public Part getLastPart() {
		return parts.get(parts.size()-1);
	}
	
	public Formula setFirstPart(Part newPart) {
		List<Part> newParts = new ArrayList(this.parts);
		newParts.set(0, newPart);
		return new Formula(newParts);
	}
	
	public Formula setLastPart(Part newPart) {
		List<Part> newParts = new ArrayList(this.parts);
		newParts.set(newParts.size()-1, newPart);
		return new Formula(newParts);
	}
	
	public String toString() {
		String name = "";
		for (Part part : parts) {
			name += part.toString();
		}
		return name;
	}

	public double getMolarMass() {
		double mass = 0;
		for (Part part : parts) {
			mass += part.getMolarMass();
		}
		return mass;
	}

	public static final class Part implements PartFactory {
		public final Element element;
		public final int stoichiometry;
		public final List<Part> parts;
		
		private Part(Element element, int stoichiometry, List<Part> parts) {
			this.parts = Collections.unmodifiableList(parts);
			this.stoichiometry = stoichiometry;
			this.element = element;
		}
		
		public Part(Element element, int stoichiometry) {
			this.element = element;
			this.stoichiometry = stoichiometry;
			this.parts = Collections.EMPTY_LIST;
		}
		
		public Part(PartFactory... parts) {
			List<Part> reifiedParts = new ArrayList();
			for (PartFactory part : parts) {
				reifiedParts.add(part.getPart());
			}
			this.parts = Collections.unmodifiableList(reifiedParts);
			this.stoichiometry = 1;
			this.element = null;
		}
		
		public Part _(int stoichiometry) {
			return new Part(element, stoichiometry, parts); 
		}
		
		public double getMolarMass() {
			double mass = 0;
			if (element != null) {
				mass = element.getAtomicWeight();
			} else {
				for (Part part : parts) {
					mass += part.getMolarMass();
				}
			}
			mass *= stoichiometry;
			return mass;
		}

		public String toString() {
			String name = "";
			if (element != null)
				name = element.name();
			else {
				for (Part part : parts) {
					name += part.toString();
				}
				if (parts.size() > 1)
					name = "(" + name + ")";
			}
			if (stoichiometry > 1)
				name += stoichiometry;
			return name;
		}
		
		public boolean equals(Object other) {
			return other instanceof Part && other.toString() == this.toString();
		}
		
		@Override
		public Part getPart() {
			return this;
		}
	}
	
	public static interface PartFactory {
		public Part getPart();
	}

}
