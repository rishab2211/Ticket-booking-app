package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Train {

    @JsonProperty("train_id") // Add annotation
    private String trainId;

    @JsonProperty("train_no") // Add annotation
    private String trainNum;

    // 'seats' name matches JSON
    private List<List<Integer>> seats;

    @JsonProperty("station_times") // Add annotation
    private Map<String, String> stationTimes;

    private List<String> stations;

    public String getTrainId() {
        return this.trainId;
    }

    public String getTrainNum() {
        return this.trainNum;
    }

    public List<List<Integer>> getSeats() {
        return this.seats;
    }

    public Map<String, String> getStationTimes() {
        return this.stationTimes;
    }

    public List<String> getStations() {
        return this.stations;
    }

    public void setSeats(List<List<Integer>> seat) {
        this.seats = seat;
    }


}
