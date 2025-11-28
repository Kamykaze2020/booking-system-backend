package com.generatik.challenge.adspace.api;

import com.generatik.challenge.adspace.api.dto.AdSpaceDetailsDto;
import com.generatik.challenge.adspace.api.dto.AdSpaceDto;
import com.generatik.challenge.adspace.model.AdSpaceType;
import com.generatik.challenge.adspace.service.AdSpaceService;
import org.springframework.web.bind.annotation.*;
import com.generatik.challenge.adspace.api.dto.CreateAdSpaceRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ad-spaces")
public class AdSpaceController {

    private final AdSpaceService service;

    public AdSpaceController(AdSpaceService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdSpaceDto> list(@RequestParam(required = false) AdSpaceType type,
                                 @RequestParam(required = false) String city) {
        return service.listAvailable(type, city).stream().map(AdSpaceMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public AdSpaceDetailsDto get(@PathVariable Long id) {
        return AdSpaceMapper.toDetailsDto(service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdSpaceDetailsDto create(@Valid @RequestBody CreateAdSpaceRequestDto req) {
        return AdSpaceMapper.toDetailsDto(service.create(req));
    }
}

