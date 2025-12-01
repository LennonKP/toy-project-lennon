package com.example.demo.domain;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.User;
import com.example.demo.repository.entity.Workstation;

@Component
public class IslandDomainService {

    public Island encontrarIlhaEAlocar(List<Island> islands, User user) {
        if (islands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }

        Island freeIsland = islands.getFirst();
        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final int positions = slots;
            var possibleIsland = islands.stream()
                    .filter(i -> i.getWorkstations().stream()
                            .map(Workstation::getUser)
                            .filter(Objects::nonNull)
                            .count() == positions)
                    .findFirst();
            if (possibleIsland.isPresent()) {
                freeIsland = possibleIsland.get();
                break;
            }
        }

        freeIsland.assignUserToTheFirstWorkstationAvailable(user);

        return freeIsland;
    }
}
