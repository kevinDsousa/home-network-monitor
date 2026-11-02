package com.homenetworkmonitor.provider;

import com.homenetworkmonitor.models.dto.DeviceAssociationDTO;
import com.homenetworkmonitor.models.enums.WifiBand;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lê os dados de dispositivos conectados direto do painel web do VSOL V2802AC.
 * As páginas status_ethernet_info.asp (DHCP) e status_wlan_info_11n.asp (associação Wi-Fi)
 * respondem sem exigir login nesse firmware.
 */
@Component
public class VsolDeviceProvider implements DeviceProvider {

    private static final Logger log = LoggerFactory.getLogger(VsolDeviceProvider.class);
    private static final Pattern DHCP_FIELD = Pattern.compile("new it\\(\"([a-zA-Z]+)\",\\s*\"([^\"]*)\"\\)");

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final String baseUrl;

    public VsolDeviceProvider(@Value("${vsol.base-url:http://192.168.1.1}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String getName() {
        return "VSOL Router Provider";
    }

    @Override
    public boolean supports(String sourceType) {
        return "VSOL".equalsIgnoreCase(sourceType);
    }

    @Override
    public List<DeviceAssociationDTO> collect() {
        Map<String, DhcpEntry> dhcpByMac = fetchDhcpClients();
        Map<String, WifiEntry> wifiByMac = fetchWifiAssociations();

        Set<String> allMacs = new LinkedHashSet<>();
        allMacs.addAll(dhcpByMac.keySet());
        allMacs.addAll(wifiByMac.keySet());

        List<DeviceAssociationDTO> result = new ArrayList<>();
        for (String mac : allMacs) {
            DhcpEntry dhcp = dhcpByMac.get(mac);
            WifiEntry wifi = wifiByMac.get(mac);

            result.add(new DeviceAssociationDTO(
                    mac,
                    dhcp != null ? dhcp.hostname() : null,
                    dhcp != null ? dhcp.ip() : null,
                    wifi != null ? wifi.band() : WifiBand.UNKNOWN,
                    wifi != null ? wifi.rssi() : null,
                    wifi != null ? wifi.packetsSent() : null,
                    wifi != null ? wifi.packetsReceived() : null,
                    wifi != null ? wifi.linktimeMinutes() : null
            ));
        }
        return result;
    }

    private Map<String, DhcpEntry> fetchDhcpClients() {
        String body = fetchBody("/status_ethernet_info.asp");
        Map<String, DhcpEntry> byMac = new LinkedHashMap<>();

        for (String line : body.split("\n")) {
            if (!line.contains("clts.push")) {
                continue;
            }
            Map<String, String> fields = new HashMap<>();
            Matcher matcher = DHCP_FIELD.matcher(line);
            while (matcher.find()) {
                fields.put(matcher.group(1), matcher.group(2));
            }
            String mac = fields.get("macAddr");
            if (mac == null || mac.isBlank()) {
                continue;
            }
            byMac.put(mac.toLowerCase(), new DhcpEntry(fields.get("devname"), fields.get("ipAddr")));
        }
        return byMac;
    }

    private Map<String, WifiEntry> fetchWifiAssociations() {
        String body = fetchBody("/status_wlan_info_11n.asp");
        Document doc = Jsoup.parse(body);
        Map<String, WifiEntry> byMac = new LinkedHashMap<>();

        for (Element row : doc.select("tr.tbl_body")) {
            var cols = row.select("td");
            if (cols.size() < 9) {
                continue;
            }
            String mac = cols.get(0).text().trim().toLowerCase();
            if (mac.isBlank()) {
                continue;
            }
            WifiBand band = WifiBand.fromRouterLabel(cols.get(1).text().trim());
            Long packetsSent = parseLong(cols.get(2).text());
            Long packetsReceived = parseLong(cols.get(3).text());
            Integer rssi = parseInt(cols.get(5).text());
            Double linktime = parseDouble(cols.get(8).text());

            byMac.put(mac, new WifiEntry(band, rssi, packetsSent, packetsReceived, linktime));
        }
        return byMac;
    }

    private String fetchBody(String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.warn("VSOL respondeu HTTP {} em {}{}", response.statusCode(), baseUrl, path);
            }
            return response.body() != null ? response.body() : "";
        } catch (Exception e) {
            log.warn("Falha ao acessar {}{}: {}", baseUrl, path, e.toString());
            return "";
        }
    }

    private static Long parseLong(String text) {
        try {
            return Long.parseLong(text.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static Integer parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static Double parseDouble(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private record DhcpEntry(String hostname, String ip) {
    }

    private record WifiEntry(WifiBand band, Integer rssi, Long packetsSent, Long packetsReceived,
                              Double linktimeMinutes) {
    }
}
