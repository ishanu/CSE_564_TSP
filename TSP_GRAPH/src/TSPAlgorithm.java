import java.util.ArrayList;
import java.util.List;

public class TSPAlgorithm {
    public static final double COOLING_RATE=0.005;
    public static final double TEMP_MIN = 0.99;
    public static final double COOLING_PERIOD = 30;
    public TSPRoute tspRoute;

    public TSPAlgorithm(List<double[]> cities) {
        List<City> cityList = new ArrayList<>();
        cities.forEach(n->{
            cityList.add(new City(n[0]+"",n[1],n[2]));
        });
        tspRoute = new TSPRoute(cityList);
    }
    public List<City> findRoute() {
        TSPRoute shortestRoute = new TSPRoute(tspRoute);
        TSPRoute adjacentRoute;
        int initialTemperature=999;
        int breakEven=0;
        while(initialTemperature>TEMP_MIN) {
            if(++breakEven == COOLING_PERIOD) {
                break;
            }
            adjacentRoute=obtainAdjacentRoute(new TSPRoute(tspRoute));
            if(tspRoute.getTotalDistance()<shortestRoute.getTotalDistance()) shortestRoute=new TSPRoute(tspRoute);
            if (acceptRoute(tspRoute.getTotalDistance(), adjacentRoute.getTotalDistance(),initialTemperature)) tspRoute
                    = new TSPRoute(adjacentRoute);
            initialTemperature*=1-COOLING_RATE;
        }
        return shortestRoute.getCities();
    }

    private boolean acceptRoute(double currentDistance,double adjacentDistance,double temperature) {
        double acceptanceProb=1.0;
        if (adjacentDistance>=currentDistance) {
            acceptanceProb=Math.exp(-(adjacentDistance-currentDistance)/temperature);
        }
        double random=Math.random();
        return acceptanceProb>=random;
    }

    private TSPRoute obtainAdjacentRoute(TSPRoute route) {
        int x1=0,x2=0;
        while(x1==x2) {
            x1=(int) (route.getCities().size()*Math.random());
            x2=(int) (route.getCities().size()*Math.random());
        }
        City city1 = route.getCities().get(x1);
        City city2 = route.getCities().get(x2);
        route.getCities().set(x2, city1);
        route.getCities().set(x1,city2);
        return route;
    }

}