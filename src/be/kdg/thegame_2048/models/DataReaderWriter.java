package be.kdg.thegame_2048.models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Jarne Van Aerde, Bryan de Ridder
 * @version 1.0 24/02/2017 10:08
 */
public final class DataReaderWriter {
    /**
     * Decryption was used while loading the playerdata.
     * The decode-number is subtracted from the individuals chars
     * to go back to the right ASCII-value.
     **/
    public static List<Player> loadPlayerData() {
        Path data = Paths.get("data" + File.separator + "playerdata.txt");
        Path decoderData = Paths.get("data" + File.separator + "encryption.txt");
        List<Player> playerList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(data.toFile()))) {
            Scanner scanner = new Scanner(decoderData);
            if (!scanner.hasNext()) return playerList;
            int decodeNumber = Integer.parseInt(scanner.nextLine());

            String playerInfo = reader.readLine();
            if (playerInfo == null || playerInfo.isEmpty()) return playerList;
            while (playerInfo != null) {
                String[] splittedData = playerInfo.split(":");

                //DECRYPT THE PLAYER DATA
                String decodedName = "";
                String decodedScore = "";
                for (int i = 0; i < splittedData[0].length(); i++) {
                    char decodedLetter = ((char) (splittedData[0].charAt(i) - decodeNumber));
                    decodedName = decodedName + String.valueOf(decodedLetter);
                }
                for (int i = 0; i < splittedData[1].length(); i++) {
                    char decodedNumber = ((char) (splittedData[1].charAt(i) - decodeNumber));
                    decodedScore = decodedScore + decodedNumber;
                }
                Player player = new Player(decodedName, Integer.parseInt(decodedScore));
                playerList.add(player);

                playerInfo = reader.readLine();
            }
        } catch (IOException e) {
            writeToLog(e.getMessage());
        }
        return playerList;
    }

    /**
     * Incription was used while writing the playerdata.
     **/
    public static void savePlayerData(List<Player> playerList) {
        Path playerdata = Paths.get("data");
        Path data = playerdata.resolve("playerdata.txt");
        Path encription = playerdata.resolve("encryption.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(data.toFile()))) {
            if (!Files.exists(playerdata)) Files.createDirectory(playerdata);
            if (!Files.exists(data)) Files.createFile(data);
            if (!Files.exists(encription)) Files.createFile(encription);

            String playerInfo = "";
            Random random = new Random();
            int randomEncriptionCode = random.nextInt(100) + 10;

            //ENCRYPTING THE PLAYER DATA
            for (Player player : playerList) {
                String encodedName = "";
                String encodedScore = "";
                for (int i = 0; i < player.getName().length(); i++) {
                    char encodedLetter = ((char) (player.getName().charAt(i) + randomEncriptionCode));
                    encodedName = encodedName + String.valueOf(encodedLetter);
                }
                for (int i = 0; i < String.valueOf(player.getBestScore()).length(); i++) {
                    char encodedNumber = ((char) (String.valueOf(player.getBestScore()).charAt(i) + randomEncriptionCode));
                    encodedScore = encodedScore + encodedNumber;
                }


                playerInfo += encodedName + ":" + encodedScore + "\n";

            }
            writer.write(playerInfo);
            new Formatter(encription.toFile()).format(String.valueOf(randomEncriptionCode)).close();
        } catch (IOException e) {
            writeToLog(e.getMessage());
        }
    }

    /**
     * Writes all error messages to a text file
     **/
    public static void writeToLog(String message) {
        Path playerData = Paths.get("data");
        Path errorMessage = playerData.resolve("errorLog.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorMessage.toFile()))) {
            if (!Files.exists(playerData)) Files.createDirectory(playerData);
            if (!Files.exists(errorMessage)) Files.createFile(errorMessage);

            writer.write(message);
        } catch (IOException e) {
            System.out.println("Fundamental problem with the log-IO. Contact support!");
            System.exit(1);
        }
    }
}
