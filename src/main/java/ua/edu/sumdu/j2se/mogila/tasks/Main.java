package ua.edu.sumdu.j2se.mogila.tasks;

import ua.edu.sumdu.j2se.mogila.tasks.controller.Controller;
import ua.edu.sumdu.j2se.mogila.tasks.controller.ControllerInterface;
import ua.edu.sumdu.j2se.mogila.tasks.model.Model;
import ua.edu.sumdu.j2se.mogila.tasks.model.ModelInterface;
import ua.edu.sumdu.j2se.mogila.tasks.view.View;
import ua.edu.sumdu.j2se.mogila.tasks.view.ViewInterface;


import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		ModelInterface model = new Model();
		ViewInterface view = new View();
		ControllerInterface controller = new Controller(model, view);
		while (true) {
			controller.act();
		}
	}
}
