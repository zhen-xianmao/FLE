package farcore.substance;

import farcore.collection.ArrayStandardStackList;

public enum Atom implements IParticle
{
	Nt("Neutron"),
	H("Hydrogen"),
	He("Helium"),
	Li("Lithium"),
	Be("Beryllium"),
	B("Boron"),
	C("Carbon"),
	N("Nitrogen"),
	O("Oxygen"),
	F("Fluorine"),
	Ne("Neon"),
	Na("Sodium"),
	Mg("Magnesium"),
	Al("Aluminium"),
	Si("Silicon"),
	P("Phosphorus"),
	S("Sulfur"),
	Cl("Chlorine"),
	Ar("Argon"),
	K("Potassium"),
	Ca("Calcium"),
	Sc("Scandium"),
	Ti("Titanium"),
	V("Vanadium"),
	Cr("Chromium"),
	Mn("Manganese"),
	Fe("Iron"),
	Co("Cobalt"),
	Ni("Nickel"),
	Cu("Copper"),
	Zn("Zinc"),
	Ga("Gallium"),
	Ge("Germanium"),
	As("Arsenic"),
	Se("Selenium"),
	Br("Bromine"),
	Kr("Krypton"),
	Rb("Rubidium"),
	Sr("Strontium"),
	Y("Yttrium"),
	Zr("Zirconium"),
	Nb("Niobium"),
	Mo("Molybdenum"),
	Tc("Technetium"),
	Ru("Ruthenium"),
	Rh("Rhodium"),
	Pd("Palladium"),
	Ag("Silver"),
	Cd("Cadmium"),
	In("Indium"),
	Sn("Tin"),
	Sb("Antimony"),
	Te("Tellurium"),
	I("Iodine"),
	Xe("Xenon"),
	Cs("Caesium"),
	Ba("Barium"),
	La("Lanthanum"),
	Ce("Cerium"),
	Pr("Praseodymium"),
	Nd("Neodymium"),
	Pm("Promethium"),
	Sm("Samarium"),
	Eu("Europium"),
	Gd("Gadolinium"),
	Tb("Terbium"),
	Dy("Dysprosium"),
	Ho("Holmium"),
	Er("Erbium"),
	Tm("Thulium"),
	Yb("Ytterbium"),
	Lu("Lutetium"),
	Hf("Hafnium"),
	Ta("Tantalum"),
	W("Tungsten"),
	Re("Rhenium"),
	Os("Osmium"),
	Ir("Iridium"),
	Pt("Platinum"),
	Au("Gold"),
	Hg("Mercury"),
	Tl("Thallium"),
	Pb("Lead"),
	Bi("Bismuth"),
	Po("Polonium"),
	At("Astatine"),
	Rn("Radon"),
	Fr("Francium"),
	Ra("Radium"),
	Ac("Actinium"),
	Th("Thorium"),
	Pa("Protactinium"),
	U("Uranium"),
	Np("Neptunium"),
	Pu("Plutonium"),
	Am("Americium"),
	Cm("Curium"),
	Bk("Berkelium"),
	Cf("Californium"),
	Es("Einsteinium"),
	Fm("Fermium"),
	Md("Mendelevium"),
	No("Nobelium"),
	Lr("Lawrencium"),
	Rf("Rutherfordium"),
	Db("Dubnium"),
	Sg("Seaborgium"),
	Bh("Bohrium"),
	Hs("Hassium"),
	Mt("Meitnerium"),
	Ds("Darmstadtium"),
	Rg("Roentgenium"),
	Cn("Copernicium"),
	Uut("Ununtrium"),
	Fl("Flerovium"),
	Uup("Ununpentium"),
	Lv("Livermorium"),
	Uus("Ununseptium"),
	Uuo("Ununoctium"),
	
	Æ("Aebitirium", -1),
	Ə("Eragorium", -2),
	Œ("Octobium", -3),
	Ø("Disappearium", -4);
	
	/** The opposite of atomic number. */
	public static final int ATOMIC_NUMBER_OPPISITE = 0;
	
	public final String name;
	public final int index;
	
	private Atom(String fullName)
	{
		this.name = fullName;
		this.index = ordinal() + ATOMIC_NUMBER_OPPISITE;
	}
	
	private Atom(String fullName, int index)
	{
		this.name = fullName;
		this.index = index;
	}
	
	public int getAtomicNumber()
	{
		return index;
	}

	@Override
	public ArrayStandardStackList<Atom> getAtomContain()
	{
		ArrayStandardStackList<Atom> list = new ArrayStandardStackList<Atom>();
		list.add(this, 1);
		return list;
	}

	@Override
	public long getSize()
	{
		return 1;
	}

	@Override
	public long getParticleCount(IParticle particle, boolean divide)
	{
		return particle == this ? 1 : 0;
	}
	
	private Substance substance;
	
	public void setSubstanceInstance(Substance substance)
	{
		this.substance = substance;
	}
	
	public Substance getSubstanceInstance()
	{
		return substance;
	}
}