package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TrainService {

    private Train train;
    private List<Train> trainList;
    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TRAINS_PATH = "app/src/main/resources/trains.json";

    public boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    public TrainService() throws IOException {
        File trains = new File(TRAINS_PATH);
        trainList = OBJECT_MAPPER.readValue(trains, new TypeReference<List<Train>>() {
        });
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    public boolean addTrain(Train newTrain) {
        //check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream().filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();

        if (existingTrain.isPresent()) {
            //if a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
            return true;
        }
        return false;
    }

    public void updateTrain(Train updatedTrain) {
        // find the index of the train with the same trainId

        OptionalInt index = IntStream.range(0, trainList.size()).filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId())).findFirst();

        if (index.isPresent()) {
            // if found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        }else{
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            OBJECT_MAPPER.writeValue(new File(TRAINS_PATH), trainList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
