package org.hzeng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "bustracker")
public class BusTrackerSettings {
    List<RouteSetting> routeSettings = new ArrayList<>();

    public List<RouteSetting> getRouteSettings() {
        return routeSettings;
    }

    public void setRouteSettings(List<RouteSetting> routeSettings) {
        this.routeSettings = routeSettings;
    }

    public static class RouteSetting{
        String id;
        String routeFileName;
        double speed;
        double timeZero;
        double timeStopAtStation;
        double minDistanceBetweenStation;
        boolean enabled;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRouteFileName() {
            return routeFileName;
        }

        public void setRouteFileName(String routeFileName) {
            this.routeFileName = routeFileName;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getTimeZero() {
            return timeZero;
        }

        public void setTimeZero(double timeZero) {
            this.timeZero = timeZero;
        }

        public double getTimeStopAtStation() {
            return timeStopAtStation;
        }

        public void setTimeStopAtStation(double timeStopAtStation) {
            this.timeStopAtStation = timeStopAtStation;
        }

        public double getMinDistanceBetweenStation() {
            return minDistanceBetweenStation;
        }

        public void setMinDistanceBetweenStation(double minDistanceBetweenStation) {
            this.minDistanceBetweenStation = minDistanceBetweenStation;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

    }

}
