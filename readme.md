# Read Me

This is a microservice build to handle some catalog business logic, working with products and following DDD philosophy.

### Description

This is an application that manages the described functionalities. Details:
- I have used Kotlin with SpringBoot and Gradle to manage dependencies.
- The structure is divided between Product and Shared. Inside them, there are three layers:
  - Application: with useCases for each functionality.
  - Domain: with the main aggregate, events, value objects, interfaces and more.
  - Infrastructure: with controllers for each functionality, dtos, repository and dependency injection config.
- The architecture of the service has been made following DDD principles, such as aggregates, value objects, and pushing all logic to the domain.
- There are domain events for each action, and a bus that handles them. But right now, as it's an API, these events have no real functionality.
- There are some unit tests for the use cases and aggregate, also an integration test for the main controller.

### TO DO

With more time, I would like to:
- Handle the errors in a better way, wrapping them better to give more information.
- Put the created domain events in good use, to trigger some other actions from possible subscribers.
- Finish testing to add more coverage.
- Set up some real DB instead of doing it in memory.
- Use terraform to create some of the resources (such as Datadog monitors).

### CI/CD

I would use Github actions with some basic steps to ensure tests pass, build it and deploy it.

### Monitoring

To ensure we have a fine observability of our application, I would:
- Use Datadog to create some dashboards and feed them with metrics, ideally raised when creating any domain event.
- Create some alerts on specific metrics, to be warned on a team channel (i.g. Slack) about what happened.

### Docker

Docker in a dev environment has been initialized, but not developed. With more time:
- I would have implemented some API operations to test it locally, with some useful make commands.
