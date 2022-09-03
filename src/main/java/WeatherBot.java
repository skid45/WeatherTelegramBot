import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class WeatherBot extends TelegramLongPollingBot {

    Properties properties = new Properties();
    private final String TOKEN = properties.getProperty("telegram.bot.token");

    private final String Username = properties.getProperty("telegram.bot.username");

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return Username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasLocation()) {
                try {
                    handleLocation(message);
                } catch (IOException | InterruptedException | TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    handleMessage(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleLocation(Message message) throws IOException, InterruptedException, TelegramApiException {
        Location location = message.getLocation();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/informers?lat=" + location.getLatitude() + "&lon=" + location.getLongitude()))
                .header("X-Yandex-API-Key", properties.getProperty("yandex.weather.api.token"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Weather weather = parseResponse(response);
        SendMessage outMessage = SendMessage.builder()
                .text(nowWeatherMessage(weather))
                .chatId(message.getChatId().toString())
                .build();
        execute(outMessage);
    }

    private void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case Commands.start:
                        SendMessage outMessage = SendMessage.builder()
                                .text("Пожалуйста, отправьте свою геопозицию:")
                                .chatId(message.getChatId().toString())
                                .build();
                        setButtons(outMessage);
                        execute(outMessage);

                }
            }
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton locationButton = new KeyboardButton(Emoji.round_pushpin + " Поделиться своей геолокацией");
        locationButton.setRequestLocation(true);
        keyboardFirstRow.add(locationButton);

        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public Weather parseResponse(HttpResponse<String> response) {
        JSONObject object = new JSONObject(response.body());
        JSONObject factObj = object.getJSONObject("fact");
        Weather weather = new Weather();
        weather.setTemperature(factObj.getInt("temp"));
        weather.setFeels_like(factObj.getInt("feels_like"));
        weather.setCondition(factObj.getString("condition"));
        weather.setWind_speed(factObj.getDouble("wind_speed"));
        weather.setWind_dir(factObj.getString("wind_dir"));
        weather.setHumidity(factObj.getInt("humidity"));

        return weather;
    }

    public String nowWeatherMessage(Weather weather) {
        String condition = weather.getCondition();
        String temperature = Emoji.thermometer + " Температура: " + weather.getTemperature() +
                " (Ощущается как " + weather.getFeels_like() + ")";
        String wind = Emoji.wind + " Ветер: " + weather.getWind_speed() + ", " + weather.getWind_dir();
        String humidity = Emoji.droplet + " Влажность: " + weather.getHumidity();

        return condition + "\n" +
                temperature + "\n" +
                wind + "\n" +
                humidity;
    }
}
