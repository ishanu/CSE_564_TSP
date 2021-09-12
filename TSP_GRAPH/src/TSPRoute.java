import java.util.ArrayList;
import java.util.List;

public class TSPRoute {
    private List<City> cities = new ArrayList<>();

    public TSPRoute(List<City> cities) {
        this.cities.addAll(cities);
    }
    public TSPRoute(TSPRoute route) {
        this.cities.addAll(route.cities);
    }
    public List<City> getCities() {return cities;}

    public double getTotalDistance() {
        int citiesSize = this.cities.size();
        return (int) (this.cities.stream().mapToDouble(x->{
            int cityIndex = this.cities.indexOf(x);
            double returnValue = 0;
            if (cityIndex<citiesSize-1) returnValue = x.measureDistance(this.cities.get(cityIndex+1));
            return returnValue;
        }).sum() + this.cities.get(citiesSize-1).measureDistance(this.cities.get(0)));
    }

}