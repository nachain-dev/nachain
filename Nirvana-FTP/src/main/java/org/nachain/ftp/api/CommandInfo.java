package org.nachain.ftp.api;

import java.io.IOException;


public class CommandInfo {

    public final Command command;
    public final String help;
    public final boolean needsAuth;

    public CommandInfo(Command command, String help, boolean needsAuth) {
        this.command = command;
        this.help = help;
        this.needsAuth = needsAuth;
    }


    @FunctionalInterface
    public interface Command {


        void run(String argument) throws IOException;

        default void run(CommandInfo info, String argument) throws IOException {
            if (argument.isEmpty()) throw new ResponseException(501, "Missing parameters");

            run(argument);
        }
    }


    @FunctionalInterface
    public interface NoArgsCommand extends Command {


        void run() throws IOException;

        @Override
        default void run(String argument) throws IOException {
            run();
        }

        @Override
        default void run(CommandInfo info, String argument) throws IOException {
            run();
        }

    }


    @FunctionalInterface
    public interface ArgsArrayCommand extends Command {


        void run(String[] argument) throws IOException;

        @Override
        default void run(String argument) throws IOException {
            run(argument.split("\\s+"));
        }

        @Override
        default void run(CommandInfo info, String argument) throws IOException {
            run(argument.split("\\s+"));
        }

    }
}
