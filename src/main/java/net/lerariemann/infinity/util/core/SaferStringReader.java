package net.lerariemann.infinity.util.core;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

public class SaferStringReader extends StringReader {
    public SaferStringReader(String string) {
        super(string);
    }

    public static CompoundTag parse(String string) throws CommandSyntaxException {
        SaferStringReader readerInner = new SaferStringReader(string);
        TagParser reader = new TagParser(readerInner);

        CompoundTag nbtCompound = reader.readStruct();
        readerInner.skipWhitespace();
        if (readerInner.canRead()) {
            throw TagParser.ERROR_TRAILING_DATA.createWithContext(readerInner);
        } else {
            return nbtCompound;
        }
    }

    @Override
    public String readStringUntil(char terminator) throws CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        while(this.canRead()) {
            char c = this.read();
            if (escaped) {
                if (c == terminator) {
                    result.append(c);
                    escaped = false;
                }
                else if (c == '\\') {
                    result.append('\\');
                }
                else {
                    result.append('\\');
                    result.append(c);
                    escaped = false;
                }
            } else if (c == '\\') {
                escaped = true;
            } else {
                if (c == terminator) {
                    return result.toString();
                }

                result.append(c);
            }
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }
}
