public class Weather {
    private int temperature;
    private int feels_like;
    private String condition;
    private double wind_speed;
    private String wind_dir;
    private int humidity;


    public Weather() {
    }

    public Weather(int temperature, int feels_like, String condition, double wind_speed, String wind_dir, int humidity) {
        this.temperature = temperature;
        this.feels_like = feels_like;
        this.condition = condition;
        this.wind_speed = wind_speed;
        this.wind_dir = wind_dir;
        this.humidity = humidity;
    }

    public String getTemperature() {
        if (temperature > 0) {
            return "+" + temperature + "°C";
        }
        return temperature + "°C";
    }

    public String getFeels_like() {
        if (feels_like > 0) {
            return "+" + feels_like + "°C";
        }
        return feels_like + "°C";
    }

    public String getCondition() {
        String newCondition = "";

        switch (condition) {
            case "clear":
                newCondition = Emoji.sun + " Ясно";
                break;
            case "partly-cloudy":
                newCondition = Emoji.sun + " Малооблачно";
                break;
            case "cloudy":
                newCondition = Emoji.sun_behind_cloud + " Облачно с прояснениями";
                break;
            case "overcast":
                newCondition = Emoji.cloud + " Пасмурно";
                break;
            case "drizzle":
                newCondition = Emoji.sun_behind_rain_cloud + " Морось";
                break;
            case "light-rain":
                newCondition = Emoji.sun_behind_rain_cloud + " Небольшой дождь";
                break;
            case "rain":
                newCondition = Emoji.cloud_with_rain + " Дождь";
                break;
            case "moderate-rain":
                newCondition = Emoji.cloud_with_rain + " Умеренно сильный дождь";
                break;
            case "heavy-rain":
                newCondition = Emoji.cloud_with_rain + " Сильный дождь";
                break;
            case "continuous-heavy-rain":
                newCondition = Emoji.cloud_with_rain + " Длительный сильный дождь";
                break;
            case "showers":
                newCondition = Emoji.cloud_with_rain + " Ливень";
                break;
            case "wet-snow":
                newCondition = Emoji.cloud_with_snow + " Дождь со снегом";
                break;
            case "light-snow":
                newCondition = Emoji.snowflake + " Небольшой снег";
                break;
            case "snow":
                newCondition = Emoji.snowflake + " Снег";
                break;
            case "snow-showers":
                newCondition = Emoji.cloud_with_snow + " Снегопад";
                break;
            case "hail":
                newCondition = Emoji.ice + " Град";
                break;
            case "thunderstorm":
                newCondition = Emoji.cloud_with_lightning + " Гроза";
                break;
            case "thunderstorm-with-rain":
                newCondition = Emoji.cloud_with_lightning_and_rain + " Дождь с грозой";
                break;
            case "thunderstorm-with-hail":
                newCondition = Emoji.cloud_with_lightning + " Гроза с градом";
                break;
        }

        return newCondition;
    }

    public String getWind_speed() {
        return wind_speed + " м/c";
    }

    public String getWind_dir() {
        String newDir = "";
        switch (wind_dir) {
            case "nw":
                newDir = "С-З";
                break;
            case "n":
                newDir = "С";
                break;
            case "ne":
                newDir = "C-B";
                break;
            case "e":
                newDir = "В";
                break;
            case "se":
                newDir = "Ю-В";
                break;
            case "s":
                newDir = "Ю";
                break;
            case "sw":
                newDir = "Ю-З";
                break;
            case "w":
                newDir = "З";
                break;
            case "c":
                newDir = "Штиль";
                break;
        }
        return newDir;
    }

    public String getHumidity() {
        return humidity + "%";
    }


    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setFeels_like(int feels_like) {
        this.feels_like = feels_like;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
