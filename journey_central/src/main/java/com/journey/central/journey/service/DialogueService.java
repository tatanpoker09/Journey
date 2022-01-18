package com.journey.central.journey.service;

import com.journey.central.journey.model.Dialogue;

import java.security.Principal;

public interface DialogueService {
    void createDialogue(Dialogue dialogue);
    void updateDialogue(Dialogue dialogue);
    void saveDialogue(Dialogue dialogue);
    void deleteDialogue(Dialogue dialogue);
    Iterable<Dialogue> getAllDialogues();
}
