package ro.cofi.cofirules;

import com.mojang.brigadier.Command;
import eu.pb4.placeholders.api.parsers.TagParser;
import eu.pb4.placeholders.api.parsers.tag.TagRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.cofi.cofirules.rules.Rule;
import ro.cofi.cofirules.rules.RuleRepo;
import ro.cofi.cofirules.rules.RuleVariables;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CofiRules implements DedicatedServerModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CofiRules.class);

    public static final String MOD_ID = "cofi-rules";
    public static final String MOD_NAME = "Cofi Rules";

    private Config config;

    @Override
    public void onInitializeServer() {
        LOGGER.info("Initializing {}", MOD_NAME);

        try {
            this.config = new Config();
        } catch (IOException e) {
            LOGGER.error("Could not initialize config", e);
            return;
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            CommandManager.literal("rules")
                .executes(context -> {
                    context.getSource().sendFeedback(this::getRules, false);
                    return Command.SINGLE_SUCCESS;
                })
                .then(CommandManager.literal("reload")
                    .requires(source -> source.hasPermissionLevel(2)) // 2 has access to command blocks
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();

                        try {
                            config.reload();
                        } catch (IOException e) {
                            LOGGER.error("Could not reload config", e);
                            source.sendError(Text.of("Could not reload config, check logs"));
                            return -1;
                        }

                        source.sendFeedback(() -> Text.of("Successfully reloaded the rules"), true);
                        return Command.SINGLE_SUCCESS;
                    })
                )
        ));

        LOGGER.info("Successfully initialized {}", MOD_NAME);
    }

    private Text getRules() {
        RuleRepo repo = config.getRepo();
        if (repo == null)
            repo = RuleRepo.DEFAULT;

        List<String> lines = new LinkedList<>();
        lines.add(repo.header());

        String format = repo.format();
        List<Rule> rules = repo.rules();
        for (int index = 0; index < rules.size(); index++) {
            Rule rule = rules.get(index);

            String ruleStr = format
                .replace(RuleVariables.TITLE.getFormat(), rule.title())
                .replace(RuleVariables.DESCRIPTION.getFormat(), rule.description())
                .replace(RuleVariables.INDEX.getFormat(), String.valueOf(index + 1));
            lines.add(ruleStr);
        }

        return TagParser.createQuickText(TagRegistry.DEFAULT).parseNode(String.join("\n\n", lines)).toText();
    }

}
