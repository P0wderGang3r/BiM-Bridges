package org.granat.ui.gui.runtime;


import org.granat.ui.gui.render.Window;

import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Server implements Runnable {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    private Operation operation;

    private Operation pendingOperation;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private final int tickrate = 10;

    private final int delta = 1000 / tickrate;

    private int currentPendingAwaitStatus = 0;

    private final int maxPendingAwaitStatus = tickrate / 4;

    public void doNothing() { }

    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public Server(
            Window window
    ) {
        this.window = window;
        resetPendingOperation();
        resetCurrentOperation();
    }

    //?----------------------------------------------------------------------------------------------------------PRIVATE

    private void resetPendingOperation() {
        this.pendingOperation = new Operation(State.READY, List.of(this::doNothing));
    }

    private void resetCurrentOperation() {
        this.operation = new Operation(State.READY, List.of(this::doNothing));
    }

    private void setCurrentOperation(Operation pendingOperation) {
        this.operation = pendingOperation;
    }

    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    public State getCurrentState() {
        return this.operation.getState();
    }

    public void setPendingOperation(State pendingState, List<IJob> pendingJob) {
        this.currentPendingAwaitStatus = 0;
        if (pendingJob == null) pendingJob = List.of(this::doNothing);
        this.pendingOperation = new Operation(pendingState, pendingJob);
    }

    //?----------------------------------------------------------------------------------------------------------RUNTIME

    private final Runnable work = () -> {
        this.operation.getJob().forEach(IJob::work);
        resetCurrentOperation();
    };

    private void nextTick() {
        //Если какая-либо операция выполняется, то изменяем оставшееся время ожидания следующей операции
        if (this.operation.getState() != State.READY) {
            this.currentPendingAwaitStatus += 1;
            if (this.currentPendingAwaitStatus >= this.maxPendingAwaitStatus)
                resetPendingOperation();
            return;
        }

        //Если же сервер в ожидании работы, устанавливаем следующее событие
        setCurrentOperation(pendingOperation);

        //Если требует выполнения какая-либо операция, то запускаем новый поток
        if (this.operation.getState() != State.READY) new Thread(work).start();
    }

    @Override
    public void run() {
        while ( !glfwWindowShouldClose(window.getWindow()) ) {
            try { Thread.sleep(delta); }
            catch (Exception ignored) { }
            nextTick();
        }
    }
}
