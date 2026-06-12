cloud-gateway: 
  enables security check by Reactive spring security over request to backend services as provider or comsumer service.
    jwt is configured for sessionless authentication.
    authoritiy is categorized by RBAC fashion where users have different roles with respective permissions.

dept-consumer-feign:
  First access point for request coming through gateway service. which then invokes provider service for real data request.
  It communicates with provider by open-feign.
  configured with resilien4j for circuitbreaker and rate-limiter

dept-provider:
  provides the api for departments data as for dept creation, dept list, single dept data.. etc.

nacos is set up in another server for registration of all the services.
