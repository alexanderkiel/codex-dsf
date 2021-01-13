# Docker Test Setup

This directory contains a Docker Compose file containing a complete setup which consists of three sites communicating over the DFS middleware. Of the three sites, one is the ZARS which represents the central application and register entity and the other two are DIC's which are data integration centers.

The goal of this setup is to test feasibility query requests which are initiated at the ZARS and answered from the two DIC's.

Before you can start any site, you have to run maven in order to build certificates and test data:

```sh
mvn -f ../dsf-tools/dsf-tools-test-data-generator/pom.xml package
```

After that, you can start the ZARS FHIR Inbox using:

```sh
docker-compose up -d zars-fhir-proxy
```

It's a good idea to keep the log of the ZARS FHIR Inbox open:

```sh
docker-compose logs -f zars-fhir-app
```

After that, you can query the [CapabilityStatement][1] of the inbox:

```sh
curl --resolve zars:443:127.0.0.1 \
  --cacert certs/ca.pem --cert-type P12 --cert certs/test-user.p12:password \
  -H accept:application/fhir+json \
  -s https://zars/fhir/metadata |\
  jq '.software, .implementation'
```

As you can see in the above command, a port mapping is created on port 443. In order to be able to access not only the zars, but also the other sites, you have to use the domain name `zars` here. With [curl][2], you can specify a custom resolver, which will resolve the host `zars` to localhost. An alternative would be to create an entry in your `/etc/hosts`. The next line in the command is about Client and CA Certificates.


After starting the ZARS, you can start the DIC-1 FHIR Inbox using:

```sh
docker-compose up -d dic-1-fhir-proxy
```

The following command should return the CapabilityStatement:

```sh
curl --resolve dic-1:443:127.0.0.1 \
  --cacert certs/ca.pem --cert-type P12 --cert certs/test-user.p12:password \
  -H accept:application/fhir+json \
  -s https://dic-1/fhir/metadata |\
  jq '.software, .implementation'
```

## Debugging

You can use the following env var and port mapping to enable debugging of one container:

```
ports:
- 5005:5005
environment:
  EXTRA_JVM_ARGS: -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

[1]: <https://www.hl7.org/fhir/capabilitystatement.html>
[2]: <https://curl.se>
