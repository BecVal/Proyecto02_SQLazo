package mx.unam.ciencias.myp.butchery;

import mx.unam.ciencias.myp.butchery.view.ConsoleView;
import mx.unam.ciencias.myp.butchery.controller.ButcheryController;
import mx.unam.ciencias.myp.butchery.model.ModelFacade;

public class Main {
    public static void main(String[] args) {
        ModelFacade model = new ModelFacade();
        ButcheryController controller = new ButcheryController(model);
        ConsoleView consoleView = new ConsoleView(controller);
        consoleView.showMenu();
    }
}
