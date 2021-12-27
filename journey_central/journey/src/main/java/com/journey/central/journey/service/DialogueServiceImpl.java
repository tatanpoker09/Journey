package com.journey.central.journey.service;

import com.journey.central.journey.dao.DialogueRepository;
import com.journey.central.journey.model.Dialogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class DialogueServiceImpl implements DialogueService {
    @Autowired
    private DialogueRepository dialogueRepository;

    @Override
    public void createDialogue(Dialogue dialogue) {
        dialogueRepository.save(dialogue);
    }

    @Override
    public void updateDialogue(Dialogue dialogue) {
        if(dialogueRepository.findById(dialogue.getId()).isPresent()) {
            Dialogue dialogueToUpdate = dialogueRepository.findById(dialogue.getId()).get();
            dialogueToUpdate.setDomain(dialogue.getDomain());
            dialogueToUpdate.setId(dialogue.getId());
            dialogueToUpdate.setIntent(dialogue.getIntent());
            dialogueToUpdate.setPrompt(dialogue.getPrompt());
            dialogueToUpdate.setResponse(dialogue.getResponse());
            dialogueRepository.save(dialogueToUpdate);
        } else {
            //return 403
        }
    }

    @Override
    public void saveDialogue(Dialogue dialogue) {
        dialogueRepository.save(dialogue);
    }

    @Override
    public void deleteDialogue(Dialogue dialogue) {
        dialogueRepository.delete(dialogue);
    }

    @Override
    public Iterable<Dialogue> getAllDialogues() {
        return dialogueRepository.findAll();
    }

    @Override
    public Iterable<Dialogue> getUserDialogues(Principal user) {
        return dialogueRepository.findByUser(user.getName());
    }
}
