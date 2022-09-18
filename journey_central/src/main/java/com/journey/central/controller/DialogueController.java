package com.journey.central.controller;

import com.journey.central.model.Dialogue;
import com.journey.central.service.DialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping(path = "/dialogue")
public class DialogueController {

    private DialogueService dialogueService;
    @Autowired
    public DialogueController(DialogueService dialogueService) {
        this.dialogueService = dialogueService;
    }

    @PostMapping(path = "/")
    public @ResponseBody String dialogue(@RequestBody Dialogue dialogue) {
        System.out.println("Received dialogue: "+dialogue);
        if(dialogue.getIntent()==null){
            return "Error";
        }
        dialogueService.saveDialogue(dialogue);
        //We now use the response to send to the speaker.

        return "Saved";
    }
}
