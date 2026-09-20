package net.codexarchonic.infinity.entity.custom;

import net.codexarchonic.infinity.util.VersionMethods;

import org.joml.Vector3f;

import java.awt.*;
//? if <1.21.2 {
/*import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Sheep;
*///?} else {
import net.minecraft.util.ARGB;
//?}

import net.minecraft.world.item.DyeColor;

public interface TintableEntity {
    default Vector3f particleColorFromInt(int i) {
        float f = (float)(i >> 16 & 0xFF) / 255.0f;
        float g = (float)(i >> 8 & 0xFF) / 255.0f;
        float h = (float)(i & 0xFF) / 255.0f;
        return new Vector3f(f, g, h);
    }

    static int getColorJeb(int age, int id) {
        int n = age / 25 + id;
        int o = DyeColor.values().length;
        int p = n % o;
        int q = (n + 1) % o;
        float r = (float)(age % 25) / 25.0F;
        //? if =1.21.1 {
        /*int s = Sheep.getColor(DyeColor.byId(p));
        int t = Sheep.getColor(DyeColor.byId(q));
        *///?} else {
        int s = DyeColor.byId(p).getFireworkColor();
        int t = DyeColor.byId(q).getFireworkColor();
        //?}


        //? if <1.21.2 {
        /*return FastColor.ARGB32
        *///?} else {
        return ARGB
        //?}
        //~ if >26 'lerp'->'linearLerp'
        .lerp(r, s, t);
    }

    int getColorNamed();

    static int getColorNamed(String name, int age, int id) {
        if ("jeb_".equals(name)) {
            return TintableEntity.getColorJeb(age, id);
        }
        if ("hue".equals(name)) {
            float hue = age / 400.f + id;
            hue = hue - (int) hue;
            return Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
        }
        return -1;
    }

    default int getColorForRender() {
        int v = getColorNamed();
        if (v!=-1) return v;
        return VersionMethods.opaque(this.getColor());
    }

    default int getColor() {
        return 0;
    }
}
