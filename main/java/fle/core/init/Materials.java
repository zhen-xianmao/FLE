//package fle.core.init;
//
//import static flapi.chem.particle.Atoms.As;
//import static flapi.chem.particle.Atoms.Cu;
//import static flapi.chem.particle.Atoms.Pb;
//import static flapi.chem.particle.Atoms.Sn;
//import static flapi.enums.CompoundType.Alloy;
//
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.List;
//
//import net.minecraft.init.Blocks;
//import net.minecraft.item.ItemStack;
//import flapi.chem.base.IChemCondition.EnumPH;
//import flapi.chem.base.Matter;
//import flapi.chem.base.Matter.AtomStack;
//import flapi.chem.particle.Atoms;
//import flapi.collection.CollectionUtil;
//import flapi.collection.CollectionUtil.FleEntry;
//import flapi.enums.EnumFLERock;
//import flapi.material.MaterialAbstract;
//import flapi.material.MaterialAlloy;
//import flapi.material.MaterialOre;
//import flapi.material.MaterialRock;
//import flapi.material.PropertyInfo;
//import flapi.util.SubTag;
//import fle.core.util.FleAlloy;
//import fle.matter.FleMatter;
//import fle.tool.DitchInfo;
//
//public class Materials
//{
//	public static MaterialAbstract Void;
//	
//	public static MaterialOre NativeCopper;
//	public static MaterialOre Tetrahedrite;
//	public static MaterialOre Enargite;
//	public static MaterialOre Cuprite;
//	public static MaterialOre Tenorite;
//	public static MaterialOre Covellite;
//	public static MaterialOre Chalcocite;
//	public static MaterialOre Malachite;
//	public static MaterialOre Azurite;
//	public static MaterialOre Chalcopyrite;
//	public static MaterialOre Bornite;
//	public static MaterialOre Orpiment;
//	public static MaterialOre Realgar;
//	public static MaterialOre Arsenolite;
//	public static MaterialOre Nickeline;
//	public static MaterialOre Arsenopyrite;
//	public static MaterialOre Scorodite;
//	public static MaterialOre Erythrite;
//	public static MaterialOre Gelenite;
//	public static MaterialOre Sphalerite;
//	public static MaterialOre Cassiterite;
//	public static MaterialOre Stannite;
//
//	public static MaterialAbstract SoftWood;
//	public static MaterialAbstract HardWood;
//	public static MaterialAbstract Charcoal;
//	public static MaterialAbstract Flint;
//	public static MaterialAbstract Bone;
//	public static MaterialAbstract Obsidian;
//	public static MaterialAbstract Argil;
//	
//	public static MaterialRock Stone;
//	public static MaterialRock CompactStone;
//	public static MaterialRock Limestone;
//	public static MaterialRock Rhyolite;
//	public static MaterialRock Andesite;
//	public static MaterialRock Basalt;
//	public static MaterialRock Peridotite;
//
//	public static MaterialAbstract Copper;
//	public static MaterialAbstract Lead;
//	public static MaterialAbstract Zinc;
//	public static MaterialAbstract Tin;
//	public static MaterialAbstract Arsenic;
//	public static MaterialAlloy CuAs;
//	public static MaterialAlloy CuAs2;
//	public static MaterialAlloy CuSn;
//	public static MaterialAlloy CuSn2;
//	public static MaterialAlloy CuPb;
//	public static MaterialAlloy CuPb2;
//	public static MaterialAlloy CuSnPb;
//	public static MaterialAlloy CuZn;
//	public static MaterialAlloy CuZn2;
//
//	public static DitchInfo ditch_stone;
//	public static DitchInfo ditch_wood0;
//	public static DitchInfo ditch_wood1;
//	public static DitchInfo ditch_wood2;
//	public static DitchInfo ditch_wood3;
//	public static DitchInfo ditch_wood4;
//	public static DitchInfo ditch_wood5;
//	
//	public static void init()
//	{
//		Void = new MaterialAbstract("Void", new PropertyInfo(0xAAAAAA, -1, -1, 1.0F, 1.0F, 1.0F, 0.0F, 10000000, -1.0F, 0.1F, 0.2F));
//		
//		NativeCopper = new MaterialOre("NativeCopper", Atoms.Cu.asMatter(),		new PropertyInfo(0xFF834C, 7, 14, 721, 1735, 698, 500, 0.0F, 1200, 1.2F, 0.8F, 1.0F, 0.2F, 12000000, 18.0F, 0.18F, 0.7F), SubTag.ORE_native);
//		Tetrahedrite = new MaterialOre("Tetrahedrite", Matter.mCu10Fe2Sb4S13,	new PropertyInfo(0xDCBC74,                      21,  9,  2.1F, 0.3F, 1.0F, 0.4F,  6800000, -1.0F, 0.21F, 0.7F ), SubTag.ORE_sulfide);
//		Enargite     = new MaterialOre("Enargite",     Matter.mCu3AsS4,			new PropertyInfo(0x7F6A68,                      13,  10, 1.3F, 1.0F, 0.4F, 0.5F,  6000000, -1.0F, 0.32F, 0.58F), SubTag.ORE_sulfide);
//		Cuprite      = new MaterialOre("Cuprite",      Matter.mCu2O,			new PropertyInfo(0xD83E26,                      10,  8,  1.0F, 0.4F, 1.0F, 0.9F,  8000000, 52.0F, 0.43F, 0.5F ), SubTag.ORE_oxide);
//		Tenorite     = new MaterialOre("Tenorite",     Matter.mCuO,				new PropertyInfo(0x5F6A73,                      12,  9,  1.2F, 0.6F, 1.0F, 1.0F,  9000000, 59.0F, 0.43F, 0.52F), SubTag.ORE_oxide);
//		Covellite    = new MaterialOre("Covellite",    Matter.mCuS,				new PropertyInfo(new int[]{0x3FC2F8, 0x643EB7}, 20,  11, 2.0F, 0.5F, 1.0F, 1.1F,  7500000, -1.0F, 0.35F, 0.45F), SubTag.ORE_sulfide);
//		Chalcocite   = new MaterialOre("Chalcocite",   Matter.mCu2S,			new PropertyInfo(0xA5B2C0,                      13,  11, 1.3F, 0.2F, 1.0F, 0.6F,  4800000, -1.0F, 0.38F, 0.55F), SubTag.ORE_sulfide);
//		Malachite    = new MaterialOre("Malachite",    Matter.mCu_OH2_CO3,		new PropertyInfo(0x43D393,                      8,   6,  0.8F, 0.3F, 0.9F, 0.7F,  7200000, -1.0F, 0.12F, 0.64F), SubTag.ORE_hydroxide, SubTag.ORE_oxide, SubTag.ORE_gem);
//		Azurite      = new MaterialOre("Azurite",      Matter.mCu_OH2_2CO3,		new PropertyInfo(0x434BD3,                      10,  7,  1.0F, 0.3F, 0.9F, 0.8F,  8000000, -1.0F, 0.21F, 0.56F), SubTag.ORE_hydroxide, SubTag.ORE_oxide, SubTag.ORE_gem);
//		Chalcopyrite = new MaterialOre("Chalcopyrite", Matter.mCuFeS2,			new PropertyInfo(0xF8E3A2,                      10,  8,  1.0F, 0.4F, 1.0F, 0.7F,  8400000, -1.0F, 0.28F, 0.59F), SubTag.ORE_sulfide);
//		Bornite      = new MaterialOre("Bornite",      Matter.mCu5FeS4,			new PropertyInfo(new int[]{0xDCBC74, 0x9D7797}, 14,  9,  1.4F, 0.4F, 1.0F, 0.8F,  9200000, -1.0F, 0.30F, 0.6F ), SubTag.ORE_sulfide);
//		Orpiment     = new MaterialOre("Orpiment",     Matter.mAs2S3,			new PropertyInfo(0xF7DA6E,                      9,   2,  0.8F, 0.1F, 1.0F, 0.3F,  5900000, -1.0F, 0.32F, 0.7F ), SubTag.ORE_sulfide);
//		Realgar      = new MaterialOre("Realgar",      Matter.mAs4S4,			new PropertyInfo(0xEF6C6B,                      8,   3,  0.9F, 0.1F, 1.0F, 0.4F,  7800000, -1.0F, 0.31F, 0.8F ), SubTag.ORE_sulfide);
//		Arsenolite   = new MaterialOre("Arsenolite",   Matter.mAs4S6,			new PropertyInfo(0xEBEFC8,                      16,  3,  1.6F, 0.1F, 1.0F, 0.3F,  8200000, -1.0F, 0.32F, 0.7F ), SubTag.ORE_sulfide);
//		Nickeline    = new MaterialOre("Nickeline",    Matter.mNiAs,			new PropertyInfo(0xC1B67A,                      31,  18, 3.8F, 0.1F, 1.3F, 0.2F,  10200000,-1.0F, 0.33F, 0.7F ), SubTag.ORE_TYPE_default);
//		Arsenopyrite = new MaterialOre("Arsenopyrite", Matter.mFeAsS,			new PropertyInfo(0xD7D5CB,                      17,  9,  2.1F, 0.1F, 1.1F, 0.2F,  8100000, -1.0F, 0.31F, 0.7F ), SubTag.ORE_TYPE_default, SubTag.ORE_sulfide);
//		Scorodite    = new MaterialOre("Scorodite",    Matter.mFeAsO4_2H2O,		new PropertyInfo(new int[]{0xB9DCCE, 0x4268A6}, 25,  12, 2.5F, 0.1F, 1.2F, 0.2F,  8900000, -1.0F, 0.32F, 0.7F ), SubTag.ORE_TYPE_default);
//		Erythrite    = new MaterialOre("Erythrite",    Matter.mCo3_AsO4_2_8H2O,	new PropertyInfo(0xE180B0,                      37,  19, 4.1F, 0.1F, 1.2F, 0.2F,  12300000,-1.0F, 0.34F, 0.6F ), SubTag.ORE_TYPE_default);
//		Gelenite     = new MaterialOre("Gelenite",     Matter.mPbS,				new PropertyInfo(0x9899A4,                      11,  7,  1.3F, 0.1F, 1.0F, 0.3F,  6400000, -1.0F, 0.32F, 0.7F ), SubTag.ORE_TYPE_default, SubTag.ORE_sulfide);
//		Sphalerite   = new MaterialOre("Sphalerite",   Matter.mZnS,				new PropertyInfo(0xD6CCCA,                      22,  9,  2.3F, 0.2F, 1.1F, 0.28F, 9200000, -1.0F, 0.34F, 0.7F ), SubTag.ORE_TYPE_default, SubTag.ORE_sulfide);
//		Cassiterite  = new MaterialOre("Cassiterite",  Matter.mSnO2,			new PropertyInfo(0xD1CBB0,                      13,  8,  1.5F, 0.1F, 1.0F, 0.18F, 8100000, -1.0F, 0.36F, 0.63F), SubTag.ORE_TYPE_default, SubTag.ORE_oxide);
//		Stannite     = new MaterialOre("Stannite",     Matter.mCu2FeSnS4,		new PropertyInfo(0xD6CCCA,                      17,  9,  1.9F, 0.1F, 1.0F, 0.2F,  8400000, -1.0F, 0.35F, 0.67F), SubTag.ORE_TYPE_default, SubTag.ORE_sulfide);
//		
//		Stone        = new MaterialRock("Stone",        new PropertyInfo(0x626262                     , 8 , 16, 1.2F , 0.0F, 1.0F, 1.4F, 10000000, -1.0F, 0.38F, 1.6F), SubTag.TOOL_stone, SubTag.TOOL_stone_real);
//		CompactStone = new MaterialRock("CompactStone", new PropertyInfo(0x686868                     , 9 , 21, 1.2F , 0.1F, 2.0F, 1.3F, 12800000, -1.0F, 0.35F, 1.72F), SubTag.TOOL_stone, SubTag.TOOL_stone_real);
//		Limestone    = new MaterialRock("Limestone",    new PropertyInfo(0xE4E4E5                     , 5 , 2 , 0.8F , 0.2F, 1.0F, 1.5F, 5600000));
//		Rhyolite     = new MaterialRock("Rhyolite",     new PropertyInfo(new int[]{0x4F535A, 0x414140}, 9 , 48, 1.9F , 0F  , 1.0F, 1.1F, 18000000), SubTag.TOOL_stone, SubTag.TOOL_stone_real).setRockType(EnumFLERock.Rhyolite);
//		Andesite     = new MaterialRock("Andesite",     new PropertyInfo(new int[]{0x616162, 0x6C6C62}, 14, 41, 1.6F , 0F  , 1.0F, 1.1F, 16300000), SubTag.TOOL_stone, SubTag.TOOL_stone_real).setRockType(EnumFLERock.Andesite);
//		Basalt       = new MaterialRock("Basalt",       new PropertyInfo(0x3A3A3A                     , 13, 41, 1.65F, 0F  , 1.0F, 1.0F, 16350000), SubTag.TOOL_stone, SubTag.TOOL_stone_real).setRockType(EnumFLERock.Basalt);
//		Peridotite   = new MaterialRock("Peridotite",   new PropertyInfo(0x4A5435                     , 14, 49, 2.2F , 0F  , 1.1F, 1.0F, 19800000), SubTag.TOOL_stone, SubTag.TOOL_stone_real).setRockType(EnumFLERock.Peridotite);
//		
//		Flint        = new MaterialAbstract("Flint",    Matter.mSiO2,           new PropertyInfo(0x5A5A5A, 8 , 12, 0.6F, 0.0F, 1.1F,  1.5F,  13500000), SubTag.TOOL_stone, SubTag.TOOL_flint);
//		Obsidian     = new MaterialAbstract("Obsidian", Matter.mSiO2,           new PropertyInfo(0x33344F, 19, 16, 2.1F, 0.0F, 1.0F,  3.0F,  7500000), SubTag.TOOL_stone, SubTag.TOOL_flint);
//		Bone         = new MaterialAbstract("Bone",                             new PropertyInfo(0xF9F7E7, 7 , 7 , 1.0F, 0.1F, 0.8F,  1.2F,  2800000), SubTag.TOOL_bone);
//		HardWood     = new MaterialAbstract("HardWood",                         new PropertyInfo(0x7F643D, 4 , 20, 0.4F, 1.0F, 0.1F,  0.1F,  4600000,  -1F, 0.34F, 2.0F), SubTag.TOOL_wood);
//		SoftWood     = new MaterialAbstract("SoftWood",                         new PropertyInfo(0x8F744D, 3 , 6 , 0.2F, 1.1F, 0.05F, 0.07F, 2800000,  -1F, 0.22F, 1.9F));
//		Charcoal     = new MaterialAbstract("Charcoal", Atoms.C.asMatter(), new PropertyInfo(0x35322A, 2 , 2 , 0.1F, 0.0F, 0.08F, 0.5F,  4000000,  98F, 0.6F , 1.2F));
//		Argil        = new MaterialAbstract("Argil",                            new PropertyInfo(0xAE9789, 5 , 29, 0.7F, 0.0F, 1.0F,  1.9F,  10800000, 59F, 0.26F, 1.8F));
//		
//		Arsenic      = new MaterialAbstract("Arsenic", Atoms.As.asMatter(), new PropertyInfo(0x555655, 8, 19, 887, 1090, 673, 481, 0.9F, 1550, 1.6F, 1.2F, 0.9F, 0.2F, 27927000, -1.0F, 0.2F, 0.21F));
//		
//		Copper       = new MaterialAbstract("Copper", Atoms.Cu.asMatter(), new PropertyInfo(0xDB4E31, 14, 52, 857, 1735, 698, 500, 0.8F, 1600, 2.3F, 8.0F, 1.2F, 0.0F, 48000000, 0.16F, 1.0F, 0.057F), SubTag.TOOL_metal_tier0, SubTag.CRAFTING_cold_wought, SubTag.MATERIAL_heatwire);
//		Lead         = new MaterialAbstract("Lead",   Atoms.Pb.asMatter(), new PropertyInfo(0xC4C4C6, 9 , 21, 579, 1849, 793, 601, 0.8F, 1600, 1.2F, 9.1F, 1.9F, 0.0F, 29000000, 2.08F, 0.8F, 0.006F), SubTag.TOOL_metal_tier0, SubTag.CRAFTING_cold_wought, SubTag.MATERIAL_heatwire);
//		Zinc         = new MaterialAbstract("Zinc",   Atoms.Zn.asMatter(), new PropertyInfo(0xD7D9DA, 5 , 30, 602, 1048, 782, 673, 0.8F, 1600, 1.8F, 4.7F, 1.4F, 0.0F, 38100000, 0.58F, 0.7F, 0.027F), SubTag.CRAFTING_cold_wought, SubTag.MATERIAL_heatwire);
//		Tin          = new MaterialAbstract("Tin",    Atoms.Sn.asMatter(), new PropertyInfo(0xD3D2CF, 8 , 8 , 473, 2784, 582, 674, 0.8F, 1600, 1.2F, 8.7F, 1.2F, 0.0F, 30000000, 1.6F , 0.8F, 0.04F ), SubTag.CRAFTING_cold_wought, SubTag.MATERIAL_heatwire);
//		MaterialAbstract.pureMaterials.register(Copper, "Cu");
//		MaterialAbstract.pureMaterials.register(Lead, "Pb");
//		MaterialAbstract.pureMaterials.register(Zinc, "Zn");
//		MaterialAbstract.pureMaterials.register(Tin, "Sn");
//		for(Atoms atom : Atoms.values())
//		{
//			MaterialAbstract material;
//			if(!MaterialAbstract.pureMaterials.contain(atom.getName()))
//				MaterialAbstract.pureMaterials.register(
//						material = new MaterialAbstract(atom.getName(), atom.asMatter(), atom.createDefaultInfo()), atom.name());
//			else material = MaterialAbstract.pureMaterials.get(atom.name());
//			material.setDisplayName(atom.getName());
//			FleMatter.registerFLEMatter(material, atom.ordinal());
//		}
//		CuAs = new MaterialAlloy("ArsenicBronze", Matter.forMatter("arsenic_bronze", Alloy, new AtomStack(Cu, 9), new AtomStack(As)), 
//				new PropertyInfo(0xA37C68, 16, 175, 684, 1735, 702, 7086, 0.2F, 1500, 1.6F, 1.7F, 1.1F, 0.2F, 92000000, 102F, 0.97F, 0.21F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(Atoms.As, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);
//		CuAs2 = new MaterialAlloy("HighArsenicBronze", Matter.forMatter("high_arsenic_bronze", Alloy, new AtomStack(Cu, 3), new AtomStack(As)), 
//				new PropertyInfo(0x6A6353, 14, 135, 573, 1735, 689, 6894, 0.1F, 1650, 1.4F, 0.5F, 1.1F, 0.4F, 86000000, 100F, 0.96F, 0.22F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.5D, 0.8D}), new FleEntry(Atoms.As, new double[]{0.1D, 0.5D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);
//		CuPb = new MaterialAlloy("LeadBronze", Matter.forMatter("lead_bronze", Alloy, new AtomStack(Cu, 17), new AtomStack(Pb, 2)), 
//				new PropertyInfo(0x8E8741, 18, 200, 671, 1735, 702, 8291, 0.2F, 1500, 1.8F, 1.6F, 1.2F, 0.1F, 97000000, 98F, 0.94F, 0.22F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(Atoms.Pb, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);
//		CuPb2 = new MaterialAlloy("HighLeadBronze", Matter.forMatter("high_lead_bronze", Alloy, new AtomStack(Cu, 3), new AtomStack(Pb, 2)), 
//				new PropertyInfo(0x817E66, 17, 160, 628, 1735, 702, 8028, 0.3F, 1500, 1.7F, 1.2F, 1.3F, 0.3F, 82000000, 101F, 0.99F, 0.21F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.4D, 0.8D}), new FleEntry(Atoms.Pb, new double[]{0.2D, 0.6D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);
//		CuSn = new MaterialAlloy("TinBronze", Matter.forMatter("tin_bronze", Alloy, new AtomStack(Cu, 17), new AtomStack(Sn, 2)), 
//				new PropertyInfo(0x936436, 18, 215, 648, 1735, 702, 8028, 0.3F, 1500, 1.9F, 1.2F, 1.1F, 0.1F, 102000000, 103F, 0.93F, 0.23F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(Atoms.Sn, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);
//		CuSn2 = new MaterialAlloy("HighTinBronze", Matter.forMatter("high_tin_bronze", Alloy, new AtomStack(Cu, 3), new AtomStack(Sn, 2)), 
//				new PropertyInfo(0xA28A72, 15, 175, 629, 1735, 702, 7960, 0.26F, 1500, 1.5F, 1.1F, 1.13F, 0.18F, 93000000, 109F, 0.91F, 0.27F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.4D, 0.8D}), new FleEntry(Atoms.Sn, new double[]{0.2D, 0.6D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);
//		CuSnPb = new MaterialAlloy("LeadTinBronze", Matter.forMatter("lead_tin_bronze", Alloy, new AtomStack(Cu, 8), new AtomStack(Sn, 1), new AtomStack(Pb, 1)), 
//				new PropertyInfo(0xD0B36A, 20, 208, 682, 1735, 689, 5839, 0.26F, 1600, 1.6F, 1.2F, 1.11F, 0.19F, 99000000, 129F, 0.87F, 0.29F), 
//				new FleAlloy(CollectionUtil.asMap(new FleEntry(Atoms.Cu, new double[]{0.8D, 0.9D}), new FleEntry(Atoms.Sn, new double[]{0.05D, 0.15D}), new FleEntry(Atoms.Pb, new double[]{0.05D, 0.15D}))), SubTag.TOOL_metal_tier1, SubTag.MATERIAL_heatwire);	
//		FleMatter.registerFLEMatter(CuAs	, 301);
//		FleMatter.registerFLEMatter(CuAs2	, 302);
//		FleMatter.registerFLEMatter(CuPb	, 303);
//		FleMatter.registerFLEMatter(CuPb2	, 304);
//		FleMatter.registerFLEMatter(CuSn	, 305);
//		FleMatter.registerFLEMatter(CuSn2	, 306);
//		FleMatter.registerFLEMatter(CuSnPb	, 307);
//		
//		Void.setDisplayName("Void");
//		
//		NativeCopper.setDisplayName("Native Copper");
//		Tetrahedrite.setDisplayName("Tetrahedrite");
//		Enargite.setDisplayName("Enargite");
//		Cuprite.setDisplayName("Cuprite");
//		Tenorite.setDisplayName("Tenorite");
//		Covellite.setDisplayName("Covellite");
//		Chalcocite.setDisplayName("Chalcocite");
//		Malachite.setDisplayName("Malachite");
//		Azurite.setDisplayName("Azurite");
//		Chalcopyrite.setDisplayName("Chalcopyrite");
//		Bornite.setDisplayName("Bornite");
//		Orpiment.setDisplayName("Orpiment");
//		Realgar.setDisplayName("Realgar");
//		Arsenolite.setDisplayName("Arsenolite");
//		Nickeline.setDisplayName("Nickeline");
//		Arsenopyrite.setDisplayName("Arsenopyrite");
//		Scorodite.setDisplayName("Scorodite");
//		Erythrite.setDisplayName("Erythrite");
//		Gelenite.setDisplayName("Gelenite");
//		Sphalerite.setDisplayName("Sphalerite");
//		Cassiterite.setDisplayName("Cassiterite");
//		Stannite.setDisplayName("Stannite");
//
//		SoftWood.setDisplayName("Soft Wood");
//		HardWood.setDisplayName("Hard Wood");
//		Charcoal.setDisplayName("Charcoal");
//		Flint.setDisplayName("Flint");
//		Bone.setDisplayName("Bone");
//		Obsidian.setDisplayName("Obsidian");
//		Argil.setDisplayName("Argil");
//		
//		Stone.setDisplayName("Stone");
//		CompactStone.setDisplayName("Compact Stone");
//		Limestone.setDisplayName("Limestone");
//		Rhyolite.setDisplayName("Rhyolite");
//		Andesite.setDisplayName("Andesite");
//		Basalt.setDisplayName("Basalt");
//		Peridotite.setDisplayName("Peridotite");
//
//		CuAs.setDisplayName("Arsenic Bronze");
//		CuAs2.setDisplayName("High Arsenic Bronze");
//		CuSn.setDisplayName("Tin Bronze");
//		CuSn2.setDisplayName("High Tin Bronze");
//		CuPb.setDisplayName("Lead Bronze");
//		CuPb2.setDisplayName("High Lead Bronze");
//		CuSnPb.setDisplayName("Lead Tin Bronze");
//		//CuZn.setDisplayName("Brass");
//		//CuZn2.setDisplayName("High Zinc Brass");
//		
//		List<MaterialAbstract> l1 = new ArrayList<MaterialAbstract>();
//		for(Atoms atom : Atoms.values())
//			if(atom.contain(SubTag.ATOM_metal))
//				l1.add(MaterialAbstract.getMaterialRegistry().get(atom.getName()));
//		MaterialAbstract[] list1 = l1.toArray(new MaterialAbstract[l1.size()]);
//		SubTag[] tags1 = {
//				//Parts.block.partTag, 
//				Parts.can.partTag, Parts.column.partTag, 
//				Parts.largeCube.partTag, Parts.cube.partTag,
//				Parts.ingot.partTag, Parts.ingotDouble.partTag, Parts.ingotQuadruple.partTag,
//				Parts.plate.partTag, Parts.plateDouble.partTag, Parts.plateQuadruple.partTag,
//				Parts.longStick.partTag, Parts.stick.partTag, 
//				Parts.tube.partTag};
//		SubTag.addTagsTo(tags1, list1);
//		SubTag.addTagsTo(tags1, CuAs, CuAs2, CuSn, CuSn2, CuPb, CuPb2, CuSnPb);//, CuZn, CuZn2);
//		
//		ditch_stone = new DitchInfo("Stone", Stone, new ItemStack(Blocks.stone), 9000, 0.01F, EnumPH.Strong_Acid, EnumPH.Weak_Alkali, 1500);
//		ditch_wood0 = new DitchInfo("Wood0", HardWood, Blocks.planks, 0, 0.03F, 4000, 380);
//		ditch_wood1 = new DitchInfo("Wood1", SoftWood, Blocks.planks, 1, 0.06F, 5000, 375);
//		ditch_wood2 = new DitchInfo("Wood2", HardWood, Blocks.planks, 2, 0.03F, 4000, 380);
//		ditch_wood3 = new DitchInfo("Wood3", HardWood, Blocks.planks, 3, 0.03F, 4000, 380);
//		ditch_wood4 = new DitchInfo("Wood4", SoftWood, Blocks.planks, 4, 0.06F, 5000, 375);
//		ditch_wood5 = new DitchInfo("Wood5", HardWood, Blocks.planks, 5, 0.03F, 4000, 380);
//	}
//	
//	public static void postInit()
//	{
//		if(MaterialAbstract.property != null)
//		{
//			BufferedWriter fw = null;
//			try
//			{
//				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
//						MaterialAbstract.property), "UTF-8"), 1024);
//				fw.write("Material Name, Color A, Color B, Max Uses, Melting Point, "
//						+ "Boiling Point, Viscosity, Hardness, Toughness, Brittleness, "
//						+ "Denseness, Shear Strength, Resistance, Specific Heat, Thermal Conductivity"
//						+ "\r\n");
//				for(MaterialAbstract material : MaterialAbstract.getMaterialRegistry())
//				{
//					PropertyInfo i = material.getPropertyInfo();
//					fw.write(String.format("%s, %d, %d, %d, %d, %d, %d, %g, %g, %g, %g, %d, %g, %g, %g\r\n", 
//							MaterialAbstract.getMaterialRegistry().name(material),
//							i.getColors()[0],
//							i.getColors()[1],
//							i.getMaxUses(),
//							i.getMeltingPoint(),
//							i.getBoilingPoint(),
//							i.getViscosity(),
//							i.getHardness(),
//							i.getToughness(),
//							i.getBrittleness(),
//							i.getDenseness(),
//							(int) Math.pow(10, i.getShearStrength()),
//							i.getResistance(),
//							(float) i.getSpecificHeat(),
//							i.getThermalConductivity()));
//				}
//				fw.flush();
//				fw.close();
//			}
//			catch(Throwable e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				if(fw != null)
//				{
//					try
//					{
//						fw.close();
//					}
//					catch(IOException e)
//					{
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//}