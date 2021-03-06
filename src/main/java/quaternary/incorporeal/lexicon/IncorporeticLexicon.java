package quaternary.incorporeal.lexicon;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import quaternary.incorporeal.Incorporeal;
import quaternary.incorporeal.IncorporeticConfig;
import quaternary.incorporeal.block.IncorporeticBlocks;
import quaternary.incorporeal.item.IncorporeticItems;
import quaternary.incorporeal.recipe.IncorporeticPetalRecipes;
import quaternary.incorporeal.recipe.IncorporeticRuneRecipes;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.CompatLexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public final class IncorporeticLexicon {
	private IncorporeticLexicon() {}
	
	public static LexiconCategory categoryCorporetic = null;
	
	//PRE-ELVEN
	public static LexiconEntry frameTinkerer;
	public static LexiconEntry naturalDevices;
	
	public static LexiconEntry sweetAlexum;
	
	//ELVEN
	//items
	public static LexiconEntry fracturedSpace;
	public static LexiconEntry ticketConjurer;
	
	//corporea blocks
	public static LexiconEntry corporeaInhibitor;
	public static LexiconEntry corporeaSolidifier;
	public static LexiconEntry corporeaTinkerer;
	public static LexiconEntry corporeaRetainerDecrementer;
	public static LexiconEntry soulCores;
	
	//etc
	public static LexiconEntry sanvocalia;
	public static LexiconEntry redStringLiar;
	
	public static void init() {
		//init stuff
		if(IncorporeticConfig.General.CORPOREA_KNOWLEDGE_TYPE) {
			//todo make this not crap
			//-can i hide the category when it's not elven? (there's nothing in it)
			//-knowledge type?
			//-seem to be getting duped index searches?
			categoryCorporetic = new LexiconCategory("incorporeal.category.corporea")
				.setPriority(3)
				.setIcon(new ResourceLocation(Incorporeal.MODID, "textures/lexicon/categories/corporea.png"))
			;
			BotaniaAPI.addCategory(categoryCorporetic);
			
			//move vanilla corporea-related items into this category
			for(LexiconEntry e : new LexiconEntry[]{
				LexiconData.corporea, LexiconData.corporeaCrystalCube, LexiconData.corporeaFunnel, LexiconData.corporeaIndex, LexiconData.corporeaInterceptor, LexiconData.corporeaRetainer
			}) {
				BotaniaAPI.addEntry(e, categoryCorporetic);
				BotaniaAPI.categoryEnder.entries.remove(e);
			}
			
			LexiconData.corporea.setPriority();
		} else {
			categoryCorporetic = BotaniaAPI.categoryEnder;
		}
		
		/////
		newEntryType = BotaniaAPI.basicKnowledge; //Basic Knowledge entries
		/////
		
		frameTinkerer = buildCraftingEntry(IncorporeticBlocks.FRAME_TINKERER, BotaniaAPI.categoryDevices, 1);
		
		//tfw u build castles of abstractions but they come crashing down on you
		naturalDevices = new CompatLexiconEntry("incorporeal.naturalDevices", BotaniaAPI.categoryDevices, Incorporeal.NAME).setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));
		naturalDevices.setIcon(new ItemStack(IncorporeticItems.NATURAL_COMPARATOR));
		naturalDevices.addExtraDisplayedRecipe(new ItemStack(IncorporeticItems.NATURAL_REPEATER));
		naturalDevices.addExtraDisplayedRecipe(new ItemStack(ModItems.manaResource, 1, 6));
		naturalDevices.setKnowledgeType(newEntryType);
		//anyhoo
		
		sweetAlexum = buildFlowerEntry("sweet_alexum", IncorporeticPetalRecipes.sweetAlexum, BotaniaAPI.categoryFunctionalFlowers, 2);
		
		/////
		newEntryType = BotaniaAPI.elvenKnowledge; //Elven Knowledge entries
		/////
		
		fracturedSpace = buildCraftingEntry(IncorporeticItems.FRACTURED_SPACE_ROD, BotaniaAPI.categoryTools, 2);
		ticketConjurer = buildCraftingEntry(IncorporeticItems.TICKET_CONJURER, categoryCorporetic, 2);
		
		corporeaInhibitor = buildCraftingEntry(IncorporeticBlocks.CORPOREA_INHIBITOR, categoryCorporetic, 1);
		corporeaSolidifier = buildCraftingEntry(IncorporeticBlocks.CORPOREA_SOLIDIFIER, categoryCorporetic, 2);
		corporeaTinkerer = buildCraftingEntry(IncorporeticBlocks.CORPOREA_SPARK_TINKERER, categoryCorporetic, 2);
		corporeaRetainerDecrementer = buildCraftingEntry(IncorporeticBlocks.CORPOREA_RETAINER_DECREMENTER, categoryCorporetic, 2);
		
		soulCores = new CompatLexiconEntry("incorporeal.soulCores", categoryCorporetic, Incorporeal.NAME);
		soulCores.setLexiconPages(
						new PageText("0"),
						new PageRuneRecipe(".flavor0", IncorporeticRuneRecipes.soulCoreFrame),
						new PageText("1"),
						new PageRuneRecipe(".flavor1", IncorporeticRuneRecipes.enderSoulCore),
						new PageText("2"),
						new PageRuneRecipe(".flavor2", IncorporeticRuneRecipes.corporeaSoulCore)
		);
		soulCores.setKnowledgeType(newEntryType);
		soulCores.addExtraDisplayedRecipe(new ItemStack(IncorporeticItems.CORPOREA_SOUL_CORE));
		soulCores.addExtraDisplayedRecipe(new ItemStack(IncorporeticItems.ENDER_SOUL_CORE));
		soulCores.setIcon(new ItemStack(IncorporeticItems.CORPOREA_SOUL_CORE));
		
		sanvocalia = buildFlowerEntry("sanvocalia", IncorporeticPetalRecipes.sanvocalia, BotaniaAPI.categoryFunctionalFlowers, 1);
		
		redStringLiar = buildCraftingEntry(IncorporeticBlocks.RED_STRING_LIAR, categoryCorporetic, 2);
	}
	
	private static LexiconEntry buildCraftingEntry(IForgeRegistryEntry subject, LexiconCategory category, int pageCount) {
		String name = Incorporeal.MODID + '.' + Preconditions.checkNotNull(subject.getRegistryName()).getPath();
		ItemStack icon;
		
		if(subject instanceof Block) {
			icon = new ItemStack((Block) subject);
		} else if (subject instanceof Item) {
			icon = new ItemStack((Item) subject);
		} else {
			throw new IllegalArgumentException("Can't determine the lexicon page item for " + subject + " if you see this quat is a big stupid");
		}
		
		LexiconPage terminalPage = new PageCraftingRecipe(".flavor", subject.getRegistryName());
		
		return buildEntryInternal(name, icon, category, pageCount, terminalPage);
	}
	
	private static LexiconEntry buildFlowerEntry(String name, RecipePetals recipe, LexiconCategory category, int pageCount) {
		ItemStack icon = ItemBlockSpecialFlower.ofType(name);
		
		LexiconPage terminalPage = new PagePetalRecipe<>(".flavor", recipe);
		
		return buildEntryInternal(name, icon, category, pageCount, terminalPage);
	}
	
	//A little global state to save typing never hurt anyone
	//Only tiny potato can judge me
	private static KnowledgeType newEntryType = BotaniaAPI.basicKnowledge;
	
	private static LexiconEntry buildEntryInternal(String name, ItemStack icon, LexiconCategory category, int pageCount, LexiconPage terminalPage) {		
		LexiconEntry entry = new CompatLexiconEntry(name, category, Incorporeal.NAME);
		for(int i=0; i < pageCount; i++) {
			//Just using addPage is tempting but for some reason the
			//unlocalized name gets hacked into shape in BasicLexiconEntry
			//But only if you don't use addPage ¯\_(ツ)_/¯
			entry.setLexiconPages(new PageText(String.valueOf(i)));
		}
		entry.setLexiconPages(terminalPage);
		entry.setIcon(icon);
		entry.setKnowledgeType(newEntryType);
		
		return entry;
	}
}
