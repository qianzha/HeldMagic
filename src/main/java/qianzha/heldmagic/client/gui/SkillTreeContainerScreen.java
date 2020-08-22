package qianzha.heldmagic.client.gui;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;
import qianzha.heldmagic.common.HMConstants;
import qianzha.heldmagic.common.gui.SkillTreeContainer;
import qianzha.heldmagic.common.network.HeldMagicPacketHandler;
import qianzha.heldmagic.common.network.msg.EqulpMagicMsg;
import qianzha.heldmagic.common.network.msg.SwapSlotMsg;
import qianzha.heldmagic.common.network.msg.UnequlpMagicMsg;
import qianzha.heldmagic.util.HMLogger;

public class SkillTreeContainerScreen extends ContainerScreen<SkillTreeContainer> {
	public static final int HOVER_COLOR = 0X80FFFFFF;
	public static final int SELECT_COLOR = 0XFFFF0000;
	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(HMConstants.MODID, "textures/gui/skilltree_alpha.png");
	private NamespaceList namespaceList = new NamespaceList();
	private MagicFakeSlot magicSlots = new MagicFakeSlot();
	private PlayerInvSolt playerInvSlots = new PlayerInvSolt();
	private int selected = -1;
	
	public SkillTreeContainerScreen(SkillTreeContainer container, PlayerInventory inv, ITextComponent titleIn) {
		super(container, inv, titleIn);
		this.xSize = 195;
		this.ySize = 136;
		namespaceList.load();
		magicSlots.load();
	}

	public void reloadMagicList() {
		this.container.getMagicList(true);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		this.blit(this.guiLeft, this.guiTop, 0, 0, 195, 136);
		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), guiLeft + 8, guiTop + 6, 0x404040);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		magicSlots.render(mouseX, mouseY, partialTicks);
		playerInvSlots.render(mouseX, mouseY, partialTicks);
		drawGuiContainerForegroundLayer(mouseX, mouseY);
		namespaceList.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void init() {
		HMLogger.DEBUG.info("Screen Init");
		super.init();
		this.children.add(namespaceList);
		this.children.add(magicSlots);
		this.children.add(playerInvSlots);
		
		namespaceList.init();
		magicSlots.init();
		playerInvSlots.init();
	}
	
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int keyId) {
		if(mouseX < this.guiLeft || mouseY < this.guiTop ||
				mouseX >= this.guiLeft + this.xSize || mouseY >= this.guiTop + this.ySize) {
			click(-1);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, keyId);
	}

	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		namespaceList.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		magicSlots.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
	
	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		namespaceList.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		magicSlots.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	    
    }
	
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
	    if(namespaceList.show)
		    return namespaceList.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
	    else 
		    return magicSlots.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
	}
	
	protected void click(int index) {
		if(index < 0 || selected == index) {
			selected = -1;
		}
		else if(selected < 0) {
			selected = index;
		}
		else {
			int a, b;
			if(selected < index) {
				a = selected;
				b = index;
			}
			else {
				a = index;
				b = selected;
			}
			selected = -1;
			
			if(b < 10) {
				// 交换
				HeldMagicPacketHandler.CHANNEL.sendToServer(new SwapSlotMsg(a, b < 9 ? b : 40));
			}
			else if(a < 10) {
				// TODO 发包 装备
				IHoldableMagic magic = magicSlots.magicList.get(b-10);
				HeldMagicPacketHandler.CHANNEL.sendToServer(new EqulpMagicMsg(magic, a < 9 ? a : 40));
				container.detectAndSendChanges();
			}
			else {
				selected = index;
			}
		}
	}
	
	public void unequlpMagic(int index) {
		HMUtils.isMagicStack(playerInventory.getStackInSlot(index));
		HeldMagicPacketHandler.CHANNEL.sendToServer(new UnequlpMagicMsg(index));
	}
	
	public class NamespaceList extends Widget {
		protected ImmutableList<String> namespaces;
		protected int nsSelect;
		private boolean show;
		
		protected int scrollBarX0;
		protected int scrollBarY0;
		protected int rows;
		protected int currentRow0;
		protected boolean isScrolling;
		
		private ImageButton btnList = new ImageButton(0, 0, 12, 10, 232, 15, 10, GUI_TEXTURE, this::onButtonPress);

		public NamespaceList() {
			super(guiLeft + 77, guiTop + 1, 90, 12, "");
			nsSelect = -1;
		}

		protected void load() {
			namespaces = container.getNamespaces();
			rows = namespaces.size();
			currentRow0 = 0;
		}
		
		protected void init() {
			if(show) {
				this.x = guiLeft + 77;
				this.y = guiTop + 1;
				this.width = 97;
				this.height = 105;
				if(!needScroll() || nsSelect < 0) 
					this.currentRow0 = -1;
				else 
					this.currentRow0 = nsSelect + 5 < rows ? nsSelect - 1 : rows - 6;
			}
			else {
				this.x = guiLeft + 80;
				this.y = guiTop + 4;
				this.width = 90;
				this.height = 12;
			}
			scrollBarX0 = guiLeft + 157;
			scrollBarY0 = guiTop + 19;
			int btnLeft = guiLeft + 157;
			int btnTop = guiTop + 5;
			btnList = new ImageButton(btnLeft, btnTop, 12, 10, show?244:232, 15, 10, GUI_TEXTURE, this::onButtonPress);
			children.add(btnList);
		}
		
		protected boolean needScroll() {
			return rows >= 6;
		}
		
		protected float getCurrentScroll() {
			return 1.0F * (this.currentRow0 + 1) / (rows - 6 + 1);
		}
		
		@Override
		public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
			if (!this.needScroll() && show) {
		       return false;
		    } else {
			    this.currentRow0 -= p_mouseScrolled_5_;
			    this.currentRow0 = MathHelper.clamp(this.currentRow0, -1, rows - 6);
			    return true;
		    }
		}

		public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
			HMLogger.DEBUG.info("Name Dragging {}", this.isScrolling);
	        if (show && this.isScrolling && p_mouseDragged_5_ == 0) {
	           int sTop = scrollBarY0;
	           int sBottom = scrollBarY0 + 65;
	           float current = ((float)p_mouseDragged_3_ - (float)sTop - 7.5F) / ((float)(sBottom - sTop) - 15.0F);
	           current = MathHelper.clamp(current, 0.0F, 1.0F);
	           this.currentRow0 = (int) (current * (rows - 5)) - 1;
	           HMLogger.DEBUG.info("Scorlling: float {}, int {}", current, currentRow0);
	           return true;
	        } else {
	           return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	        }
	    }

		@Override
		public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
			this.isScrolling = false;
			return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		}
		
		@Override
		public void render(int mouseX, int mouseY, float partialTicks) {
			if(visible) {
				minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
				int optionX0;
				int optionY0;
				if(show) {
					// Namespaces Menu
					blit(this.x, this.y, 0, 136, 97, 105);
					// Scroll Bar
					this.blit(scrollBarX0, scrollBarY0 + (int)(65 * getCurrentScroll()), this.needScroll() ? 232 : 244, 0, 12, 15);
					
					// Options
					optionX0 = this.x + 3;
					optionY0 = this.y + 3;
					
					for(int i=0; i<6; i++) {
						int index = i + currentRow0;
						if(index >= rows) break;
						drawOption(optionX0, optionY0 + ((i+1)*14), index, mouseX, mouseY);
					} 
					for(int i=0; i<6; i++) {
						int index = i + currentRow0;
						if(index >= rows) break;
						drawOptionString(optionX0 + 5, optionY0 + 16 + 14*i, index);
					}
				}
				else {
					optionX0 = this.x;
					optionY0 = this.y;
				}
				btnList.render(mouseX, mouseY, partialTicks);
				font.drawString(nsSelect < 0 ? "All" : container.getSelectedNamespace(), optionX0 + 5, optionY0 + 2, 0x202020);
			}
		}
		
		protected void drawOption(int x, int y, int index, int mouseX, int mouseY) {
			boolean selected = index == nsSelect;
			blit(x, y, 97, selected ? 148 : 136, 74, 12);
			if(mouseX >= x && mouseY >= y && mouseX < x+74 && mouseY < y+12) {
				this.fillGradient(x, y, x + 74, y + 12, HOVER_COLOR, HOVER_COLOR);
			}
		}
		
		protected void drawOptionString(int x, int y, int index) {
			boolean selected = index == nsSelect;
			String optStr = index < 0 ? "All" : namespaces.get(index);
			if(selected) optStr = TextFormatting.BOLD + optStr;
			font.drawString(optStr, x, y, 0x202020);
		}
		
		public void setShow(boolean show) {
			this.show = show;
			this.init();
		}
		
		public void onButtonPress(Button btn) {
			setShow(show ? false : true);
		}
		
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int keyId) {
			if (this.active && this.visible) {
				if(btnList.mouseClicked(mouseX, mouseY, keyId)) {
					return true;
				}
				if(keyId == 0) {
					if(show) {
						double mX = mouseX - guiLeft;
						double mY = mouseY - guiTop;
						if(mX >= 80 && mY >= 18 && mX < 154 && mY < 100){
							if((mY - 18) % 14 < 12) {
								int row = (((int) mY) - 18) / 14;
								int nsIndex = currentRow0 + row;
								if(nsIndex < namespaces.size() && selectNs(nsIndex)) {
									this.playDownSound(Minecraft.getInstance().getSoundHandler());
									container.setSelectedNamespace(nsSelect < 0 ? null : namespaces.get(nsSelect));
									SkillTreeContainerScreen.this.magicSlots.load();
									setShow(false);
					                return true;
								}
							}
						}
						else {
							int scrollX = scrollBarX0 - guiLeft;
							int scrollY = scrollBarY0 - guiTop + (int)(65 * getCurrentScroll());
							HMLogger.DEBUG.info("mX, mY  {}, {}", mX, mY);
							HMLogger.DEBUG.info("sX, sY  {}, {}", scrollX, scrollY);
							
							if(mX >= scrollX && mY >= scrollY && mX < scrollX + 12 && mY < scrollY + 15) {
								isScrolling = this.needScroll();
								HMLogger.DEBUG.info("scrolling {}", isScrolling);
							}
						}
					}
				}
			}
			return false;
		}
		
		public boolean selectNs(int index) {
			if(index > namespaces.size())
				return false;
			
			if(this.nsSelect != index) {
				this.nsSelect = index;
				return true;
			}
			else {
				if(this.nsSelect >= 0) {
					this.nsSelect = -1;
					return true;
				}
			}
			return false;
		}
		
	}
	
	public class MagicFakeSlot extends Widget {
		protected List<IHoldableMagic> magicList;
		protected int rows;
		protected int currentRow0;
		protected boolean isScrolling;
		
		protected int scrollBarX0;

		public MagicFakeSlot() {
			super(guiLeft + 9, guiTop + 18, 160, 88, "");
		}
		
		protected void init() {
			this.x = guiLeft + 9;
			this.y = guiTop + 18;
			this.scrollBarX0 = this.x + 166;
		}
		
		protected void load() {
			this.magicList = container.getMagicList(true);
			this.currentRow0 = 0;
			this.rows = Math.floorDiv(this.magicList.size(), 9);
		}

		protected boolean needScroll() {
			return this.rows > 5;
		}
		
		protected float getCurrentScroll() {
			return 1.0F * this.currentRow0 / (rows - 5);
		}
		
		@Override
		public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
			if (!this.needScroll()) {
		       return false;
		    } else {
			    this.currentRow0 -= p_mouseScrolled_5_;
			    this.currentRow0 = MathHelper.clamp(this.currentRow0, 0, rows - 5);
			    return true;
		    }
		}

		public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
			// TODO 
           HMLogger.DEBUG.info("Dragging");
	        if (this.isScrolling && p_mouseDragged_5_ == 0) {
	           int sTop = this.y;
	           int sBottom = this.y + 73;
	           float current = ((float)p_mouseDragged_3_ - (float)sTop - 7.5F) / ((float)(sBottom - sTop) - 15.0F);
	           current = MathHelper.clamp(current, 0.0F, 1.0F);
	           this.currentRow0 = (int) (current * (rows - 5));
	           HMLogger.DEBUG.info("Scorlling: float {}, int {}", current, currentRow0);
	           return true;
	        } else {
	           return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	        }
	    }
		
		@Override
		public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
			this.isScrolling = false;
			return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		}
		
		@Override
		public void render(int mouseX, int mouseY, float partialTicks) {
			this.blit(scrollBarX0, y + (int)(73F * this.getCurrentScroll()), this.needScroll() ? 232 : 244, 0, 12, 15);
			for(int i = 0; i < 45; i++) {
				int index = currentRow0 * 9 + i;
				if(index >= this.magicList.size()) break;
				int col = i % 9;
				if(!namespaceList.show || col < 4) {
					int x = this.x + col * 18;
					int y = this.y + (i / 9) * 18;
					ItemStack stack = new ItemStack(magicList.get(index).getItem());
					
					if(selected - 10 == index) {
						this.fillGradient(x, y, x + 16, y + 16, 0xFFFF8080, 0xFF800000);
					}
					itemRenderer.renderItemIntoGUI(stack, x, y);
				}
			}
			for(int i = 0; i < 45; i++) {
				int index = currentRow0 * 9 + i;
				if(index >= this.magicList.size()) break;
				int col = i % 9;
				if(!namespaceList.show || col < 4) {
					int x = this.x + col * 18;
					int y = this.y + (i / 9) * 18;

					if(mouseX >= x && mouseX < x+16 && mouseY >= y && mouseY < y+16) {
						IHoldableMagic magic = magicList.get(index);
						this.fillGradient(x, y, x + 16, y + 16, HOVER_COLOR, HOVER_COLOR);
						SkillTreeContainerScreen.this.renderTooltip(Lists.asList(
								HMUtils.getMagicTranslatedName(magic).setStyle(
										new Style().setColor(TextFormatting.LIGHT_PURPLE).setItalic(true)
								).getFormattedText(),
								new String[] {magic.getInfo().getFormattedText()}
						), mouseX, mouseY);
					}
				}
			}
		}
		
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int keyId) {
			if (this.visible) {
				if(keyId == 0) {
					double mX = mouseX - guiLeft;
					double mY = mouseY - guiTop;
					if(mX >= 9 && mY >= 18 && mX < 169 && mY < 106){
						if((mX - 9) % 18 < 16 && (mY - 18) % 18 < 16) {
							int col = (((int) mX) - 9) / 18;
							int row = (((int) mY) - 18) / 18;
							int mIndex = (currentRow0 + row) * 9 + col;
							if(mIndex < magicList.size()) {
								this.playDownSound(Minecraft.getInstance().getSoundHandler());
				                SkillTreeContainerScreen.this.click(mIndex + 10);
				                return true;
							}
						}
					}
					else {
						int scrollBarY0 = y - guiTop  + (int)(73F * this.getCurrentScroll());
						HMLogger.DEBUG.info("mX mY: {} {}", mX, mY);
						HMLogger.DEBUG.info("sX sY: {} {}", scrollBarX0, scrollBarY0);
						if(mX >= scrollBarX0 - guiLeft && mY >= scrollBarY0&& mX < scrollBarX0 - guiLeft  + 12 && mY < scrollBarY0 + 15) {
							isScrolling = this.needScroll();
							HMLogger.DEBUG.info("scrolling {}", isScrolling);
						}
					}
				}
			}
			return false;
		}
		
	}
	
	public class PlayerInvSolt extends Widget {

		public PlayerInvSolt() {
			super(guiLeft + 9, guiTop + 112, 180, 16, "");
		}
		
		protected void init() {
			this.x = guiLeft + 9;
			this.y = guiTop + 112;
		}
		
		@Override
		public void render(int mouseX, int mouseY, float partialTicks) {
			minecraft.textureManager.bindTexture(GUI_TEXTURE);
			for(int i=0; i<10; i++) {
				drawSlot(container.getSlot(i), i);
			}
			for(int i=0; i<10; i++) {
				drawHoverSlot(container.getSlot(i), mouseX, mouseY);
			}
		}
		
		void drawHoverSlot(Slot slot, int mouseX, int mouseY) {
			int x = this.x + slot.xPos;
			int y = this.y + slot.yPos;
			ItemStack itemstack = slot.getStack();
			
			if(!itemstack.isEmpty() && mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
				this.fillGradient(x, y, x + 16, y + 16, HOVER_COLOR, HOVER_COLOR);
				SkillTreeContainerScreen.this.renderTooltip(itemstack, mouseX, mouseY);
			}
		}
		
		void drawSlot(Slot slot, int index) {
			int x = this.x + slot.xPos;
			int y = this.y + slot.yPos;
			ItemStack itemstack = slot.getStack();
			
			if(selected == index) {
				this.fillGradient(x, y, x + 16, y + 16, 0xFF00A2E8, 0xFF000055);
			}
			if(itemstack.isEmpty()) {
		         Pair<ResourceLocation, ResourceLocation> pair = slot.getBackground();
		         if (pair != null) {
		            TextureAtlasSprite textureatlassprite = minecraft.getAtlasSpriteGetter(pair.getFirst()).apply(pair.getSecond());
		            minecraft.getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
		            blit(x, y, this.getBlitOffset(), 16, 16, textureatlassprite);
		         }
			}
			else {
				itemRenderer.renderItemAndEffectIntoGUI(minecraft.player, itemstack, x, y);
		        itemRenderer.renderItemOverlayIntoGUI(font, itemstack, x, y, null);
			}
		}
		
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int keyId) {
			if (this.visible) {
				double mX = mouseX - guiLeft;
				double mY = mouseY - guiTop;
				int index = -1;
				if(mX >= 173 && mY >= 112 && mX < 189 && mY < 128) {
					index = 9;
				}
				else if(mX >= 9 && mY >= 112 && mX < 169 && mY < 128){
					if((mX - 9) % 18 < 16) {
						index = (((int) mX) - 9) / 18;
					}
				}
				if(index != -1) {
					if(keyId == 0) {
						this.playDownSound(Minecraft.getInstance().getSoundHandler());
						SkillTreeContainerScreen.this.click(index);
		                return true;
					}
					else if(keyId == 1) {
						this.playDownSound(Minecraft.getInstance().getSoundHandler());
						SkillTreeContainerScreen.this.unequlpMagic(index < 9 ? index : 40);
						SkillTreeContainerScreen.this.click(-1);
						return true;
					}
				}
			}
			return false;
		}
	}

	
}
