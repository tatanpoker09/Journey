package com.journey.central.journey.integrations;

import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

public class PythonExecutor extends NotifyingThread{
    private List<String> command;
    private File file;
    private Process process;

    private static final ExecutionType EXECUTION_TYPE = ExecutionType.JYTHON;

    public PythonExecutor(List<String> command){
        this.command = command;
    }

    public PythonExecutor(File file) {
        this.file = file;
    }

    @Override
    public void doRun() {
        switch (EXECUTION_TYPE){
            case SUBPROCESS:
                runSubprocess();
                break;
            case JYTHON:
                runJython();
                break;
        }
    }

    private void runJython() {
        PythonInterpreter interpreter = new PythonInterpreter();
        Properties p = new Properties();
        PythonInterpreter.initialize(System.getProperties(), p, new String[]{ "Foo", "Bar" });
        interpreter.execfile(file.getAbsolutePath());
    }

    public void runSubprocess(){
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            process = builder.start();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Process getProcess() {
        return process;
    }
}

enum ExecutionType {
    SUBPROCESS,
    JYTHON;
}