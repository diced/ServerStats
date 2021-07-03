package me.diced.serverstats.common.config;

import java.nio.file.Path;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

public class ConfigLoader<T> {
    private final HoconConfigurationLoader loader;
    private final Class<T> clazz;
    private final Path path;

    public ConfigLoader(Class<T> clazz, Path path) {
        this.clazz = clazz;
        this.path = path;
        this.loader = HoconConfigurationLoader.builder()
                .path(path)
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .build();

    }

    public T load() throws ConfigurateException {
        final CommentedConfigurationNode node = this.loader.load();
        final @Nullable T config = node.get(this.clazz);

        if (!this.path.toFile().exists()) {
            this.loader.save(node);
        }

        return config;
    }
}
