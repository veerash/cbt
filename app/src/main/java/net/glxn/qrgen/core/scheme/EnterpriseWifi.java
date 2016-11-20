package net.glxn.qrgen.core.scheme;

import java.util.Map;

import static net.glxn.qrgen.core.scheme.SchemeUtil.getParameters;

/**
 * Encodes a Wifi connection containing a username / password scheme for authentication, format is:
 * <code>WIFI:S:SSID;U:USER;P:PSK;E:EAP;PH:PHASE;;</code>
 */
public class EnterpriseWifi extends Wifi {
    public static final String USER = "U";
    public static final String EAP = "E";
    public static final String PHASE = "PH";
    private String user;
    private String eap;
    private String phase;

    public EnterpriseWifi() {
    }

    public static EnterpriseWifi parse(final String wifiCode) {
        if (wifiCode == null || !wifiCode.startsWith(WIFI_PROTOCOL_HEADER)) {
            throw new IllegalArgumentException(
                    "this is not a valid WIFI code: " + wifiCode);
        }
        EnterpriseWifi wifi = new EnterpriseWifi();
        Map<String, String> parameters = getParameters(
                wifiCode.substring(WIFI_PROTOCOL_HEADER.length()), "(?<!\\\\);");
        if (parameters.containsKey(SSID)) {
            wifi.setSsid(unescape(parameters.get(SSID)));
        }
        if (parameters.containsKey(PSK)) {
            wifi.setPsk(unescape(parameters.get(PSK)));
        }
        if (parameters.containsKey(USER)) {
            wifi.setUser(unescape(parameters.get(USER)));
        }
        if (parameters.containsKey(EAP)) {
            wifi.setEap(unescape(parameters.get(EAP)));
        }
        if (parameters.containsKey(PHASE)) {
            wifi.setPhase(unescape(parameters.get(PHASE)));
        }
        if (parameters.containsKey(HIDDEN)) {
            wifi.setHidden(parameters.get(HIDDEN));
        }
        return wifi;
    }

    public static String escape(final String text) {
        return text.replace("\\", "\\\\").replace(",", "\\,")
                .replace(";", "\\;").replace(".", "\\.")
                .replace("\"", "\\\"").replace("'", "\\'");
    }

    public static String unescape(final String text) {
        return text.replace("\\\\", "\\").replace("\\,", ",")
                .replace("\\;", ";").replace("\\.", ".")
                .replace("\\\"", "\"").replace("\\'", "'");
    }

    public EnterpriseWifi withUser(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        withUser(user);
    }

    public String getUser() {
        return user;
    }

    public EnterpriseWifi withEap(String eap) {
        this.eap = eap;
        return this;
    }

    public void setEap(String eap) {
        withEap(eap);
    }

    public String getEap() {
        return eap;
    }

    public EnterpriseWifi withPhase(String phase) {
        this.phase = phase;
        return this;
    }

    public void setPhase(String phase) {
        withPhase(phase);
    }

    public String getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        StringBuilder bob = new StringBuilder(WIFI_PROTOCOL_HEADER);
        if (getSsid() != null) {
            bob.append(SSID).append(":").append(escape(getSsid())).append(";");
        }
        if (getUser() != null) {
            bob.append(USER).append(":").append(escape(getUser())).append(";");
        }
        if (getPsk() != null) {
            bob.append(PSK).append(":").append(escape(getPsk())).append(";");
        }
        if (getEap() != null) {
            bob.append(EAP).append(":").append(escape(getEap())).append(";");
        }
        if (getPhase() != null) {
            bob.append(PHASE).append(":").append(escape(getPhase())).append(";");
        }
        bob.append(HIDDEN).append(":").append(isHidden()).append(";");
        return bob.toString();
    }
}
