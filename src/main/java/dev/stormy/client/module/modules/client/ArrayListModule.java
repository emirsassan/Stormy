package dev.stormy.client.module.modules.client;

import dev.stormy.client.clickgui.ArrayListPosition;
import dev.stormy.client.clickgui.Theme;
import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.ComboSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.ColorUtils;
import dev.stormy.client.utils.Utils;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.util.List;

public class ArrayListModule extends Module {
   public static TickSetting editPosition, alphabeticalSort;
   public static ComboSetting<ColorModes> colorMode;
   public static int hudX = 5;
   public static int hudY = 5;
   public static Utils.HUD.PositionMode positionMode;
   public static final String HUDX_prefix = "HUDX~ ";
   public static final String HUDY_prefix = "HUDY~ ";

   public ArrayListModule() {
      super("ArrayList", Module.ModuleCategory.Client, 0);
      this.registerSetting(colorMode = new ComboSetting<>("Mode", ColorModes.Fade));
      this.registerSetting(editPosition = new TickSetting("Edit position", false));
      this.registerSetting(alphabeticalSort = new TickSetting("Alphabetical sort", false));
   }

   @Override
   public void onEnable() {
      Stormy.moduleManager.sort();
   }

   @Override
   public void guiButtonToggled(TickSetting tick) {
      if (tick == editPosition) {
         editPosition.disable();
         mc.displayGuiScreen(new ArrayListPosition());
      } else if (tick == alphabeticalSort) {
         Stormy.moduleManager.sort();
      }
   }

   @SubscribeEvent
   public void onRender(RenderGameOverlayEvent.Post ev) {
      if (Utils.Player.isPlayerInGame()) {
         if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
            return;
         }

         int margin = 2;
         int y = hudY;

         if (!alphabeticalSort.isToggled()){
            if (positionMode == Utils.HUD.PositionMode.UPLEFT || positionMode == Utils.HUD.PositionMode.UPRIGHT) {
               Stormy.moduleManager.sortShortLong();
            }
            else if(positionMode == Utils.HUD.PositionMode.DOWNLEFT || positionMode == Utils.HUD.PositionMode.DOWNRIGHT) {
               Stormy.moduleManager.sortLongShort();
            }
         }


         List<Module> en = new java.util.ArrayList<>(Stormy.moduleManager.getModules());
         if(en.isEmpty()) return;

         int textBoxWidth = Stormy.moduleManager.getLongestActiveModule(mc.fontRendererObj);
         int textBoxHeight = Stormy.moduleManager.getBoxHeight(mc.fontRendererObj, margin);

         if(hudX < 0) {
            hudX = margin;
         }
         if(hudY < 0) {
            {
               hudY = margin;
            }
         }

         if(hudX + textBoxWidth > mc.displayWidth/2){
            hudX = mc.displayWidth/2 - textBoxWidth - margin;
         }

         if(hudY + textBoxHeight > mc.displayHeight/2){
            hudY = mc.displayHeight/2 - textBoxHeight;
         }

         for (Module m : en) {
            if (m.isEnabled() && m != this) {
               if (ArrayListModule.positionMode == Utils.HUD.PositionMode.DOWNRIGHT || ArrayListModule.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
                  if (colorMode.getMode() == ColorModes.Static) {
                     mc.fontRendererObj.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRendererObj.getStringWidth(m.getName())), (float) y, Theme.getMainColor().getRGB(), true);
                     y += mc.fontRendererObj.FONT_HEIGHT + margin;
                  } else if (colorMode.getMode() == ColorModes.Fade) {
                     mc.fontRendererObj.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRendererObj.getStringWidth(m.getName())), (float) y, ColorUtils.reverseGradientDraw(Theme.getMainColor(), y).getRGB(), true);
                     y += mc.fontRendererObj.FONT_HEIGHT + margin;
                  } else if (colorMode.getMode() == ColorModes.Breathe) {
                     mc.fontRendererObj.drawString(m.getName(), (float) hudX + (textBoxWidth - mc.fontRendererObj.getStringWidth(m.getName())), (float) y, ColorUtils.gradientDraw(Theme.getMainColor(), 0).getRGB(), true);
                     y += mc.fontRendererObj.FONT_HEIGHT + margin;
                  }
               } else {
                  if (colorMode.getMode() == ColorModes.Static) {
                     mc.fontRendererObj.drawString(m.getName(), (float) hudX, (float) y, Theme.getMainColor().getRGB(), true);
                     y += mc.fontRendererObj.FONT_HEIGHT + margin;
                  } else if (colorMode.getMode() == ColorModes.Fade) {
                     mc.fontRendererObj.drawString(m.getName(), (float) hudX, (float) y, ColorUtils.reverseGradientDraw(Theme.getMainColor(), y).getRGB(), true);
                     y += mc.fontRendererObj.FONT_HEIGHT + margin;
                  } else if (colorMode.getMode() == ColorModes.Breathe) {
                     mc.fontRendererObj.drawString(m.getName(), (float) hudX, (float) y, ColorUtils.gradientDraw(Theme.getMainColor(), 0).getRGB(), true);
                     y += mc.fontRendererObj.FONT_HEIGHT + margin;
                  }
               }
            }
         }
      }

   }

   public enum ColorModes {
      Static, Fade, Breathe
   }

   public static int getHudX() {
      return hudX;
   }

   public static int getHudY() {
      return hudY;
   }

   public static void setHudX(int hudX) {
      ArrayListModule.hudX = hudX;
   }

   public static void setHudY(int hudY) {
      ArrayListModule.hudY = hudY;
   }
}
