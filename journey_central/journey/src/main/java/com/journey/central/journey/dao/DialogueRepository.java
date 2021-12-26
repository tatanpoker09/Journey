package com.journey.central.journey.dao;

import com.journey.central.journey.model.Dialogue;
import org.springframework.data.repository.CrudRepository;

public interface DialogueRepository extends CrudRepository<Dialogue, Long> {

    Iterable<Dialogue> findByUser(String name);//Todo work on this.
}
