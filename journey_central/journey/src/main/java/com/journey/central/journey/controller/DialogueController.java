package com.journey.central.journey.controller;

import com.journey.central.journey.model.Dialogue;
import com.journey.central.journey.service.DialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/dialogue")
public class DialogueController {

    @Autowired
    private DialogueService dialogueService;

    @PostMapping(path = "/")
    public void dialogue(@ModelAttribute("dialogue") Dialogue dialogue) {
        dialogueService.saveDialogue(dialogue);
    }
}
