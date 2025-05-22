package com.egov.matchservice;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class MainRestController
{
    @Autowired
    ProConLinkRepository proConLinkRepository;

    @PostMapping("/proconlink")
    public ResponseEntity<?> connectProjectContractors(@RequestBody ProConLink proConLink)
    {
        proConLinkRepository.save(proConLink);
        return ResponseEntity.ok("Project Contractor Links Saved");
    }

}
