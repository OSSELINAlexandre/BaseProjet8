package tourGuide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
	}

	/**
	 * 
	 * Done
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping("/getNearbyAttractions")
	public String getNearbyAttractions(@RequestParam String userName) {

		// TODO: Change this method to no longer return a List of Attractions.
		// Instead: Get the closest five tourist attractions to the user - no matter how
		// far away they are.
		// Return a new JSON object that contains:
		// Name of Tourist attraction,
		// Tourist attractions lat/long,
		// The user's location lat/long,
		// The distance in miles between the user's location and each of the
		// attractions.
		// The reward points for visiting each Attraction.
		// Note: Attraction reward points can be gathered from RewardsCentral

		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(tourGuideService.getNearByFifthClosestAttractions(visitedLocation));
	}

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
	}

	/**
	 * 
	 * This method will highly depend on the way the data is stored into the system.
	 * We are working with in Memory users, therefore there is only one way of
	 * retrieving this information.
	 * 
	 * @return
	 */
	@RequestMapping("/getAllCurrentLocations")
	public String getAllCurrentLocations() {
		// TODO: Get a list of every user's most recent location as JSON
		// - Note: does not use gpsUtil to query for their current location,
		// but rather gathers the user's current location from their stored location
		// history.
		//
		// Return object should be the just a JSON mapping of userId to Locations
		// similar to:
		// {
		// "019b04a9-067a-4c76-8817-ee75088c3822":
		// {"longitude":-48.188821,"latitude":74.84371}
		// ...
		// }

		/**
		 * 
		 * DONE
		 */

		Map<String, User> inMemoryDb = tourGuideService.getInternalUserMap();
		Map<String, Location> result = new HashMap<>();

		for (Map.Entry<String, User> f : inMemoryDb.entrySet()) {

			VisitedLocation lastVisitedLocForUser = f.getValue().getLastVisitedLocation();

			Location newItem = lastVisitedLocForUser.location;

			result.put(f.getValue().getUserId().toString(), newItem);
		}

		return JsonStream.serialize(result);
	}

	@RequestMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
		return JsonStream.serialize(providers);
	}

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

}