package service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServiceApp extends Application<ServiceConfiguration> {

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			args = new String[2];
			args[0] = "server";
			args[1] = System.getProperty("user.dir")+ "\\microservice.yml";
		}

		new ServiceApp().run(args);
	}

	@Override
	public String getName() {
		return "LS3_microservice";
	}

	@Override
	public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {

	}

	@Override
	public void run(ServiceConfiguration configuration, Environment environment) {
		final Resource resource = new Resource();
		environment.jersey().register(resource);
	}
}