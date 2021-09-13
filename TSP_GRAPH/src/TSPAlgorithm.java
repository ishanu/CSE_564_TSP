import java.util.ArrayList;
import java.util.List;

/**
 * This class runs the travelling sales person algorithm to find the optimal route between the points.
 * It uses the Simulated Annealing Algorithm.
 * @author : Ishanu Dhar (ID: 1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 */
public class TSPAlgorithm {
    public static final double COOLING_RATE=0.005;
    public static final double TEMP_MIN = 0.99;
    public static final double COOLING_PERIOD = 30;
    public TSPRoute tspRoute;

    /**
     * This constructor initiates the points for which the algorithm needs to run
     * @param cities: the list of cities
     */
    public TSPAlgorithm(List<double[]> cities) {
        List<City> cityList = new ArrayList<>();
        cities.forEach(n->{
            cityList.add(new City(n[0]+"",n[1],n[2]));
        });
        tspRoute = new TSPRoute();
        tspRoute.cities = new ArrayList<>();
        tspRoute.cities.addAll(cityList);
    }

    /**
     * This method calculates the optimal route using the SA algorithm and return the list of cities
     * @return return the list of cities in th order of routing
     */
    public List<City> findRoute() {
        TSPRoute shortestRoute = createTspRoute(tspRoute);
        TSPRoute adjacentRoute;
        int initialTemperature=999;
        int breakEven=0;
        while(initialTemperature>TEMP_MIN) {
            if(++breakEven == COOLING_PERIOD) {
                break;
            }
            TSPRoute route = createTspRoute(tspRoute);
            adjacentRoute=obtainAdjacentRoute(route);
            if(tspRoute.getTotalDistance()<shortestRoute.getTotalDistance()) {
                shortestRoute=createTspRoute(tspRoute);
            }
            if (acceptRoute(tspRoute.getTotalDistance(), adjacentRoute.getTotalDistance(),initialTemperature)) {
                tspRoute = createTspRoute(adjacentRoute);
            }
            initialTemperature*=1-COOLING_RATE;
        }
        return shortestRoute.cities;
    }

    private TSPRoute createTspRoute(TSPRoute tspRoute) {
        TSPRoute route = new TSPRoute();
        route.cities = new ArrayList<>();
        route.cities.addAll(tspRoute.cities);
        return route;
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
            x1=(int) (route.cities.size()*Math.random());
            x2=(int) (route.cities.size()*Math.random());
        }
        City city1 = route.cities.get(x1);
        City city2 = route.cities.get(x2);
        route.cities.set(x2, city1);
        route.cities.set(x1,city2);
        return route;
    }

}