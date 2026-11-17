package net.codexarchonic.infinity.util.loading;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public final class GeneratedBiomeNames {
    private static final Pattern KEY = Pattern.compile("biome\\.infinity\\.biome_(-?[0-9]+)");
    public static boolean matches(String key) { return KEY.matcher(key).matches(); }
    public static String translate(String key, Map<String, String> translations) {
        var match = KEY.matcher(key);
        if (!match.matches()) return null;
        if (translations.containsKey(key)) return translations.get(key);
        return String.format(Locale.ROOT, translations.getOrDefault("biome.infinity.generated", "Random Biome #%s"), match.group(1));
    }
}
