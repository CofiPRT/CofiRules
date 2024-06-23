package ro.cofi.cofirules.rules;

import ro.cofi.cofirules.Config;

import java.util.List;

public record RuleRepo(String header, String format, String version, List<Rule> rules) {
    public static RuleRepo DEFAULT = new RuleRepo(
        "<bold><italic><green>No Rules</green> (A lawless server)</italic></bold>",
        String.format(
            "<bold>%s. <green>%s</green></bold>\n%s",
            RuleVariables.INDEX.getFormat(),
            RuleVariables.TITLE.getFormat(),
            RuleVariables.DESCRIPTION.getFormat()
        ),
        Config.VERSION,
        List.of(
            new Rule("Please contact a moderator", "If you see <italic>this message</italic>")
        )
    );
}
