package edu.moravian;
import exceptions.SecretsException;
import exceptions.StorageException;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import secrets.Secrets;

public class DiscordManager {
    public static void main(String[] args) throws StorageException {
        GameStorage storage = createStorage();
        BotResponder responder = createResponder(storage);

        // Get the token from AWS Secrets Manager
        String token;
        try {
            Secrets secrets = new Secrets();
            token = secrets.getSecret("220_Discord_Token", "DISCORD_TOKEN");
        } catch (SecretsException e) {
            System.out.println("Failed to retrieve token: " + e.getMessage());
            return;
        }
        //String token = loadToken();

        startBot(responder, token);
    }

    private static GameStorage createStorage() throws StorageException {
        //RedisStorage storage = new RedisStorage("localhost", 6379);
        MemoryStorage storage = new MemoryStorage();
        return storage;
    }

    private static BotResponder createResponder(GameStorage storage) {
        Game game = new Game(storage);
        return new BotResponder(game);
    }

    private static String loadToken() {
        try {
            Dotenv dotenv = Dotenv.load();
            return dotenv.get("DISCORD_TOKEN");
        } catch (DotenvException e) {
            System.err.println("Failed to load .env file\n\nIs it present?");
            System.exit(1);
            return null;
        }
    }

    private static void startBot(BotResponder responder, String token) {
        System.out.println("Starting bot...");
        JDA api = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        api.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                if (event.getAuthor().isBot()) return;

                if (!event.getChannel().getName().equals("connor-channel")) return;

                String username = event.getAuthor().getName();
                String message = event.getMessage().getContentRaw();

                String response = responder.respond(username, message);
                event.getChannel().sendMessage(response).queue();
            }
        });
    }
}