package org.application.prod.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "VpnClient", url = "${vpn.host}")
public interface VpnFeignClient {

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> sendPostAuthorization(@RequestBody Map<String, ?> body);

    @PostMapping(value = "/panel/api/inbounds/addClient", consumes = "application/json")
    ResponseEntity<String> addClientToInbound(@RequestBody Map<String, Object> requestBody,
                                              @RequestHeader("Cookie") String token);

    @GetMapping("/panel/api/inbounds/getClientTrafficsById/{id}")
    ResponseEntity<String> getTimeToLeft(@PathVariable("id") Long id, @RequestHeader("Cookie") String token);

    @PostMapping("/panel/api/inbounds/{inboundId}/delClient/{id}")
    ResponseEntity<String> deleteClientInbound(@PathVariable("inboundId") Long InboundId, @PathVariable("id") Long id, @RequestHeader("Cookie") String token);

    @PostMapping("/panel/api/inbounds/updateClient/{id}")
    ResponseEntity<String> updateClientInbound(@PathVariable("id") Long Id, @RequestBody Map<String, Object> requestBody,
                                               @RequestHeader("Cookie") String token);
}
