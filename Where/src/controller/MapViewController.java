package controller;

import helper.FacesUtils;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import manager.GroupManager;
import model.Group;
import model.User;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import com.icesoft.faces.context.effects.JavascriptContext;

/**
 * The MapViewController is the root controller of the web application. It contains 
 * all groups and users, which can be displayed on a map. 
 */
@ManagedBean(name = "mapViewController")
@ViewScoped
public class MapViewController implements Serializable {

	// Serializable UID
	private static final long serialVersionUID = -6231515488379715020L;
	private final GroupManager groupManager = FacesUtils.getGroupManager();
	// Displayed Groups
	private List<Group> groups;
	// Indicates if marker should be added to map
	private boolean updateMarker = true;
	// Current User
	private User curUser = null;
	// Latitude and Longitude of the current user
	private Double userLat = null;
	private Double userLng = null;
	// Default update interval for location submits in ms
	private int locationUpdateInterval = 5000;
	// Default interval for updating markers on the map in ms
	private int mapUpdateInterval = 10000;
	// Thread which updates the map
	private UpdateViewThread updateThread;
	// true, if updateThread should be stopped
	private boolean stopUpateThread = false;
	// Portable updateRenderer
	private PortableRenderer m_renderer;
	// Session id
	private String m_sessionId;

	/**
	 * Method is called on bean construction. The session is added to the push
	 * renderer and the updateThread is started.
	 */
	@PostConstruct
	public void postConstruct() {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final HttpSession session = (HttpSession) facesContext
				.getExternalContext().getSession(false);
		this.curUser = FacesUtils.getSecurityController().getCurUser();
		m_sessionId = session.getId();
		// Init AJAX PUSH
		// Tell the Icefaces PushRenderer that it listens to updates belonging
		// to the session id --> If PushRenderer.render(m_sessionId) is called,
		// view gets rerendered
		PushRenderer.addCurrentSession(m_sessionId);

		m_renderer = PushRenderer.getPortableRenderer();
		updateThread = new UpdateViewThread();
		updateThread.start();
	}

	/**
	 * Before bean is destroyed, cleanup running thread
	 */
	@PreDestroy
	public void preDestroy() {
		PushRenderer.removeCurrentSession(m_sessionId);
		if (updateThread != null && updateThread.isAlive()) {
			this.stopUpateThread = true;
			updateThread.interrupt();
		}
	}

	/**
	 * Called if the slider of the mapUpdateInterval was changed. The update
	 * Thread is interrupted and updated with the new update intervall
	 * 
	 * @param e	Event
	 */
	public void mapUpdateIntervalChanged(ValueChangeEvent e) {
		this.mapUpdateInterval = (int) e.getNewValue();
		this.updateThread.interrupt();
	}

	/**
	 * Called if the form, which contains the mobi:geolocation tag is submitted
	 * (performed via a JavaScript timer)
	 * 
	 * @param e
	 *            Event
	 */
	public void locationChanged(ActionEvent e) {
		// only update if location really changed
		if (curUser.getLat() != this.userLat
				&& curUser.getLng() != this.userLng) {
			this.curUser = FacesUtils.getUserManager().updateUserLocation(
					curUser, this.userLat, this.userLng);
			this.updateMarker = true;
		}
	}

	/**
	 * Update the logged in user
	 * 
	 * @param user
	 */
	protected void updateUser(User user) {
		this.curUser = user;
		this.updateGeoTimer();
	}

	/**
	 * The JS GeoTimerModul is called for (re)intializing the timer of the
	 * location update
	 */
	private void updateGeoTimer() {
		JavascriptContext
				.addJavascriptCall(FacesContext.getCurrentInstance(),
						"GeoTimerModul.initTimer("
								+ this.locationUpdateInterval + ");");
	}

	/**
	 * Called if the value of the locationUpdateInterval changed
	 * 
	 * @param e
	 */
	public void locationUpdateIntervalChanged(ValueChangeEvent e) {
		this.locationUpdateInterval = (int) e.getNewValue();
		this.updateGeoTimer();
	}

	/**
	 * Magic method which is called before the re(rendering) of the current view
	 * ends. If updateMarker is true, markers are added to the map. 
	 */
	public void preRenderView() {
		if (updateMarker) {
			this.addMarker();
			updateMarker = false;
		}
	}

	/**
	 * Called if the logout button is pressed
	 * 
	 * @param e
	 *            EVent
	 */
	public void logout(ActionEvent e) {
		// Delete curUser
		this.curUser = null;
		// Logout user
		FacesUtils.getSecurityController().logout();
		// Reset Interval
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"GeoTimerModul.resetTimer();");
	}

	/**
	 * This method get the current groups from the database and updates the
	 * markers on the google map via calling the MapMarkerModul
	 * 
	 * @return
	 */
	public String addMarker() {
		// Get new Groups
		groups = this.groupManager.getGroups();
		// Remove existing markers/clusters from gmap
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"MapMarkerModul.clearCluster();");
		// Add new Marker
		for (Group g : this.groups) {
			final String groupColor = g.getColor().replace("#", "");
			for (User u : g.getUsers()) {
				// Only add user to map who have existing lat/longitude
				if (u.getLat() != null && u.getLng() != null) {
					JavascriptContext.addJavascriptCall(
							FacesContext.getCurrentInstance(),
							"MapMarkerModul.addMarker(" + u.getLat() + ","
									+ u.getLng() + ",'" + groupColor + "','"
									+ u.getName() + "','" + u.getId() + "');");
				}
			}
		}
		// Init cluster
		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(),
				"MapMarkerModul.initCluster();");
		return "";
	}

	/**
	 * Performs a sleep of mapUdateInterval milliseconds and forces the
	 * pushrenderer to rerender the session
	 */
	private class UpdateViewThread extends Thread {
		public void run() {
			{
				while (!stopUpateThread) {
					try {
					
						// Thleap mapUdateInterval milliseconds
						Thread.sleep(mapUpdateInterval);
						groups = null;
						updateMarker = true;
						// Rerender
						m_renderer.render(m_sessionId);
					} catch (InterruptedException e) {
						
					}
				}
			}
		}
	}

	/***
	 * GETTER AND SETTER
	 ***/

	public User getCurUser() {
		return curUser;
	}

	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void posValueChanged(ValueChangeEvent e) {
		
	}

	public void posChanged(ActionEvent e) {
		
	}

	public Double getUserLng() {
		return userLng;
	}

	public void setUserLng(Double userLng) {
		this.userLng = userLng;
	}

	public Double getUserLat() {
		return userLat;
	}

	public List<User> getUsers() {
		return this.groups.get(0).getUsers();
	}

	public void setUsers(List<User> user) {

	}

	public void setUserLat(Double userLat) {
		this.userLat = userLat;
	}

	public int getMapUpdateInterval() {
		return mapUpdateInterval;
	}

	public void setMapUpdateInterval(int mapUpdateInterval) {
		this.mapUpdateInterval = mapUpdateInterval;
	}

	public int getLocationUpdateInterval() {
		return locationUpdateInterval;
	}

	public void setLocationUpdateInterval(int locationUpdateInterval) {
		this.locationUpdateInterval = locationUpdateInterval;
	}

}
