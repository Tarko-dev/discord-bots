package me.janmatura;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) {

        JDA bot = JDABuilder.createDefault("MTA1NzMyNTUwOTcyNzUwNjUwMg.GQ4-g6.KDwWxDssXEdcSdAdGio9X9EmDeenzlqV_8-bis")
                .setActivity(Activity.playing("Type /picks"))
                .addEventListeners(new DiscordBot())
                .build();

        bot.updateCommands().addCommands(
                Commands.slash("picks", "For your smoothest 1v1 pick and bans")
                .addOption(OptionType.STRING, "firstban","First champion to ban",true)
                .addOption(OptionType.STRING, "secondban","Second champion to ban",true),
                Commands.slash("help", "Show a little guide")
        ).queue();

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "picks":
                event.reply("Lets Rumble... and here they are -> YOUR CHAMPIONS").setEphemeral(true);
                String champion1 = event.getOption("firstban", OptionMapping::getAsString);
                String champion2 = event.getOption("secondban", OptionMapping::getAsString);

                List<String> championPool = loadChampionPool();
                List<String> pickableChampionPool;

                if(!(championPool.contains(champion1))||!(championPool.contains(champion2))){
                System.out.println("spatne zadany champici");
                event.reply("One of the champions: [" + champion1 + "] or [" + champion2 + "], does not exists.\n"+ "Hint: Check spelling and capital letters.").queue();
                break;
            }
            pickableChampionPool = banChampions(championPool, champion1, champion2);

                List<String> pickableChampionPool2 = new ArrayList<>(pickableChampionPool);

                String pick1 = pickChampion(pickableChampionPool);
                String pick2 = pickChampion(pickableChampionPool);
                String pick3 = pickChampion(pickableChampionPool);

                String pick4 = pickChampion(pickableChampionPool2);
                String pick5 = pickChampion(pickableChampionPool2);
                String pick6 = pickChampion(pickableChampionPool2);

                String message = champion1 + " and " + champion2 + " were banned!! \n\n" +
                        "+-----P1-----+\n["+
                        pick1 + "]\n["+
                        pick2 + "]\n["+
                        pick3 + "]\n\n" +
                        "+-----P2-----+\n["+
                        pick4 + "]\n["+
                        pick5 + "]\n["+
                        pick6 + "]\n";

                System.out.println(message);

                event.reply(message).queue();
                break;
            case "help":
                event.reply("/picks for generating a 1v1 scenario of three available pick for each player, under condition of two bans.");
        }
    }

    private static String pickChampion(List<String> championPool) {
        Random rand = new Random();
        String randomChampion = championPool.get(rand.nextInt(championPool.size()));
        championPool.remove(randomChampion);
        return randomChampion;
    }

    private static List<String> loadChampionPool() {
        List<String> championPool = new ArrayList<>();
        String fileName = "src/main/resources/ChampionPool.txt";
        Path path = Paths.get(fileName);
        try {
            championPool = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return championPool;
    }

    private static List<String> banChampions (List<String> championPool, String champion1, String champion2){
        championPool.remove(champion1);
        championPool.remove(champion2);
        return championPool;
    }
}
