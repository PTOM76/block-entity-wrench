package net.pitan76.bew76.command;

import net.pitan76.bew76.config.BEWConfig;
import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.ConfigCommand;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;

import static net.pitan76.bew76.BlockEntityWrench.MOD_NAME;

public class BEWCommand extends LiteralCommand {

    @Override
    public void init(CommandSettings settings) {
        super.init(settings);

        addArgumentCommand("config", new ConfigCommand(BEWConfig.getConfig(), BEWConfig.getFile(), "[" + MOD_NAME + "]", BEWConfig::setDefault));

        addArgumentCommand("reload", new LiteralCommand() {

            @Override
            public void init(CommandSettings settings) {
                super.init(settings);
                settings.permissionLevel(3);
            }

            @Override
            public void execute(ServerCommandEvent e) {
                BEWConfig.reload();
                e.sendSuccess("[" + MOD_NAME + "] Reloaded config named \"bew76.yml\"");
            }
        });
    }

    @Override
    public void execute(ServerCommandEvent e) {
        e.sendSuccess(
                "[BlockEntityWrench] Command List: \n" +
                " - /bew76 reload ... Reload config\n" +
                " - /bew76 config ... Config command"
        );
    }
}
