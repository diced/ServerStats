package me.diced.serverstats.common.config;

import java.nio.file.Path;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class zConfigLoader<T> {
    private final YamlConfigurationLoader loader;
    private final Class<T> clazz;
    private final Path path;

    public ConfigLoader(Class<T> clazz, Path path) {
        this.clazz = clazz;
        this.path = path;
        this.loader = YamlConfigurationLoader.builder()
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
