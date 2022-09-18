package com.journey.central.service;

import com.journey.central.model.Dialogue;

public interface DialogueService {
    void createDialogue(Dialogue dialogue);
    void updateDialogue(Dialogue dialogue);
    void saveDialogue(Dialogue dialogue);
    void deleteDialogue(Dialogue dialogue);
    Iterable<Dialogue> getAllDialogues();
}
