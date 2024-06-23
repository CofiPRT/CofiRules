package ro.cofi.cofirules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.cofi.cofirules.rules.RuleRepo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public static final String VERSION = "1.0";

    // disable HTML escaping because we're using HTML-like tags for formatting
    private static final Gson PRETTY_GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final File file;
    private RuleRepo repo;

    public Config() throws IOException {
        this.file = new File(Paths.get("config", CofiRules.MOD_ID, "config.json").toUri());

        reload();
    }

    public void reload() throws IOException {
        // create directory if necessary
        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException(String.format("Could not create config directory %s", dir.getPath()));

        // if the file does not exist, save the default config
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                repo = RuleRepo.DEFAULT;
                PRETTY_GSON.toJson(repo, writer);
            } catch (IOException | JsonParseException e) {
                throw new IOException(String.format("Could not save default config to %s", file.getAbsolutePath()), e);
            }

            LOGGER.info("Saved default config to {}", file.getAbsolutePath());

            return;
        }

        // otherwise, load it
        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            repo = PRETTY_GSON.fromJson(reader, RuleRepo.class);
        } catch (IOException | JsonParseException e) {
            throw new IOException(String.format("Could not load config file %s", file.getAbsolutePath()), e);
        }

        LOGGER.info("Loaded config from {}", file.getAbsolutePath());
    }

    public RuleRepo getRepo() {
        return repo;
    }

}
