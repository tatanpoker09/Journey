package com.journey.central.journey.controller;

import com.journey.central.journey.integrations.IntegrationManager;
import com.journey.central.journey.integrations.IntegrationTrigger;
import com.journey.central.journey.model.Dialogue;
import com.journey.central.journey.service.BluetoothService;
import com.journey.central.journey.service.DialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping(path = "/dialogue")
public class DialogueController {

    private DialogueService dialogueService;
    private BluetoothService bluetoothService;
    private IntegrationManager integrationManager;

    @Autowired
    public DialogueController(DialogueService dialogueService, BluetoothService bluetoothService, IntegrationManager integrationManager) {
        this.dialogueService = dialogueService;
        this.bluetoothService = bluetoothService;
        this.integrationManager = integrationManager;
    }

    @PostMapping(path = "/")
    public @ResponseBody String dialogue(@RequestBody Dialogue dialogue) {
        System.out.println("Received dialogue: "+dialogue);
        if(dialogue.getIntent()==null){
            return "Error";
        }
        dialogueService.saveDialogue(dialogue);
        //We now use the response to send to the speaker.

        //bluetoothService.speak(dialogue.getResponse());
        //And we finally just do whatever it should do.

        IntegrationTrigger trigger = new IntegrationTrigger(dialogue.getDomain(), dialogue.getIntent());
        HashMap<String, String> entities = dialogue.getEntities();
        integrationManager.onTrigger(trigger, entities);
        return "Saved";
    }
}
