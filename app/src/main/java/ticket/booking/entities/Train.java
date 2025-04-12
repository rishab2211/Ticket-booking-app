package ticket.booking.entities;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Train {

    private  String trainId;
    private String trainNum;
    private List<List<Integer>> seats;
    private Map<String, Date> stationTimes;
    private List<String> stations;
}
