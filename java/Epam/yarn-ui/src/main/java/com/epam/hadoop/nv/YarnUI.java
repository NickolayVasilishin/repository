package com.epam.hadoop.nv;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.logging.LogFactory;

import com.epam.hadoop.nv.tomcat.Main;
import com.epam.hadoop.nv.yarn.Client;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("theme")
@Widgetset("com.epam.hadoop.nv.AppWidgetset")
public class YarnUI extends UI {
	private List<String> args = new ArrayList<>();
	private ProgressBar bar;
	private TextField result;
	private YarnUI instance;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		instance = this;
		final GridLayout page = new GridLayout(3, 3);

		final VerticalLayout layout = new VerticalLayout();

		final TextField masterRam = new TextField();
		masterRam.setCaption("Master Ram:");
		final TextField containerRam = new TextField();
		containerRam.setCaption("Container Ram:");
		final TextField mastervCores = new TextField();
		mastervCores.setCaption("Mseter vCores:");
		final TextField containervCores = new TextField();
		containervCores.setCaption("Container vCores:");
		final TextField containers = new TextField();
		containers.setCaption("Containers:");
		final TextField command = new TextField();
		command.setCaption("Command:");
		final TextField arguments = new TextField();
		arguments.setCaption("Command Arguments:");

		bar = new ProgressBar();
		bar.setIndeterminate(true);
		bar.setVisible(false);

		result = new TextField("Application results");
		result.setVisible(false);

		Button button = new Button("Submit");
		button.addClickListener(e -> {
			setMasterMemory(masterRam.getValue());
			setMastervCores(mastervCores.getValue());
			setContainerMemory(containerRam.getValue());
			setContainervCores(containervCores.getValue());
			setContainers(containers.getValue());
			setScript(command.getValue());
			setScriptArgs(arguments.getValue());
			try {
				LogFactory.getLog(YarnUI.class).info("Starting client");
				bar.setVisible(true);
				Logger.getLogger(YarnUI.class.getName()).info("Starting client");
				args.add("--jar");
				args.add(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()
						.replaceAll("\\\\", "/"));
				new Thread(new Runnable() {
					@Override
					public void run() {
						Client.main(instance, args.toArray(new String[] {}));
					}
				}).start();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		});

		layout.addComponents(masterRam, mastervCores, containerRam, containervCores, containers, command, arguments,
				button);
		layout.setMargin(true);
		layout.setSpacing(true);

		page.addComponent(layout, 0, 0, 0, 2);
		page.addComponent(bar, 1, 1);
		page.addComponent(result, 2, 1);

		setContent(page);
	}

	private void setScriptArgs(String value) {
		args.add("--shell_args");
		args.add(value);
	}

	private void setScript(String value) {
		args.add("--shell_script");
		args.add(value);
	}

	private void setContainers(String value) {
		args.add("--num_containers");
		args.add(value);
	}

	private void setMastervCores(String value) {
		args.add("--master_vcores");
		args.add(value);
	}

	private void setContainervCores(String value) {
		args.add("--container_vcores");
		args.add(value);
	}

	private void setMasterMemory(String value) {
		args.add("--master_memory");
		args.add(value);
	}

	private void setContainerMemory(String value) {
		args.add("--container_memory");
		args.add(value);
	}

	@WebServlet(urlPatterns = "/*", name = "YarnUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = YarnUI.class, productionMode = false)
	public static class YarnUIServlet extends VaadinServlet {
	}

	public void notifyNotStarted() {
		notifyUI("Application has not started.");
	}

	public void notifyWrongInput() {
		notifyUI("Application has not started due to wrong arguments.");
	}

	public void notifyError() {
		notifyUI("Application has been failed due to error.");
	}

	public void notifyCompleted() {
		notifyUI("Application has been succeeded.");
	}

	public void notifyFailed() {
		notifyUI("Application has been failed.");
	}

	private void notifyUI(String message) {
		bar.setVisible(false);
		result.setValue(message);
		result.setVisible(true);
	}
}
